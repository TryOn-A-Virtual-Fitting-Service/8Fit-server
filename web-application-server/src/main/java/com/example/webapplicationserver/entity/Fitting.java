package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "fittings")
public class Fitting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "result_image_url")
    private String imageUrl;

    @Column(nullable = true, columnDefinition = "TEXT", name = "llm_advice")
    private String llmAdvice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloth_id", nullable = false)
    private Cloth cloth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
