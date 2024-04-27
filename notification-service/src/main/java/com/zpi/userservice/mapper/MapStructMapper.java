package com.zpi.userservice.mapper;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapStructMapper {

    //left as reference
//    void updateAppUser(@MappingTarget AppUser finalUser, AppUser inputUser);
}
