package com.example.petlifecycle.metadata.service;

import com.example.petlifecycle.metadata.controller.request.FileUploadRequest;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.repository.MetaDataFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FileService {

    private final S3Service s3Service;
    private final MetaDataFileRepository metaDataFileRepository;

    public MetaDataFile uploadFile(MultipartFile file, FileType fileType,
                                   AccessType accessType, String relatedEntityType, Long relatedEntityId) {

        // 파일 유효성 검증
        validateFile(file, fileType);

        // 저장할 파일명 생성 (중복 방지)
        String storedFileName = generateStoredFileName(file.getOriginalFilename());

        // S3 키 생성 (디렉토리 구조)
        String s3Key = generateS3Key(fileType, storedFileName);

        try {
            // S3에 파일 업로드
            String fileUrl = s3Service.uploadFile(file, s3Key);

            // 메타데이터 저장
            MetaDataFile metaDataFile = MetaDataFile.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedFileName)
                    .s3Key(s3Key)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileType(fileType)
                    .accessType(accessType)
                    .relatedEntityType(relatedEntityType)
                    .relatedEntityId(relatedEntityId)
                    .build();

            return metaDataFileRepository.save(metaDataFile);
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}" ,e.getMessage());
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.");
        }

    }

    private void validateFile(MultipartFile file, FileType fileType) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        if(file.getSize() > fileType.getMaxFileSize()) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }
        if(!fileType.getAllowedContentTypes().contains(file.getContentType())) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        }
    }

    private String generateStoredFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

    private String generateS3Key(FileType fileType, String storedFileName) {
        String prefix = switch(fileType) {
            case PROFILE_IMAGE -> "profiles/";
            case REGISTRATION_PDF -> "registrations/";
            case MEDICAL_DOCUMENT -> "medical/";
        };

        return prefix + storedFileName;
    }
}
