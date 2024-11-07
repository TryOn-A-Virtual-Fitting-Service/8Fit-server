//package com.example.webapplicationserver.model;
//
//import com.example.webapplicationserver.entity.Cloth;
//import com.example.webapplicationserver.entity.Fitting;
//import com.example.webapplicationserver.entity.User;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//
//public class FittingFixture {
//    private FittingFixture() {
//        throw new AssertionError("This class cannot be instantiated");
//    }
//
//    public static Fitting createTestFitting(User user, Cloth cloth, String imageUrl) {
//        Fitting fitting = new Fitting(imageUrl, null, cloth, user);
//
//        // Reflection으로 createdAt 설정
//        ReflectionTestUtils.setField(fitting, "createdAt", LocalDateTime.now());
//
//        // createdAt 설정 후 관계 설정
//        fitting.setUser(user);
//
//        return fitting;
//    }
//}
