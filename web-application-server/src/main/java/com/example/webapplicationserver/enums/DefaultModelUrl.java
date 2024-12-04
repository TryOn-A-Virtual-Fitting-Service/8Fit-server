package com.example.webapplicationserver.enums;

import lombok.Getter;

@Getter
public enum DefaultModelUrl {
    MALE_MODEL("https://8fit.xyz/images/models/default-male.png"),
    FEMALE_MODEL("https://8fit.xyz/images/models/default-female.png");

    private final String url;

    DefaultModelUrl(String url) {
        this.url = url;
    }
}
