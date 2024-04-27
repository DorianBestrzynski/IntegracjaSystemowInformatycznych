package com.zpi.tripgroupservice.proxy;


import com.zpi.tripgroupservice.config.CustomFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "coordinates", url = "${app.attractions-proxy}:8090/api/v1", configuration = CustomFeignConfiguration.class)
public interface CoordinatesProxy {

    @GetMapping()
    Double[] findCoordinates(String startLocation);

}
