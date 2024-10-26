package com.example.webapplicationserver.repository;

import com.example.webapplicationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
