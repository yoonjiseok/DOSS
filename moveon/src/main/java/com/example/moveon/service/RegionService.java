package com.example.moveon.service;

import java.time.LocalDate;
import java.util.List;

import com.example.moveon.domain.enums.RegionLevel;
import com.example.moveon.repository.RegionRepository;
import com.example.moveon.util.DateUtils;
import com.example.moveon.web.dto.PixelDTO.ClusteredPixelCount;
import com.example.moveon.web.dto.PixelDTO.RegionInfo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class 	RegionService {
    private static final int WGS84_SRID = 4326;
    private static final int CITY_LEVEL_THRESHOLD = 70000;
    private final RegionRepository regionRepository;
    private final GeometryFactory geometryFactory;

    public List<ClusteredPixelCount> getIndividualModeClusteredPixelCount(
            double currentLatitude,
            double currentLongitude,
            int radius
    ) {
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);
        RegionLevel regionLevel = radius < CITY_LEVEL_THRESHOLD ? RegionLevel.CITY : RegionLevel.PROVINCE;
        LocalDate now = LocalDate.now();

        List<RegionInfo> regions;
        if (radius < CITY_LEVEL_THRESHOLD) {
            regions = regionRepository.findAllIndividualCityRegionsByCoordinate(point, radius,
                    DateUtils.getWeekOfDate(now),
                    now.getYear());
        } else {
            regions = regionRepository.findAllIndividualProvinceRegionsByCoordinate(DateUtils.getWeekOfDate(now),
                    now.getYear());
        }

        return regions.stream().map(region -> ClusteredPixelCount.from(
                region.getRegionId(),
                region.getName(),
                region.getCount(),
                region.getLatitude(),
                region.getLongitude(),
                regionLevel
        )).toList();
    }

    public List<ClusteredPixelCount> getCommunityModeClusteredPixelCount(
            double currentLatitude,
            double currentLongitude,
            int radius
    ) {
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);
        RegionLevel regionLevel = radius < CITY_LEVEL_THRESHOLD ? RegionLevel.CITY : RegionLevel.PROVINCE;
        LocalDate now = LocalDate.now();

        List<RegionInfo> regions;
        if (radius < CITY_LEVEL_THRESHOLD) {
            regions = regionRepository.findAllCommunityCityRegionsByCoordinate(point, radius,
                    DateUtils.getWeekOfDate(now),
                    now.getYear());
        } else {
            regions = regionRepository.findAllCommunityProvinceRegionsByCoordinate(DateUtils.getWeekOfDate(now),
                    now.getYear());
        }

        return regions.stream().map(region -> ClusteredPixelCount.from(
                region.getRegionId(),
                region.getName(),
                region.getCount(),
                region.getLatitude(),
                region.getLongitude(),
                regionLevel
        )).toList();
    }

    public List<ClusteredPixelCount> getIndividualHistoryClusteredPixelCount(
            double currentLatitude,
            double currentLongitude,
            int radius,
            Long userId
    ) {
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);
        RegionLevel regionLevel = radius < CITY_LEVEL_THRESHOLD ? RegionLevel.CITY : RegionLevel.PROVINCE;

        List<RegionInfo> regions;
        if (radius < CITY_LEVEL_THRESHOLD) {
            regions = regionRepository.findAllIndividualHistoryCityRegionsByCoordinate(point, radius, userId);
        } else {
            regions = regionRepository.findAllIndividualHistoryProvinceRegionsByCoordinate(userId);
        }

        return regions.stream().map(region -> ClusteredPixelCount.from(
                region.getRegionId(),
                region.getName(),
                region.getCount(),
                region.getLatitude(),
                region.getLongitude(),
                regionLevel
        )).toList();
    }
}
