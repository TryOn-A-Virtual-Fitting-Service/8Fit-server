package com.example.webapplicationserver.enums;

import lombok.Getter;

@Getter
public enum Category {
    SLEEVELESS("TOP"),
    SHORT_SLEEVE("TOP"),
    LONG_SLEEVE("TOP"),
    SHORT_PANTS("BOTTOM"),
    LONG_PANTS("BOTTOM"),
    SHOES("FOOTWEAR"),
    OUTWEAR("TOP"),
    ACCESSORY("ACCESSORY");

    private final String superType;

    Category(String superType) {
        this.superType = superType;
    }
}
