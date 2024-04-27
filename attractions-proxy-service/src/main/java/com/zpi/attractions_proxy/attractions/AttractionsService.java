package com.zpi.attractions_proxy.attractions;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionsService {
    private final GeoApiContext context;

    private static final int RADIUS_DISTANCE = 50000;

    public List<AttractionCandidateDto> findCandidates(String name) {
        var foundCandidates = PlacesApi.textSearchQuery(context, name)
                                       .awaitIgnoreError();

        var result = convertToAttractionCandidateDto(foundCandidates.results);
        getUrl(result);
        System.out.println(foundCandidates);
        return result;
    }

    public List<AttractionCandidateDto> findNearbyCandidates(Double longitude, Double latitude, String  queryCategory, RankByType rankByType) {
        var coordinates = new LatLng(latitude, longitude);
        var rankBy = parseToRankBy(rankByType);

        var foundCandidates = Optional.ofNullable(queryCategory)
                                      .map(category -> findNearbyCandidatesByLocationAndCategory(coordinates, category, rankBy))
                                      .orElseGet(() -> findNearbyCandidatesByLocation(coordinates));

        var result = convertToAttractionCandidateDto(foundCandidates.results);
        getUrl(result);
        return result;
    }

    private PlacesSearchResponse findNearbyCandidatesByLocation(LatLng coordinates) {
        log.info("Coordinates {}{}", coordinates.lat, coordinates.lng);
        var request = PlacesApi.nearbySearchQuery(context, coordinates);
        request.radius(RADIUS_DISTANCE);

        return request.awaitIgnoreError();
    }

    private PlacesSearchResponse findNearbyCandidatesByLocationAndCategory(LatLng coordinates, String category, RankBy rankBy) {
        try {
            var placeType = PlaceType.valueOf(category.toUpperCase());
            log.info("Performing search by location and category");
            log.info("Place type is: {}", placeType);
            log.info("RankBy type is: " + rankBy);
            var request =  PlacesApi.nearbySearchQuery(context, coordinates);
            if (rankBy.equals(RankBy.PROMINENCE)) {
                request.radius(RADIUS_DISTANCE);
            }
            else {
                request.rankby(rankBy);
            }
            request.type(placeType);

            return request.awaitIgnoreError();

        } catch (Exception ex) {
            log.info("Wrong place type, performing textSearch query");
            var request = PlacesApi.textSearchQuery(context, category, coordinates);
            request.radius(RADIUS_DISTANCE);
            return request.awaitIgnoreError();
        }
    }

    private List<AttractionCandidateDto> convertToAttractionCandidateDto(PlacesSearchResult[] foundCandidates) {
        if(foundCandidates == null)
            return new ArrayList<>();

        var result = new ArrayList<AttractionCandidateDto>();
        for (PlacesSearchResult foundCandidate : foundCandidates) {
            var candidate = new AttractionCandidateDto(foundCandidate.name,
                                                       foundCandidate.geometry.location.lat,
                                                       foundCandidate.geometry.location.lng,
                                                       foundCandidate.placeId,
                                                       foundCandidate.photos == null ? null : foundCandidate.photos[0].photoReference,
                                                       foundCandidate.formattedAddress);
            result.add(candidate);
        }
        return result;
    }

    private List<AttractionCandidateDto> getUrl(List<AttractionCandidateDto> candidates) {
        if(candidates == null)
            return new ArrayList<>();

        for (AttractionCandidateDto candidate : candidates) {
            var placeDetails = PlacesApi.placeDetails(context, candidate.getPlaceId())
                                        .fields(PlaceDetailsRequest.FieldMask.URL,
                                                PlaceDetailsRequest.FieldMask.OPENING_HOURS,
                                                PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS)
                                        .awaitIgnoreError();
            candidate.setUrl(placeDetails.url.toString());
            candidate.setOpeningHours(new String[] {});
            candidate.setAddress(placeDetails.formattedAddress);

        }
        return candidates;
    }

    public DistanceMatrix getDistanceMatrix(List<LatLng> attractionsCoordinates) {
        var attractionsCoordinatesArray = attractionsCoordinates.toArray(new LatLng[0]);
        return DistanceMatrixApi.newRequest(context)
                                .origins(attractionsCoordinatesArray)
                                .destinations(attractionsCoordinatesArray)
                                .awaitIgnoreError();
    }

    private RankBy parseToRankBy(RankByType rankByType) {
        if (rankByType == null) {
            return RankBy.PROMINENCE;
        }
        return rankByType == RankByType.DISTANCE ? RankBy.DISTANCE : RankBy.PROMINENCE;
    }

}
