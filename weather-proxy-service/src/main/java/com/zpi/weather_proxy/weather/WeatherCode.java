package com.zpi.weather_proxy.weather;

public enum WeatherCode {
    CLEAR_SKY(0, "Clear sky"),
    MAINLY_CLEAR(1, "Mainly clear"),
    PARTLY_CLOUDY(2, "Partly cloudy"),
    OVERCAST(3, "Overcast"),
    FOG(45, "Fog"),
    DEPOSITING_RIME_FOG(48, "Depositing rime fog"),
    LIGHT_DRIZZLE(51, "Drizzle: Light"),
    MODERATE_DRIZZLE(53, "Drizzle: Moderate"),
    DENSE_DRIZZLE(55, "Drizzle: Dense"),
    LIGHT_FREEZING_DRIZZLE(56, "Freezing Drizzle: Light"),
    DENSE_FREEZING_DRIZZLE(57, "Freezing Drizzle: Dense"),
    SLIGHT_RAIN(61, "Rain: Slight"),
    MODERATE_RAIN(63, "Rain: Moderate"),
    HEAVY_RAIN(65, "Rain: Heavy"),
    LIGHT_FREEZING_RAIN(66, "Freezing Rain: Light"),
    HEAVY_FREEZING_RAIN(67, "Freezing Rain: Heavy"),
    SLIGHT_SNOW_FALL(71, "Snow fall: Slight"),
    MODERATE_SNOW_FALL(73, "Snow fall: Moderate"),
    HEAVY_SNOW_FALL(75, "Snow fall: Heavy"),
    SNOW_GRAINS(77, "Snow grains"),
    SLIGHT_RAIN_SHOWERS(80, "Rain showers: Slight"),
    MODERATE_RAIN_SHOWERS(81, "Rain showers: Moderate"),
    VIOLENT_RAIN_SHOWERS(82, "Rain showers: Violent"),
    SLIGHT_SNOW_SHOWERS(85, "Snow showers: Slight"),
    HEAVY_SNOW_SHOWERS(86, "Snow showers: Heavy"),
    SLIGHT_THUNDERSTORM(95, "Thunderstorm: Slight"),
    MODERATE_THUNDERSTORM(96, "Thunderstorm: Moderate"),
    SLIGHT_HEAVY_HAIL_THUNDERSTORM(99, "Thunderstorm with heavy hail: Slight");

    private final int code;
    private final String description;

    WeatherCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static WeatherCode fromCode(int code) {
        for (WeatherCode wc : WeatherCode.values()) {
            if (wc.code == code) {
                return wc;
            }
        }
        throw new IllegalArgumentException("Weather code " + code + " is not recognized.");
    }
}
