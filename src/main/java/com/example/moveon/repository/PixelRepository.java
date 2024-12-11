package com.example.moveon.repository;

import com.example.moveon.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixelRepository extends JpaRepository<user, Long> {

}
