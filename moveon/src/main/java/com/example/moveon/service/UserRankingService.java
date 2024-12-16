package com.example.moveon.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.moveon.domain.Pixel;
import com.example.moveon.domain.RankingHistory;
import com.example.moveon.domain.User;
import com.example.moveon.exception.AppException;
import com.example.moveon.exception.ErrorCode;
import com.example.moveon.repository.RankingHistoryRepository;
import com.example.moveon.repository.UserRankingRedisRepository;
import com.example.moveon.repository.UserRepository;
import com.example.moveon.util.DateUtils;
import com.example.moveon.web.dto.ranking.Ranking;
import com.example.moveon.web.dto.ranking.UserRankingResponse;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRankingService {
    private final UserRankingRedisRepository userRankingRedisRepository;
    private final UserRepository userRepository;
    private final RankingHistoryRepository rankingHistoryRepository;

    public void updateCurrentPixelRanking(Pixel targetPixel, Long occupyingUserId) {
        Long originalOwnerUserId = targetPixel.getUserId();
        LocalDateTime thisWeekStart = DateUtils.getThisWeekStartDate().atTime(0, 0);
        LocalDateTime userOccupiedAt = targetPixel.getUserOccupiedAt();

        if (Objects.equals(originalOwnerUserId, occupyingUserId)) {
            if (userOccupiedAt.isAfter(thisWeekStart)) {
                return;
            }
            userRankingRedisRepository.increaseCurrentPixelCount(occupyingUserId);
        } else {
            if (originalOwnerUserId == null || userOccupiedAt.isBefore(thisWeekStart)) {
                userRankingRedisRepository.increaseCurrentPixelCount(occupyingUserId);
            } else {
                updateCurrentPixelRankingAfterOccupy(occupyingUserId, originalOwnerUserId);
            }
        }
    }

    public void updateAccumulatedRanking(Long userId) {
        userRankingRedisRepository.increaseAccumulatePixelCount(userId);
    }

    /**
     * 픽셀을 새로 차지하는 유저의 점수는 1증가, 빼앗긴 유저의 점수는 1감소
     * @param occupyingUserId 픽셀을 새로 차지하는 유저
     * @param deprivedUserId 픽셀을 뺴앗긴 유저
     */
    public void updateCurrentPixelRankingAfterOccupy(Long occupyingUserId, Long deprivedUserId) {
        userRankingRedisRepository.increaseCurrentPixelCount(occupyingUserId);
        userRankingRedisRepository.decreaseCurrentPixelCount(deprivedUserId);
    }

    /**
     * 현재 소유한 픽셀의 개수를 반환한다.
     * @param userId 사용자 id
     * @return 현재 소유한 픽셀의 개수
     */
    public Long getCurrentPixelCountFromCache(Long userId) {
        return userRankingRedisRepository.getCurrentPixelCount(userId).orElse(0L);
    }

    public Long getAccumulatePixelCount(Long userId) {
        return userRankingRedisRepository.getAccumulatePixelCount(userId).orElse(0L);
    }

    /**
     * 모든 유저의 순위를 반환한다. 최대 30개
     * @return 모든 유저의 순위
     */
    public List<UserRankingResponse> getCurrentPixelAllUserRankings(LocalDate lookUpDate) {
        if (lookUpDate == null) {
            lookUpDate = LocalDate.now();
        }

        if (DateUtils.isDateInCurrentWeek(lookUpDate)) {
            return getCurrentWeekCurrentPixelRankings();
        } else {
            return getPastWeekCurrentPixelRankingsByDate(lookUpDate);
        }
    }

    private List<UserRankingResponse> getPastWeekCurrentPixelRankingsByDate(LocalDate lookUpDate) {
        return rankingHistoryRepository.findAllByYearAndWeek(
                lookUpDate.getYear(),
                DateUtils.getWeekOfDate(lookUpDate)
        );
    }

    private List<UserRankingResponse> getCurrentWeekCurrentPixelRankings() {
        List<Ranking> rankings = userRankingRedisRepository.getRankingsWithCurrentPixelCount();
        Map<Long, User> users = getRankedUsers(rankings);

        rankings = filterNotExistUsers(rankings, users);

        return rankings.stream()
                .map(ranking -> {
                    User user = users.get(ranking.getId());
                    return UserRankingResponse.from(user, ranking.getRank(), ranking.getCurrentPixelCount());
                })
                .collect(Collectors.toList());
    }

    private List<Ranking> filterNotExistUsers(List<Ranking> rankings, Map<Long, User> users) {
        return rankings.stream()
                .filter(ranking -> {
                    if (users.containsKey(ranking.getId())) {
                        return true;
                    } else {
                        log.error("[filterNotExistUsers] userId {}은 데이터베이스에 존재하지 않음", ranking.getId());
                        return false;
                    }
                })
                .toList();
    }

    /**
     * 30위 안에 있는 유저 entity 를 반환한다.
     * @param rankings 랭킹 정보 (유저 id 와 rank, 점수만 들어있음)
     * @return Map 형식의 User 엔티티
     */
    private Map<Long, User> getRankedUsers(List<Ranking> rankings) {
        Set<Long> userIds = rankings.stream()
                .map(Ranking::getId)
                .collect(Collectors.toSet());
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    /**
     * 유저의 순위 정보를 반환한다.
     * @param userId 사용자 Id
     * @return 유저의 순위 정보
     */
    public UserRankingResponse getUserCurrentPixelRankInfo(Long userId, LocalDate lookUpDate) {
        if (lookUpDate == null) {
            lookUpDate = LocalDate.now();
        }

        if (DateUtils.isDateInCurrentWeek(lookUpDate)) {
            return getCurrentWeekCurrentPixelUserRanking(userId);
        } else {
            return getPastWeekCurrentPixelUserRanking(userId, lookUpDate);
        }
    }

    private UserRankingResponse getPastWeekCurrentPixelUserRanking(Long userId, LocalDate lookUpDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<RankingHistory> rankingHistory = rankingHistoryRepository.findByUserIdAndYearAndWeek(
                userId,
                lookUpDate.getYear(),
                DateUtils.getWeekOfDate(lookUpDate)
        );

        if (rankingHistory.isPresent()) {
            return UserRankingResponse.from(
                    user,
                    rankingHistory.get().getRanking(),
                    rankingHistory.get().getCurrentPixelCount());
        } else {
            return UserRankingResponse.from(user);
        }
    }

    private UserRankingResponse getCurrentWeekCurrentPixelUserRanking(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Long rank = getUserCurrentPixelRankFromCache(userId);
        Long currentPixelCount = getCurrentPixelCountFromCache(userId);
        return UserRankingResponse.from(user, rank, currentPixelCount);
    }

    /**
     * 유저의 순위를 반환한다
     * @param userId 사용자 Id
     * @return 사용자의 순위
     */
    private Long getUserCurrentPixelRankFromCache(Long userId) {
        return userRankingRedisRepository.getCurrentPixelRank(userId)
                .orElseThrow(() -> {
                    log.error("User {} not register at redis", userId);
                    return new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                });
    }
}
