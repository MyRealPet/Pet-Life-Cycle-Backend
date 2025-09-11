package com.example.petlifecycle.metadata.controller;

import com.example.petlifecycle.metadata.controller.request.FileUploadRequest;
import com.example.petlifecycle.metadata.controller.response.FileUploadResponse;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileType fileType,
            @RequestParam(value = "accessType", defaultValue = "PRIVATE") AccessType accessType,
            @RequestParam(value = "relatedEntityType", required = false) String relatedEntityType,
            @RequestParam(value = "relatedEntityId", required = false) Long relatedEntityId) {

        try {
            MetaDataFile uploadedFile = fileService.uploadFile(
                    file, fileType, accessType, relatedEntityType, relatedEntityId);

            FileUploadResponse response = FileUploadResponse.builder()
                    .fileId(uploadedFile.getId())
                    .originalFileName(uploadedFile.getOriginalFileName())
                    .fileSize(uploadedFile.getFileSize())
                    .contentType(uploadedFile.getContentType())
                    .message("파일이 성공적으로 업로드되었습니다.")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(FileUploadResponse.builder()
                            .message("파일 업로드에 실패했습니다. ")
                            .build());
        }
    }
}
