package com.example.petlifecycle.metadata.controller.response;

import com.example.petlifecycle.metadata.entity.MetaDataFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FileUploadResponse {
    private final Long fileId;
    private final String originalFileName;
    private final Long fileSize;
    private final String contentType;
    private final String message;

    public static FileUploadResponse from(MetaDataFile file, String message) {
        return new FileUploadResponse(
                file.getId(),
                file.getOriginalFileName(),
                file.getFileSize(),
                file.getContentType(),
                message
        );
    }

//    public static FileUploadResponse from(MetaDataFile file, String message) {
//        return FileUploadResponse.builder()
//                .fileId(file.getId())
//                .originalFileName(file.getOriginalFileName())
//                .fileSize(file.getFileSize())
//                .contentType(file.getContentType())
//                .message(message)
//                .build();
//    }
}
