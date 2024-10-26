package com.example.webapplicationserver.repository;

import com.example.webapplicationserver.entity.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClothRepository extends JpaRepository<Cloth, Long> {
}
