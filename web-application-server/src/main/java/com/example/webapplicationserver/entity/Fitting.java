package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "fittings")
public class Fitting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "result_image_url")
    private String imageUrl;

    @Column(nullable = true, columnDefinition = "TEXT", name = "llm_advice")
    private String llmAdvice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cloth_id", nullable = false)
    private Cloth cloth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitting_model_id", nullable = false)
    private FittingModel fittingModel;


    @Builder(builderMethodName = "createFitting")
    public Fitting(String imageUrl, String llmAdvice, Cloth cloth, FittingModel fittingModel) {
        this.imageUrl = imageUrl;
        this.llmAdvice = llmAdvice;
        this.cloth = cloth;
        setFittingModel(fittingModel);
    }


    public void setFittingModel(FittingModel fittingModel) {
        // remove existing relation
        if (this.fittingModel != null) {
            this.fittingModel.getFittings().remove(this);
        }

        // set new relation
        this.fittingModel = fittingModel;
        if (fittingModel != null) {
            fittingModel.getFittings().add(this);
        }
    }
}
