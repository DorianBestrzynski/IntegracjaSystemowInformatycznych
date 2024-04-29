package com.zpi.notification.proxies;

import com.zpi.notification.config.CustomFeignConfiguration;
import com.zpi.notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-group", url = "${app.trip-group-service}:8082/api/v1/user-group", configuration = CustomFeignConfiguration.class)
public interface UserGroupProxy {

    @GetMapping("/coordinators")
    List<UserDto> getAllGroupCoordinators(@RequestHeader("innerCommunication") String header, @RequestParam(name = "groupId") Long groupId);
}
