package com.example.moveon.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.moveon.domain.common.BaseTimeEntity;
import com.example.moveon.domain.Pixel;
import com.example.moveon.domain.User;
import com.example.moveon.exception.AppException;
import com.example.moveon.exception.ErrorCode;
import com.example.moveon.repository.DailyPixelRepository;
import com.example.moveon.repository.PixelRepository;
import com.example.moveon.repository.PixelUserRepository;
import com.example.moveon.repository.UserRepository;
import com.example.moveon.util.DateUtils;
import com.example.moveon.web.dto.PixelDTO.*;

import com.example.moveon.web.dto.UserDTO.VisitedUser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import org.locationtech.jts.geom.Point;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixelReader {
    private static final int WGS84_SRID = 4326;
    private static final String DEFAULT_LOOK_UP_DATE = "2024-07-15";

    private final GeometryFactory geometryFactory;

    private final PixelRepository pixelRepository;
    private final PixelUserRepository pixelUserRepository;
    private final UserRepository userRepository;
    private final UserRankingService userRankingService;

    private final DailyPixelRepository dailyPixelRepository;

    /**
     * 사용자를 중심으로 일정한 반경 내에 개인전 픽셀들을 가져온다.
     * @param currentLatitude 사용자의 위도
     * @param currentLongitude 사용자의 경도
     * @param radius 반경
     * @return List<IndividualModePixelResponse> 픽셀의 정보가 담긴 리스트
     * @author 김민욱
     */
    public List<IndividualModePixelResponse> getNearIndividualModePixelsByCoordinate(
            double currentLatitude,
            double currentLongitude,
            int radius
    ) {
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);
        LocalDate thisWeekStartDate = DateUtils.getThisWeekStartDate();
        return pixelRepository.findAllIndividualModePixelsByCoordinate(point, radius, thisWeekStartDate);
    }

    public List<IndividualModePixelResponse> getNeaerIndividualModePixelsTest(
            int x,
            int y,
            int width,
            int height
    ) {
        LocalDate thisWeekStartDate = DateUtils.getThisWeekStartDate();
        return pixelRepository.findAllIndividualModePixelTest(x, y, width, height, thisWeekStartDate);
    }



    /**
     * 위도 경도를 기준으로 radius 범위 안에서 userId 의 유저가 방문한 픽셀을 가져온다,.
     * @param currentLatitude 사용자의 위도
     * @param currentLongitude 사용자의 경도
     * @param radius 반경
     * @param userId 사용자의 id
     * @return 픽셀의 정보가 담긴 리스트
     */
    public List<IndividualHistoryPixelResponse> getNearIndividualHistoryPixelsByCoordinate(double currentLatitude,
                                                                                           double currentLongitude, int radius, Long userId, LocalDate lookUpDate) {
        Point point = geometryFactory.createPoint(new Coordinate(currentLongitude, currentLatitude));
        point.setSRID(WGS84_SRID);

        if (lookUpDate == null) {
            lookUpDate = LocalDate.parse(DEFAULT_LOOK_UP_DATE);
        }

        return pixelRepository
                .findAllIndividualPixelsHistoryByCoordinate(point, radius, userId, lookUpDate.atStartOfDay());
    }

    /**
     * 특정 픽셀의 정보를 반환한다. 소유주의 정보, 오늘 방문한 사람의 수, 방문한 사람 리스트
     * @param pixelId 찾고자 하는 픽셀의 id
     * @return 소유주의 정보, 오늘 방문한 사람의 수, 방문한 사람 리스트
     */
    public IndividualPixelInfoResponse getIndividualModePixelInfo(Long pixelId) {
        Pixel pixel = pixelRepository.findById(pixelId)
                .orElseThrow(() -> new AppException(ErrorCode.PIXEL_NOT_FOUND));

        List<VisitedUser> visitedUsers = pixelUserRepository.findAllVisitedUserByPixelId(pixelId);
        PixelOwnerUserResponse pixelOwnerUserResponse = getPixelOwnerUserInfo(pixel);

        return IndividualPixelInfoResponse.from(
                pixel,
                pixelOwnerUserResponse,
                visitedUsers.stream().map(VisitedUserInfo::from).toList()
        );
    }

