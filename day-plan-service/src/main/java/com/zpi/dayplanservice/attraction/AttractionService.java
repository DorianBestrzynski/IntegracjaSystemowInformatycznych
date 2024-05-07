package com.zpi.dayplanservice.attraction;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.*;
import com.zpi.dayplanservice.aspects.AuthorizeCoordinator;
import com.zpi.dayplanservice.aspects.AuthorizePartOfTheGroup;
import com.zpi.dayplanservice.day_plan.DayPlanService;
import com.zpi.dayplanservice.dto.*;
import com.zpi.dayplanservice.exception.ApiRequestException;
import com.zpi.dayplanservice.mapstruct.MapStructMapper;
import com.zpi.dayplanservice.proxies.AttractionsProxy;
import com.zpi.dayplanservice.proxies.TripGroupProxy;
import com.zpi.dayplanservice.security.CustomUsernamePasswordAuthenticationToken;
import com.zpi.dayplanservice.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zpi.dayplanservice.exception.ExceptionInfo.ATTRACTION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;

    private final DayPlanService dayPlanService;

    private final AttractionsProxy attractionsProxy;

    private final MapStructMapper mapstructMapper;

    private final TripGroupProxy tripGroupProxy;

    private final WeatherService weatherService;

    @AuthorizePartOfTheGroup
    public List<AttractionFullInfoDto> getAllAttractionsForDay(Long groupId, Long dayPlanId) {
        var dayPlan = dayPlanService.getDayPlanById(dayPlanId);
        var date = dayPlan.getDate();
        var attractions = dayPlan.getDayAttractions().stream().toList();
        var weather = weatherService.getWeatherForAttraction(date, getAttractionsInfo(attractions));

        return attractions.stream()
                .map(attraction -> new AttractionFullInfoDto(attraction, weather.get(attraction.getAttractionId())))
                .toList();
    }

    private List<AttractionInfo> getAttractionsInfo(List<Attraction> attractions) {
        return attractions.stream()
                .map(attraction -> new AttractionInfo(attraction.getAttractionId(), attraction.getLatitude(), attraction.getLongitude()))
                .toList();
    }

    public List<AttractionCandidateDto> findCandidates(String name) {
        return attractionsProxy.getCandidates(name);
    }

    public List<AttractionCandidateDto> findNearbyCandidates(Double longitude, Double latitude, String  queryCategory, RankByType rankByType) {
        return attractionsProxy.getNearByCandidates(longitude, latitude, queryCategory, rankByType);
    }

    @Transactional
    @AuthorizeCoordinator
    public Attraction deleteAttraction(Long attractionId, Long dayPlanId) {
        if(attractionId == null || dayPlanId == null || attractionId < 0 || dayPlanId < 0)
            throw new IllegalArgumentException("Attraction id or day plan id is null");

        var dayPlan = dayPlanService.getDayPlanById(dayPlanId);

        var toDelete = dayPlan.deleteAttraction(attractionId);
        toDelete.removeDay(dayPlan);

        if(toDelete.getDays().isEmpty())
            attractionRepository.delete(toDelete);

        return toDelete;
    }

    @Transactional
    public Attraction addAttraction(List<Long> dayPlanIds, AttractionCandidateDto attractionCandidateDto) {
        CustomUsernamePasswordAuthenticationToken authentication = (CustomUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getUserId();
        if(dayPlanIds.isEmpty())
            throw new IllegalArgumentException("Day plan ids cannot be empty");

        if(userId == null)
            throw new IllegalArgumentException("User id cannot be null");

        var days = dayPlanService.getDayPlanById(dayPlanIds, userId);
        if(days.size() != dayPlanIds.size())
            throw new IllegalArgumentException("Day plan not found");

        var attraction = mapstructMapper.getAttractionFromCandidateDto(attractionCandidateDto);
        attraction.addDays(days);

        for (var day : days) {
            day.addAttraction(attraction);
        }

        return attractionRepository.save(attraction);
    }

    @Transactional
    public Attraction editAttraction(Attraction attraction) {
        if(attraction == null)
            throw new IllegalArgumentException("User id or attraction candidate dto is null");

        var currentAttraction = attractionRepository.findById(attraction.getAttractionId()).orElseThrow(() -> new ApiRequestException(ATTRACTION_NOT_FOUND));

        if(attraction.getDescription() == null){
            return currentAttraction;
        }
        currentAttraction.setDescription(attraction.getDescription());
        return attractionRepository.save(currentAttraction);
    }

    public List<AttractionPlanDto> findOptimalDayPlan(Long dayPlanId) {
        var dayPlan = dayPlanService.getDayPlanById(dayPlanId);
        var attractions = new ArrayList<>(dayPlan.getDayAttractions());

        if(dayPlan.getDayPlanStartingPointId() == null) {
            var accommodation = tripGroupProxy.getAccommodation("internalCommunication",dayPlan.getGroupId());
            if(accommodation == null || accommodation.destinationLatitude() == null || accommodation.destinationLongitude() == null)
                return findBestAttractionsOrder(attractions).attractions();

            attractions.add(new Attraction(accommodation.destinationLatitude(), accommodation.destinationLongitude()));
            var result = findBestAttractionsOrder(attractions, attractions.size() - 1).attractions();
            result.remove(0);
            return result;
        }
        else return findBestAttractionsOrder(attractions, IntStream.range(0, attractions.size())
                                                           .filter(i -> attractions.get(i).getAttractionId().equals(dayPlan.getDayPlanStartingPointId()))
                                                           .findAny()
                                                           .orElse(0)).attractions();

    }

    public RouteDto findBestAttractionsOrder(List<Attraction> attractions) {
        long minDistance = Long.MAX_VALUE;
        RouteDto minRoute = null;
        for (int i = 0; i < attractions.size(); i++) {
            var route = findBestAttractionsOrder(attractions, i);
            if (route.distance() < minDistance) {
                minDistance = route.distance();
                minRoute = route;
            }
        }
        return minRoute;
    }

    public RouteDto findBestAttractionsOrder(List<Attraction> attractions, int startingPointIndex) {
        var distanceMatrix = getDistanceMatrix(getCoordinates(attractions));
        var attractionList = new LinkedHashSet<Attraction>();
        var attractionDtoList = new ArrayList<AttractionPlanDto>();

        int currentAttractionIndex = startingPointIndex;
        attractionList.add(attractions.get(currentAttractionIndex));
        attractionDtoList.add(new AttractionPlanDto(attractions.get(currentAttractionIndex)));

        var routeDistance = 0L;

        while(attractions.size() != attractionList.size()) {
            var minDistance = Long.MAX_VALUE;
            var rowElems = distanceMatrix.rows[currentAttractionIndex].elements;
            int bestAttractionIndex = -1;

            for(int i = 0; i < rowElems.length; i++) {
                if(i == currentAttractionIndex)
                    continue;

                if(rowElems[i].distance.inMeters < minDistance && !attractionList.contains(attractions.get(i))) {
                    bestAttractionIndex = i;
                    minDistance = rowElems[i].distance.inMeters;
                }
            }

            if(minDistance != Long.MAX_VALUE) {
                routeDistance += minDistance;
                attractionDtoList.get(attractionDtoList.size() - 1)
                                 .setDistanceToNextAttraction(Long.valueOf(minDistance)
                                                                  .doubleValue());
            }


            attractionList.add(attractions.get(bestAttractionIndex));
            attractionDtoList.add(new AttractionPlanDto(attractions.get(bestAttractionIndex)));
            currentAttractionIndex = bestAttractionIndex;
        }

        return new RouteDto(new ArrayList<>(attractionDtoList), routeDistance);
    }

    private DistanceMatrix getDistanceMatrix(List<LatLng> coordinates) {
        var dto =  attractionsProxy.getDistanceMatrix(coordinates);
        return new DistanceMatrix(dto.originAddresses(), dto.destinationAddresses(), dto.rows());
    }

    private List<LatLng> getCoordinates(List<Attraction> attractions) {
        return attractions.stream()
                          .map(attraction -> new LatLng(attraction.getLatitude(), attraction.getLongitude()))
                          .collect(Collectors.toList());
    }
}
