package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
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

    @Builder(builderMethodName = "createFittingModel")
    public FittingModel(String imageUrl, User user) {
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public void setUser(User user) {
        // remove existing relation
        if (this.user != null) {
            this.user.getFittingModels().remove(this);
        }

        // set new relation
        this.user = user;
        if (user != null && !user.getFittingModels().contains(this)) {
            user.getFittingModels().add(this);
        }
    }
}
