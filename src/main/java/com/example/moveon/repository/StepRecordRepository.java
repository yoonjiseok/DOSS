package com.example.moveon.repository;

import com.example.moveon.domain.StepRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface StepRecordRepository extends JpaRepository<StepRecord, Long> {
    List<StepRecord> findByUserIdAndRunDateBetween(Long userId, Date startDate, Date endDate);
}
