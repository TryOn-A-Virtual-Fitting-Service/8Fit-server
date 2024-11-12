package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.enums.Category;

public class ClothConverter {
    public static Cloth toEntity(String imageUrl, Category category) {
        return Cloth.createCloth()
                .imageUrl(imageUrl)
                .site(null)
                .pageUrl(null)
                .category(category)
                .build();
    }
}
