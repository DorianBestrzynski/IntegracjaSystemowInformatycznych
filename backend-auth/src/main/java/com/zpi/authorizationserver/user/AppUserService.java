package com.zpi.authorizationserver.user;

import com.zpi.authorizationserver.dto.RegisterRequestDto;
import com.zpi.authorizationserver.dto.UserDto;
import com.zpi.authorizationserver.event.Constants;
import com.zpi.authorizationserver.event.UserRegisteredEvent;
import com.zpi.authorizationserver.exceptions.ApiPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static com.zpi.authorizationserver.event.Constants.USER_REGISTERED_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;


    public List<UserDto> getUsers(List<Long> usersIds) {

        List<AppUser> usersList = appUserRepository.findAllById(usersIds);

        return usersList.parallelStream()
                .map(u -> new UserDto(u.getUserId(),
                        u.getEmail(),
                        u.getPhoneNumber(),
                        u.getFirstName(),
                        u.getSurname()))
                .toList();
    }

    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email).orElseThrow(() -> new ApiPermissionException("Incorrect email or password. Permission denied"));
    }

    public void registerUser(RegisterRequestDto registerRequestDto) {
        if (appUserRepository.findAppUserByEmail(registerRequestDto.email()).isPresent())
            throw new IllegalArgumentException("Account with given email already exists");

        var encodedPassword = passwordEncoder.encode(registerRequestDto.password());
        var password = new Password(encodedPassword);
        var user = new AppUser(registerRequestDto.phoneNumber(),
                registerRequestDto.email(),
                registerRequestDto.firstName(),
                registerRequestDto.surname(),
                registerRequestDto.birthday(),
                LocalDateTime.now(),
                password);
        password.setAppUser(user);
        appUserRepository.save(user);

        var userRegisteredEvent = new UserRegisteredEvent(USER_REGISTERED_EVENT, registerRequestDto.email(),
                registerRequestDto.firstName(), registerRequestDto.surname(), registerRequestDto.phoneNumber(),
                Instant.now());
        rabbitTemplate.convertAndSend("q.user-registration" ,userRegisteredEvent);
        log.error("Here");
    }
}
