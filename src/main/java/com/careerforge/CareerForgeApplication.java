// src/main/java/com/careerforge/CareerForgeApplication.java
package com.careerforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching   // ← Required to activate @Cacheable / @CacheEvict annotations
public class CareerForgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareerForgeApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  CareerForge API running on port 8080");
        System.out.println("===========================================");
    }
}