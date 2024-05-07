package com.zpi.dayplanservice.attraction;

import com.google.maps.model.DistanceMatrixRow;

public record DistanceMatrixDTO(
        String[] originAddresses,
        String[] destinationAddresses,
        DistanceMatrixRow[] rows
) {}
