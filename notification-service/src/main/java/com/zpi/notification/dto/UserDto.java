package com.zpi.notification.dto;

public record UserDto(Long userId, String phoneNumber, String email, String firstName, String surname) {
}