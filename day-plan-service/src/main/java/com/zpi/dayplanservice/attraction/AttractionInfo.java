package com.zpi.dayplanservice.attraction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttractionInfo {
    private long attractionId;
    private double latitude;
    private double longitude;
}
