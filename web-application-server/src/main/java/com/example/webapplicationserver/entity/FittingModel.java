package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "fitting_models")
public class FittingModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 255, name = "model_image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
