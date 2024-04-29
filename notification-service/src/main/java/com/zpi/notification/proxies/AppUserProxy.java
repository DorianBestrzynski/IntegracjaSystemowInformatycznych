package com.zpi.notification.proxies;

import com.zpi.notification.config.CustomFeignConfiguration;
import com.zpi.notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user", url = "${app.user-service}:8081/api/v1/user", configuration = CustomFeignConfiguration.class)
public interface AppUserProxy {

    @PostMapping("/users")
    List<UserDto> getUsersByIds(@RequestHeader("innerCommunication") String header, List<Long> usersIds);

    @GetMapping("/{userId}")
    UserDto getUserById(@RequestHeader("innerCommunication") String header, @PathVariable Long userId);
}