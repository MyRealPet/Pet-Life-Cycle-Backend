package com.example.petlifecycle.metadata.controller;

import com.example.petlifecycle.metadata.controller.request.FileUploadRequest;
import com.example.petlifecycle.metadata.controller.response.FileUploadResponse;
import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import com.example.petlifecycle.metadata.service.FileService;
import com.example.petlifecycle.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/pet/files")
public class FileController {

    private final FileService fileService;
    private final RedisCacheService redisCacheService;

    @PostMapping(value = "/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            /*@RequestHeader("Authorization") String authorizedHeader,*/
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileType fileType,
            @RequestParam(value = "accessType", defaultValue = "PRIVATE") AccessType accessType,
            @RequestParam(value = "relatedEntityType", required = false) String relatedEntityType,
            @RequestParam(value = "relatedEntityId", required = false) Long relatedEntityId) {

//        String userToken = authorizedHeader.replace("Bearer ", "");
//        Long accountId = redisCacheService.getValueByKey(userToken, Long.class);
        Long accountId = 1001L;

        try {
            MetaDataFile uploadedFile = fileService.uploadFile(
                    file, fileType, accountId, accessType, relatedEntityType, relatedEntityId);

            String fileUrl = fileService.getFileUrl(uploadedFile.getS3Key());

            FileUploadResponse response = FileUploadResponse.builder()
                    .fileId(uploadedFile.getId())
                    .originalFileName(uploadedFile.getOriginalFileName())
                    .fileUrl(fileUrl)
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
    // 다운
    // 조회

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(/*@RequestHeader("Authorization") String authorizedHeader,*/ @PathVariable Long fileId) {

//        String userToken = authorizedHeader.replace("Bearer ", "");
//        Long accountId = redisCacheService.getValueByKey(userToken, Long.class);
        Long accountId = 1001L;

        try {
            MetaDataFile file = fileService.getFileById(fileId);

            if (!file.getAccountId().equals(accountId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("파일을 삭제할 권한이 없습니다.");
            }
            fileService.deleteFile(fileId);
            return ResponseEntity.ok("파일이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("파일 삭제에 실패했습니다.");
        }
    }
}
