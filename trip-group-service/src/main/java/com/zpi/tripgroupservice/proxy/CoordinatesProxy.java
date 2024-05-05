package com.zpi.tripgroupservice.proxy;


import com.zpi.tripgroupservice.config.CustomFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "coordinates", url = "${app.attractions-proxy}:8090/api/v1/coordinates", configuration = CustomFeignConfiguration.class)
public interface CoordinatesProxy {

    @GetMapping()
    Double[] findCoordinates(@RequestParam String startLocation);

}
