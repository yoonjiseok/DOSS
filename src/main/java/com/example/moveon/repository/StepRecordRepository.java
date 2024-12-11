package com.example.moveon.repository;

import com.example.moveon.domain.step_record;
import com.example.moveon.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRecordRepository extends JpaRepository<step_record, Long> {
}
