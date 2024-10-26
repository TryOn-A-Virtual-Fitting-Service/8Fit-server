package com.example.webapplicationserver.repository;

import com.example.webapplicationserver.entity.FittingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FittingModelRepository extends JpaRepository<FittingModel, Long> {
}
