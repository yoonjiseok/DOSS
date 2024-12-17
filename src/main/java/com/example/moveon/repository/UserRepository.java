package com.example.moveon.repository;

import java.util.Optional;

import com.example.moveon.domain.User;
import com.example.moveon.domain.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndEmail(Provider provider, String email);

    Optional<User> findByNickname(String nickname);
}