//    public CommunityPixelInfoResponse getCommunityModePixelInfo(Long pixelId) {
//        Pixel pixel = pixelRepository.findById(pixelId)
//                .orElseThrow(() -> new AppException(ErrorCode.PIXEL_NOT_FOUND));
//
//        List<VisitedCommunity> visitedCommunities = pixelUserRepository.findAllVisitedCommunityByPixelId(pixelId);
//        PixelOwnerCommunityResponse pixelOwnerCommunityResponse = getPixelOwnerCommunityInfo(pixel);
//
//        return CommunityPixelInfoResponse.from(
//                pixel,
//                pixelOwnerCommunityResponse,
//                visitedCommunities.stream().map(VisitedCommunityInfo::from).toList()
//        );
//    }

    /**
     * 개인 기록 모드에서 픽셀 방문 기록을 가져온다
     * @param pixelId 기록을 조회할 픽셀
     * @param userId 기록을 조회할 사용자
     * @return IndividualHistoryPixelInfoResponse 기록, 방문 횟수 등을 담고 있는 객체
     * @author 김민욱
     */
    public IndividualHistoryPixelInfoResponse getIndividualHistoryPixelInfo(Long pixelId, Long userId,
                                                                            LocalDate lookUpDate) {
        Pixel pixel = pixelRepository.findById(pixelId)
                .orElseThrow(() -> new AppException(ErrorCode.PIXEL_NOT_FOUND));

        User user = userRepository.getReferenceById(userId);
        if (lookUpDate == null) {
            lookUpDate = LocalDate.parse(DEFAULT_LOOK_UP_DATE);
        }
        List<LocalDateTime> visitList = pixelUserRepository.findAllVisitHistoryByPixelAndUser(pixel, user,
                        lookUpDate.atStartOfDay())
                .stream()
                .map(BaseTimeEntity::getCreatedAt)
                .toList();

        return IndividualHistoryPixelInfoResponse.from(pixel, visitList);
    }

    /**
     * 현재 소유하고 있는 픽셀, 누적으로 방문한 픽셀 갯수를 구한다.
     * @param userId 픽셀 개수를 조회할 사용자
     * @return PixelCountResponse 현재 픽셀, 누적 픽셀을 담고있는 객체
     * @author 김민욱
     */
    public PixelCountResponse getPixelCount(Long userId, LocalDate lookUpDate) {
        Long accumulatePixelCount;
        if (lookUpDate == null) {
            accumulatePixelCount = userRankingService.getAccumulatePixelCount(userId);
        } else {
            accumulatePixelCount = pixelUserRepository.countAccumulatePixelByUserId(userId, lookUpDate.atStartOfDay());
        }

        return PixelCountResponse.builder()
                .currentPixelCount(userRankingService.getCurrentPixelCountFromCache(userId))
                .accumulatePixelCount(accumulatePixelCount)
                .build();
    }

    public PixelCountResponse getCommunityPixelCount(Long communityId) {
        return PixelCountResponse.builder()
                .accumulatePixelCount(0L)
                .build();
    }


    /**
     * 픽셀의 소유주의 정보를 반환한다.
     * @param pixel 소유주의 정보를 알고 싶은 픽셀
     * @return 닉네임, 프로필 사진, 현재 픽실, 누적 픽셀
     */
    private PixelOwnerUserResponse getPixelOwnerUserInfo(Pixel pixel) {
        Long ownerUserId = pixel.getUserId();
        if (ownerUserId == null) {
            return null;
        } else {
            Long accumulatePixelCount = pixelUserRepository.countAccumulatePixelByUserId(ownerUserId,
                    LocalDate.parse(DEFAULT_LOOK_UP_DATE).atStartOfDay());
            Long currentPixelCount = userRankingService.getCurrentPixelCountFromCache(ownerUserId);
            User ownerUser = userRepository.findById(ownerUserId)
                    .orElseThrow(() -> {
                        log.error("pixel {} 의 소유자가 {} 인데 존재하지 않음.", pixel.getId(), ownerUserId);
                        return new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                    });
            return PixelOwnerUserResponse.from(ownerUser, currentPixelCount, accumulatePixelCount);
        }
    }


    public List<Integer> getDailyPixel(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyPixelRepository.findAllDailyPixel(userId, startDate, endDate);
    }
}
