package com.example.webapplicationserver.enums;

import lombok.Getter;

@Getter
public enum Site {
    MUSINSA("MALE"), // 무신사
    ABLY("FEMALE"), // ABLY
    ZIGZAG("FEMALE"), // 지그재그
    ETC("MALE"); // 기타 쇼핑몰

    private final String defaultFittingModel;

    Site(String defaultFittingModel) {
        this.defaultFittingModel = defaultFittingModel;
    }
}
