// src/main/java/com/careerforge/config/S3Config.java
package com.careerforge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    /*
     * @Bean creates a singleton — one S3Client for the entire application.
     * Spring injects this same instance wherever S3Client is @Autowired.
     * We don't create "new S3Client()" on every upload — that would be wasteful.
     * This is the DEPENDENCY INVERSION principle in action.
     */

    @Value("${aws.access-key}") private String accessKey;
    @Value("${aws.secret-key}") private String secretKey;
    @Value("${aws.s3.region}")  private String region;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        // Presigner generates time-limited download URLs for private S3 objects
        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }
}
