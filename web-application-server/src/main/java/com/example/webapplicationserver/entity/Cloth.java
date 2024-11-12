package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import com.example.webapplicationserver.enums.Category;
import com.example.webapplicationserver.enums.Site;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "clothes")
public class Cloth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "category")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "site")
    private Site site;

    @Column(nullable = false, length = 255, name = "cloth_image_url")
    private String imageUrl;

    @Column(nullable = true, length = 255, name = "product_page_url")
    private String pageUrl;

    @Builder(builderMethodName = "createCloth")
    public Cloth(Category category, Site site, String imageUrl, String pageUrl) {
        this.category = category;
        this.site = site;
        this.imageUrl = imageUrl;
        this.pageUrl = pageUrl;
    }

    @Builder(builderMethodName = "createClothWithoutUrlsForTest")
    public Cloth(Category category, Site site) {
        this.category = category;
        this.site = site;
    }
}
