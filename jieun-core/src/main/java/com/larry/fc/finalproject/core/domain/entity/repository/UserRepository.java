package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //쿼리메서드, jpa에서는 메서드명을 컨벤션으로 지켜주게 되면, 실제로 여기에 맞는 쿼리를 날려준다
}
