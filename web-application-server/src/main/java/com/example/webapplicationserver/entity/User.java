package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 255, name = "device_id", unique = true)
    private String device_id;

    // orphanRemoval = True => deletes when fittingModel/fittings's user become null
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FittingModel> fittingModels = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Fitting> fittings = new ArrayList<>();


}




