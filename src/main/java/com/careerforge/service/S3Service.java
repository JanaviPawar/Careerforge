// src/main/java/com/careerforge/service/S3Service.java
package com.careerforge.service;

import com.careerforge.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    /*
     * OOP — ABSTRACTION:
     * All AWS SDK calls live here. Other classes call uploadFile() / getDownloadUrl() /
     * deleteFile() — they never touch AWS SDK directly.
     * If we switch from AWS to GCP tomorrow, we only change THIS class.
     */

    private final S3Client    s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, Long userId) {
        if (file.isEmpty())
            throw new FileUploadException("File is empty");
        if (!isPdf(file))
            throw new FileUploadException("Only PDF files are allowed for resumes");

        try {
            // Unique key: resumes/42/uuid_resumeV2.pdf
            String s3Key = String.format("resumes/%d/%s_%s",
                    userId, UUID.randomUUID(), file.getOriginalFilename());

            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucketName).key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));
            log.info("Uploaded to S3: {}", s3Key);
            return s3Key;   // Return key — URL is generated separately (key never changes)

        } catch (IOException e) {
            throw new FileUploadException("Upload failed: " + e.getMessage());
        }
    }

    public String generatePresignedUrl(String s3Key) {
        /*
         * Pre-signed URL = temporary access URL for a private S3 object.
         * Expires after 1 hour. The URL contains an auth signature as a query param.
         * Anyone with this URL can download the file for 1 hour — then it expires.
         * FAR safer than making the bucket or object publicly accessible.
         */
        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(r -> r.bucket(bucketName).key(s3Key).build())
                .build();
        return s3Presigner.presignGetObject(presignReq).url().toString();
    }

    public void deleteFile(String s3Key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder().bucket(bucketName).key(s3Key).build());
        log.info("Deleted from S3: {}", s3Key);
    }

    private boolean isPdf(MultipartFile file) {
        return "application/pdf".equals(file.getContentType());
    }
}
