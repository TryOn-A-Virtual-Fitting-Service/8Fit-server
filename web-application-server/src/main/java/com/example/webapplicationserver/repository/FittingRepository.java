package com.example.webapplicationserver.repository;

import com.example.webapplicationserver.entity.Fitting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FittingRepository extends JpaRepository<Fitting, Long> {
}
