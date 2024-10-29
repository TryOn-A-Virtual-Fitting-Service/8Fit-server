package com.example.webapplicationserver.repository;

import com.example.webapplicationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDeviceId(String deviceId);
}
