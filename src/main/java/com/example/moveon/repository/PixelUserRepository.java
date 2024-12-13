package com.example.moveon.repository;


import java.time.LocalDateTime;
import java.util.List;

import com.example.moveon.domain.Pixel;
import com.example.moveon.domain.PixelUser;
import com.example.moveon.domain.User;
import com.example.moveon.web.dto.UserDTO.VisitedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PixelUserRepository extends JpaRepository<PixelUser, Long> {
    @Query(value = """
			SELECT pu.pixel_id AS pixelId,
				pu.user_id AS userId,
				u.nickname AS nickname,
				u.profile_image AS profileImage
			FROM pixel_user pu
			JOIN user u ON pu.user_id = u.user_id
			WHERE pu.pixel_id = :pixel_id AND pu.created_at >= current_date()
			GROUP BY pu.user_id;
		""", nativeQuery = true)
    List<VisitedUser> findAllVisitedUserByPixelId(
            @Param("pixel_id") Long pixelId);


    @Query(value = """
		SELECT COUNT(DISTINCT pu.pixel.id) AS count
		FROM PixelUser pu
		WHERE pu.user.id = :userId AND pu.createdAt >= :lookup_date
		""")
    Long countAccumulatePixelByUserId(@Param("userId") Long userId, @Param("lookup_date") LocalDateTime lookUpDate);

    @Query(value = """
			SELECT pu
			FROM PixelUser pu
			WHERE pu.pixel = :pixel AND pu.user = :user AND pu.createdAt >= :lookup_date
			GROUP BY DATE(pu.createdAt)
			ORDER BY pu.createdAt DESC
		""")
    List<PixelUser> findAllVisitHistoryByPixelAndUser(@Param("pixel") Pixel pixel, @Param("user") User user,
                                                      @Param("lookup_date") LocalDateTime lookUpDate);

    @Modifying
    @Query(value = """
			INSERT INTO pixel_user (pixel_id, user_id, created_at, modified_at)
			VALUES (:pixel_id, :user_id, NOW(), NOW())
		""", nativeQuery = true)
    void save(@Param("pixel_id") Long pixelId, @Param("user_id") Long userId);

    boolean existsByPixelIdAndUserId(Long pixelId, Long userId);

    @Query("SELECT COUNT(pu) > 0 FROM PixelUser pu WHERE pu.user.id = :userId AND FUNCTION('DATE', pu.createdAt) = FUNCTION('DATE', :currentDate)")
    boolean existsByUserIdAndPixelIdForToday(
            @Param("userId") Long userId,
            @Param("currentDate") LocalDateTime currentDate
    );

    //boolean existsByPixelIdAndCommunityId(Long pixelId, Long communityId);
}
