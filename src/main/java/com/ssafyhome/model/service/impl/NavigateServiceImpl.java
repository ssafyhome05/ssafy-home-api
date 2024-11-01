package com.ssafyhome.model.service.impl;

import com.ssafyhome.api.tmap.TMapClient;
import com.ssafyhome.model.dao.mapper.SpotMapper;
import com.ssafyhome.model.dto.api.*;
import com.ssafyhome.model.dto.navigate.NavigateDto;
import com.ssafyhome.model.service.NavigateService;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NavigateServiceImpl implements NavigateService {

    private static final int TRANSPORT_NUMS = 3;

    private final TMapClient tMapClient;
    private final SpotMapper spotMapper;
    private final ConvertUtil convertUtil;
    private final NavigateInternalService navigateInternalService;

    public NavigateServiceImpl(
            TMapClient tMapClient,
            SpotMapper spotMapper,
            ConvertUtil convertUtil,
            NavigateInternalService navigateInternalService
    ) {

        this.tMapClient = tMapClient;
        this.spotMapper = spotMapper;
        this.convertUtil = convertUtil;
        this.navigateInternalService = navigateInternalService;
    }

    @Override
    public List<NavigateDto> getNavigates(String type, String aptSeq) {

        TMapPoint start = spotMapper.getTMapPointByAptSeq(aptSeq);
        List<NavigateDto> navigates = new ArrayList<>();
        List<TMapPoint> endPoints = getEndpoints(type, aptSeq, start);
        for (TMapPoint end : endPoints) {
            navigates.add(getNavigate(type, aptSeq, end));
        }

        return navigates;
    }

    @Override
    public NavigateDto getNavigate(String type, String aptSeq, TMapPoint end) {

        TMapPoint start = spotMapper.getTMapPointByAptSeq(aptSeq);
        NavigateDto.RequestParameter requestParameter = NavigateDto.RequestParameter.builder()
                .endPointName(end.getName())
                .endType(type)
                .build();

        Map<String, List<NavigateDto.Route>> routes = new HashMap<>();
        routes.put("car", getCarRoute(start, end));
        routes.put("walk", getWalkRoute(start, end));
        routes.put("transport", getTransportRoute(start, end));

        return NavigateDto.builder()
                .requestParameter(requestParameter)
                .routes(routes)
                .build();
    }

    private List<TMapPoint> getEndpoints(String type, String aptSeq, TMapPoint start) {
        if (type.equals("spot")) {
            try {
                navigateInternalService.findNearestSpot(aptSeq, start);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return spotMapper.getNearestSpotByStartPoint(aptSeq).stream()
                    .map(entity -> TMapPoint.builder()
                            .name(entity.getSpotName())
                            .x(Double.parseDouble(entity.getLongitude()))
                            .y(Double.parseDouble(entity.getLatitude()))
                            .build()
                    )
                    .toList();
        } else {
            String userSeq = SecurityContextHolder.getContext().getAuthentication().getName();
            return spotMapper.getCustomSpotByUser(userSeq).stream()
                    .map(entity -> TMapPoint.builder()
                            .name(entity.getSpotName())
                            .x(Double.parseDouble(entity.getLongitude()))
                            .y(Double.parseDouble(entity.getLatitude()))
                            .build()
                    )
                    .toList();
        }
    }

    private List<NavigateDto.Route> getCarRoute(TMapPoint start, TMapPoint end) {

        TMapCarRouteRequestDto request = TMapCarRouteRequestDto.builder()
                .startX(start.getX())
                .startY(start.getY())
                .endX(end.getX())
                .endY(end.getY())
                .build();
        TMapCarRouteResponseDto response = tMapClient.findCarRoute(1, request);

        List<NavigateDto.Route> routes = new ArrayList<>();
        NavigateDto.Route route = new NavigateDto.Route();

        TMapCarRouteResponseDto.Feature startFeature = response.getFeatures().get(0);
        route.setTotalTime(startFeature.getProperties().getTotalTime());
        route.setTotalDistance(startFeature.getProperties().getTotalDistance());
        route.setFare(response.getFeatures().get(0).getProperties().getTaxiFare());
        route.setRouteInfos(
                response.getFeatures().stream()
                        .map(feature -> NavigateDto.Route.RouteInfo.builder()
                                .type(feature.getGeometry().getType())
                                .coordinates(feature.getGeometry().getCoordinates())
                                .build())
                        .toList()
        );
        routes.add(route);
        return routes;
    }

    private List<NavigateDto.Route> getWalkRoute(TMapPoint start, TMapPoint end) {

        TMapWalkRouteRequestDto request = TMapWalkRouteRequestDto.builder()
                .startName(URLEncoder.encode(start.getName(), StandardCharsets.UTF_8))
                .startX(start.getX())
                .startY(start.getY())
                .endName(URLEncoder.encode(end.getName(), StandardCharsets.UTF_8))
                .endX(end.getX())
                .endY(end.getY())
                .build();
        TMapWalkRouteResponseDto response = tMapClient.findWalkRoute(1, request);

        List<NavigateDto.Route> routes = new ArrayList<>();
        NavigateDto.Route route = new NavigateDto.Route();

        TMapWalkRouteResponseDto.Feature startFeature = response.getFeatures().get(0);
        route.setTotalTime(startFeature.getProperties().getTotalTime());
        route.setTotalDistance(startFeature.getProperties().getTotalDistance());
        route.setRouteInfos(
                response.getFeatures().stream()
                        .map(feature -> NavigateDto.Route.RouteInfo.builder()
                                .type(feature.getGeometry().getType())
                                .coordinates(feature.getGeometry().getCoordinates())
                                .build())
                        .toList()
        );
        routes.add(route);
        return routes;
    }

    private List<NavigateDto.Route> getTransportRoute(TMapPoint start, TMapPoint end) {

        TMapTransportRouteRequestDto request = TMapTransportRouteRequestDto.builder()
                .startX(start.getX())
                .startY(start.getY())
                .endX(end.getX())
                .endY(end.getY())
                .count(TRANSPORT_NUMS)
                .build();
        TMapTransportRouteResponseDto response = tMapClient.findTransportRoute(request);
        if (response.getMetaData() == null) return  new ArrayList<>();

        return response.getMetaData().getPlan().getItineraries().stream().map(this::getRouteByItinerary).toList();
    }

    private NavigateDto.Route getRouteByItinerary(TMapTransportRouteResponseDto.MetaData.Plan.Itinerary itinerary) {

        NavigateDto.Route route = new NavigateDto.Route();
        route.setTotalTime(itinerary.getTotalTime());
        route.setTotalDistance(itinerary.getTotalDistance());
        route.setFare(itinerary.getFare().getRegular().getTotalFare());
        route.setRouteInfos(itinerary.getLegs().stream().map(this::getRouteInfoByLeg).toList());
        return route;
    }

    private NavigateDto.Route.RouteInfo getRouteInfoByLeg(TMapTransportRouteResponseDto.MetaData.Plan.Itinerary.Leg leg) {

        String lineString;
        NavigateDto.Route.RouteInfo.TransferParameter transferParameter = null;
        if (leg.getMode().equals("WALK")) {
            if(leg.getSteps() != null) {
                lineString = leg.getSteps().stream().map(step -> step.getLinestring()).collect(Collectors.joining(" "));
            }
            else {
                lineString = leg.getStart().getLon() + "," + leg.getStart().getLat() + " " + leg.getEnd().getLon() + "," + leg.getEnd().getLat();
            }
        }
        else {
            lineString = leg.getPassShape().getLinestring();
            transferParameter = NavigateDto.Route.RouteInfo.TransferParameter.builder()
                    .mode(leg.getMode())
                    .color(leg.getRouteColor())
                    .id(leg.getRouteId())
                    .name(leg.getRoute())
                    .start(convertUtil.convert(leg.getStart(), NavigateDto.Route.RouteInfo.TransferParameter.Point.class))
                    .end(convertUtil.convert(leg.getEnd(), NavigateDto.Route.RouteInfo.TransferParameter.Point.class))
                    .build();
        }

        return NavigateDto.Route.RouteInfo.builder()
                .type(leg.getType())
                .transferParameter(transferParameter)
                .coordinates(Arrays.stream(lineString.split(" "))
                        .map(coordinate -> (Object)Arrays.stream(coordinate.split(",")).toList())
                        .toList()
                )
                .build();
    }
}
