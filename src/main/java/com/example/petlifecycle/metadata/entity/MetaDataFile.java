package com.example.petlifecycle.metadata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
//@Table(name = "metadata_files")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String originalFileName;
    // 중복방지
    @Column(nullable = false, length = 100)
    private String storedFileName;
    // 경로
    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false, length = 100)
    private String contentType;
    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FileType fileType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccessType accessType;

//    @Column(nullable = false)
//    private Long uploaderId;
    @Column(length = 50)
    private String relatedEntityType;
    private Long relatedEntityId;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public MetaDataFile(String originalFileName, String storedFileName, String s3Key, String contentType, Long fileSize, FileType fileType, AccessType accessType, String relatedEntityType, Long relatedEntityId) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.s3Key = s3Key;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.accessType = accessType;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
