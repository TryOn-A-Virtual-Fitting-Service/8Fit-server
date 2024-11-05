package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.entity.Cloth;

public class ClothConverter {
    public static Cloth toEntity(String imageUrl) {
        return Cloth.createCloth()
                .imageUrl(imageUrl)
                .site(null)
                .pageUrl(null)
                .category(null)
                .build();
    }
}
