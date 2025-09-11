package com.example.petlifecycle.metadata.controller.request;

import com.example.petlifecycle.metadata.entity.AccessType;
import com.example.petlifecycle.metadata.entity.FileType;
import com.example.petlifecycle.metadata.entity.MetaDataFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadRequest {
    private MultipartFile file;
    private FileType fileType;
    private AccessType accessType;
//    private Long uploadId;
    private String relatedEntityType;
    private Long relatedEntityId;

    public FileUploadRequest(MultipartFile file, FileType fileType, AccessType accessType, String relatedEntityType, Long relatedEntityId) {
        this.file = file;
        this.fileType = fileType;
        this.accessType = accessType;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
    }

    public MetaDataFile toMetaDataFile(String storedFileName, String s3Key) {
        return new MetaDataFile(
                file.getOriginalFilename(),
                storedFileName,
                s3Key,
                file.getContentType(),
                file.getSize(),
                fileType,
                accessType,
                relatedEntityType,
                relatedEntityId
        );
    }
}
