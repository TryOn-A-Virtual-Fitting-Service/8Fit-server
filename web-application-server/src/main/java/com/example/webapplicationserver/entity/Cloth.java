package com.example.webapplicationserver.entity;

import com.example.webapplicationserver.entity.base.BaseEntity;
import com.example.webapplicationserver.enums.Category;
import com.example.webapplicationserver.enums.Site;
import jakarta.persistence.*;

@Entity
@Table(name = "clothes")
public class Cloth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Site site;

    @Column(nullable = true, length = 255, name = "cloth_image_url")
    private String imageUrl;

    @Column(nullable = true, length = 255, name = "product_page_url")
    private String pageUrl;
}
