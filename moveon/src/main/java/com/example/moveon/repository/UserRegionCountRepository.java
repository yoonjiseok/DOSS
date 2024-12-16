package com.example.moveon.repository;

import java.util.Optional;

import com.example.moveon.domain.Region;
import com.example.moveon.domain.UserRegionCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserRegionCountRepository extends JpaRepository<UserRegionCount, Long> {
    @Query("SELECT u FROM UserRegionCount u WHERE u.region = :region AND u.user.id = :user_id")
    Optional<UserRegionCount> findByRegionAndUser(
            @Param("region") Region region,
            @Param("user_id") Long userId
    );
}