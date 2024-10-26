package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 255, name = "device_id", unique = true)
    private String deviceId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final Set<FittingModel> fittingModels = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final Set<Fitting> fittings = new HashSet<>();

    @Builder(builderMethodName = "createUser")
    public User(String deviceId) {
        this.deviceId = deviceId;

    }

    public void addFittingModel(FittingModel fittingModel) {
        fittingModels.add(fittingModel);
        fittingModel.setUser(this);
    }

    public void removeFittingModel(FittingModel fittingModel) {
        fittingModels.remove(fittingModel);
        fittingModel.setUser(null);
    }

    public void addFitting(Fitting fitting) {
        fittings.add(fitting);
        fitting.setUser(this);
    }

    public void removeFitting(Fitting fitting) {
        fittings.remove(fitting);
        fitting.setUser(null);
    }
}




