package com.example.webapplicationserver.model;


import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;

public final class FittingModelFixture {
    private FittingModelFixture() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static FittingModel.FittingModelBuilder createBaseTestFittingModel(User user) {
        return FittingModel.createFittingModel()
                .user(user)
                .imageUrl("test-image-url");
    }

    public static FittingModel createDefaultTestFittingModelWithUser(User user) {
        return createBaseTestFittingModel(user)
                .build();
    }

    public static FittingModel createTestFittingModelWithUserAndImageUrl(User user, String imageUrl) {
        return createBaseTestFittingModel(user)
                .imageUrl(imageUrl)
                .build();
    }

}
