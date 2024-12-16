package com.example.moveon.repository;

import java.util.List;
import java.util.Optional;

import com.example.moveon.domain.RankingHistory;
import com.example.moveon.web.dto.ranking.UserRankingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface RankingHistoryRepository extends JpaRepository<RankingHistory, Long> {
    @Query("""
			SELECT new com.example.moveon.web.dto.ranking.UserRankingResponse
			(u.id, u.nickname, u.profileImage, rh.currentPixelCount, rh.ranking)
			FROM RankingHistory rh 
			INNER JOIN User u on u.id = rh.userId 
			WHERE rh.year = :requestYear AND rh.week = :requestWeek
			ORDER BY rh.ranking ASC 
			LIMIT 30 
		""")
    List<UserRankingResponse> findAllByYearAndWeek(@Param("requestYear") int year, @Param("requestWeek") int week);

    Optional<RankingHistory> findByUserIdAndYearAndWeek(
            @Param("userId") Long userId,
            @Param("requestYear") int year,
            @Param("requestWeek") int week);

    void deleteByUserIdAndYearAndWeek(Long userId, int year, int week);
}