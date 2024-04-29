package com.zpi.weather_proxy.weather;

public class DatesOutOfRangeException extends RuntimeException{
    public DatesOutOfRangeException(String message) {
        super(message);
    }
}
