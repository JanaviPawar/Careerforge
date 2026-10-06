// src/main/java/com/careerforge/service/ResumeService.java
package com.careerforge.service;

import com.careerforge.entity.Resume;
import com.careerforge.entity.User;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.exception.UnauthorizedException;
import com.careerforge.repository.ResumeRepository;
import com.careerforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final S3Service        s3Service;
    private final ResumeRepository resumeRepository;
    private final UserRepository   userRepository;

    public Resume upload(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String s3Key = s3Service.uploadFile(file, userId);

        List<Resume> existing = resumeRepository.findByUserIdOrderByUploadDateDesc(userId);
        int version = existing.isEmpty() ? 1 : existing.get(0).getVersion() + 1;

        Resume resume = Resume.builder()
                .user(user)
                .fileName(file.getOriginalFilename())
                .s3Key(s3Key)
                .s3Url(s3Service.generatePresignedUrl(s3Key))  // initial URL
                .version(version)
                .isPrimary(existing.isEmpty())  // first upload = primary
                .build();

        return resumeRepository.save(resume);
    }

    public List<Resume> getMyResumes(Long userId) {
        return resumeRepository.findByUserIdOrderByUploadDateDesc(userId);
    }

    public String getDownloadUrl(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUser().getId().equals(userId))
            throw new UnauthorizedException("Access denied");
        // Regenerate — the stored URL may have already expired
        return s3Service.generatePresignedUrl(resume.getS3Key());
    }

    public void delete(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUser().getId().equals(userId))
            throw new UnauthorizedException("Access denied");
        s3Service.deleteFile(resume.getS3Key());
        resumeRepository.delete(resume);
    }
}
