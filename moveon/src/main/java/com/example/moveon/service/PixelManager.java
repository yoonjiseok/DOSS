package com.example.moveon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.example.moveon.domain.Pixel;
import com.example.moveon.domain.PixelUser;
import com.example.moveon.domain.Region;
import com.example.moveon.domain.UserRegionCount;
import com.example.moveon.exception.AppException;
import com.example.moveon.exception.ErrorCode;
import com.example.moveon.repository.*;
import com.example.moveon.util.DateUtils;
import com.example.moveon.web.dto.PixelDTO.PixelOccupyRequest;
import com.example.moveon.web.dto.PixelDTO.naverapi.ReverseGeocodingResult;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixelManager {
    private static final int WGS84_SRID = 4326;
    private static final double lat_per_pixel = 0.000724;
    private static final double lon_per_pixel = 0.000909;
    private static final double upper_left_lat = 38.240675;
    private static final double upper_left_lon = 125.905952;

    private final PixelRepository pixelRepository;
    private final UserRankingService userRankingService;

    private final PixelUserRepository pixelUserRepository;
    private final GeometryFactory geometryFactory;
    private final ReverseGeoCodingService reverseGeoCodingService;
    private final RegionRepository regionRepository;
    private final UserRegionCountRepository userRegionCountRepository;
    private final UserRepository userRepository;



    @Transactional
    public void occupyPixel(PixelOccupyRequest pixelOccupyRequest) {
        Long occupyingUserId = pixelOccupyRequest.getUserId();

        log.info("[Visit Pixel] x : {}, y: {}, user : {}", pixelOccupyRequest.getX(), pixelOccupyRequest.getY(),
                occupyingUserId);

        if (!isValidCoordinate(pixelOccupyRequest.getX(), pixelOccupyRequest.getY())) {
            throw new AppException(ErrorCode.PIXEL_NOT_FOUND);
        }

        Pixel targetPixel = pixelRepository.findByXAndY(pixelOccupyRequest.getX(),
                        pixelOccupyRequest.getY())
                .orElseGet(() -> createPixel(pixelOccupyRequest.getX(), pixelOccupyRequest.getY()));
        userRankingService.updateCurrentPixelRanking(targetPixel, occupyingUserId);
        updateUserAccumulatePixelCount(targetPixel, occupyingUserId);


        savePixelUser(targetPixel, occupyingUserId);
    }

    private void savePixelUser(Pixel targetPixel, Long occupyingUserId) {
        PixelUser pixelUser = PixelUser.builder()
                .pixel(targetPixel)
                .user(userRepository.getReferenceById(occupyingUserId))
                .build();
        pixelUserRepository.save(pixelUser);
    }

    private boolean isValidCoordinate(Long x, Long y) {
        return x >= 0 && x < 9000 && y >= 0 && y < 8156;
    }

    private Pixel createPixel(Long x, Long y) {
        Long pixelId = getPixelId(x, y);
        Point coordinate = getCoordinate(x, y);
        ReverseGeocodingResult reverseGeocodingResult = getRegion(coordinate);
        log.info("x: {}, y: {} pixel 생성", x, y);
        Region region = reverseGeocodingResult.getRegionId() != null
                ? regionRepository.getReferenceById(reverseGeocodingResult.getRegionId()) : null;

        Pixel pixel = Pixel.builder()
                .id(pixelId)
                .x(x)
                .y(y)
                .coordinate(coordinate)
                .createdAt(LocalDateTime.now())
                .userOccupiedAt(LocalDateTime.of(2024, 6, 1, 0, 0))
                .communityOccupiedAt(LocalDateTime.of(2024, 6, 1, 0, 0))
                .region(region)
                .address(reverseGeocodingResult.getRegionName())
                .build();
        return pixelRepository.save(pixel);
    }

    private ReverseGeocodingResult getRegion(Point coordinate) {
        double longitude = coordinate.getX();
        double latitude = coordinate.getY();
        try {
            return reverseGeoCodingService.getRegionFromCoordinates(longitude, latitude);
        } catch (Exception e) {
            String errorLog = "[Reverse Geocoding Error] longitude : " + longitude + ", latitude : " + latitude + "  ";
            log.error("{}{}", errorLog, e.getMessage(), e);
            return ReverseGeocodingResult.builder().regionId(null).regionName(null).build();
        }
    }

    private Point getCoordinate(Long x, Long y) {
        double currentLongitude = upper_left_lon + (y * lon_per_pixel);
        double currentLatitude = upper_left_lat - (x * lat_per_pixel);
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);
        return point;
    }

    private Long getPixelId(Long x, Long y) {
        return x * 4156 + y + 1;
    }



    private void updateUserAccumulatePixelCount(Pixel targetPixel, Long userId) {
        if (!pixelUserRepository.existsByPixelIdAndUserId(targetPixel.getId(), userId)) {
            updateUserRegionCount(targetPixel, userId);
            userRankingService.updateAccumulatedRanking(userId);
        }
        if (!pixelUserRepository.existsByUserIdAndPixelIdForToday(userId, LocalDateTime.now())) {

        }
    }

    private void updateUserRegionCount(Pixel targetPixel, Long userId) {
        if (targetPixel.getRegion() == null) {
            return;
        }
        UserRegionCount userRegionCount = userRegionCountRepository
                .findByRegionAndUser(targetPixel.getRegion(), userId)
                .orElseGet(() -> createUserRegionCount(targetPixel.getRegion(), userId));
        userRegionCount.increaseCount();
    }

    private UserRegionCount createUserRegionCount(Region region, Long userId) {
        UserRegionCount userRegionCount = UserRegionCount.builder()
                .count(0)
                .region(region)
                .user(userRepository.getReferenceById(userId))
                .build();
        return userRegionCountRepository.save(userRegionCount);
    }





    private boolean isLandTakenFromExistingUser(Pixel targetPixel, Long occupyingUserId) {
        Long originalOwnerUserId = targetPixel.getUserId();
        LocalDateTime thisWeekStart = DateUtils.getThisWeekStartDate().atTime(0, 0);
        LocalDateTime userOccupiedAt = targetPixel.getUserOccupiedAt();
        if (!Objects.equals(originalOwnerUserId, occupyingUserId)) {
            return originalOwnerUserId != null && !userOccupiedAt.isBefore(thisWeekStart);
        } else {
            return false;
        }

    }




}
