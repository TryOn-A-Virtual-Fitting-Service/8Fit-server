package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloth_id", nullable = false)
    private Cloth cloth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder(builderMethodName = "createFitting")
    public Fitting(String imageUrl, String llmAdvice, Cloth cloth, User user) {
        this.imageUrl = imageUrl;
        this.llmAdvice = llmAdvice;
        this.cloth = cloth;
        setUser(user);
    }

    public void setUser(User user) {
        // remove existing relation
        if (this.user != null) {
            this.user.getFittings().remove(this);
        }

        // set new relation
        this.user = user;
        if (user != null && !user.getFittings().contains(this)) {
            user.getFittings().add(this);
        }
    }
}
