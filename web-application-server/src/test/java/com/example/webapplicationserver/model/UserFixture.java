//package com.example.webapplicationserver.model;
//
//import com.example.webapplicationserver.entity.User;
//
//public final class UserFixture {
//
//    // private initializer to prevent instantiation
//    private UserFixture() {
//        throw new AssertionError("This class cannot be instantiated");
//    }
//
//    public static User.UserBuilder createBaseTestUser() {
//        return User.createUser()
//                .deviceId("test-device-id");
//    }
//
//    public static User createDefaultTestUser() {
//        return createBaseTestUser().build();
//    }
//
//    public static User createTestUserWithDeviceId(String deviceId) {
//        return createBaseTestUser()
//                .deviceId(deviceId)
//                .build();
//    }
//
//}
