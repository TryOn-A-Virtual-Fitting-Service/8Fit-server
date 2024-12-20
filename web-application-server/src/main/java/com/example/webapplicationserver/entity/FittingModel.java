package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "fitting_models")
public class FittingModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(nullable = false, length = 255, name = "model_image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fittingModel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @OrderBy("id DESC")
    private final List<Fitting> fittings = new ArrayList<>();


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
        if (user != null) {
            user.getFittingModels().add(this);
        }
    }


    public void addFitting(Fitting fitting) {
        fittings.add(fitting);
        fitting.setFittingModel(this);
    }

    public void removeFitting(Fitting fitting) {
        fittings.remove(fitting);
        fitting.setFittingModel(null);
    }
}
