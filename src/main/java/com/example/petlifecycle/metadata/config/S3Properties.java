package com.example.petlifecycle.metadata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
@Data
public class S3Properties {
    private String bucket;
    private String region = "ap-southeast-2";
    private String baseUrl;

}
