package com.example.moveon.repository;
import java.time.LocalDate;
import java.util.List;

import com.example.moveon.domain.DailyPixel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DailyPixelRepository extends JpaRepository<DailyPixel, Long> {

    @Query(value = """
		WITH RECURSIVE date_series AS (
		     SELECT :startDate AS date
		     UNION ALL
		     SELECT date + INTERVAL 1 DAY
		     FROM date_series
		     WHERE date < :endDate
		)
		SELECT
		     COALESCE(dp.daily_pixel_count, 0) AS daily_pixel_count
		FROM
		     date_series ds
		LEFT JOIN daily_pixel dp ON ds.date = DATE(dp.created_at) AND dp.user_id = :userId
		ORDER BY
		     ds.date ASC
			""",
            nativeQuery = true)
    List<Integer> findAllDailyPixel(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
