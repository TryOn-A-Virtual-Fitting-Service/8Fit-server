//package com.example.webapplicationserver.model;
//
//import com.example.webapplicationserver.entity.Cloth;
//
//public class ClothFixture {
//    private ClothFixture() {
//        throw new AssertionError("This class cannot be instantiated");
//    }
//
//    private static Cloth.ClothBuilder createBaseTestCloth(String imageUrl) {
//        return Cloth.createCloth()
//                .imageUrl(imageUrl)  // NOT NULL
//                .site(null)
//                .pageUrl(null)
//                .category(null);
//    }
//
//    public static Cloth createTestCloth(String imageUrl) {
//        return createBaseTestCloth(imageUrl)
//                .build();
//    }
//}
