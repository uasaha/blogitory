package com.blogitory.blog.commons.config;

import com.blogitory.blog.commons.properties.ObjectStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configuration for using AWS S3 Object storage.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class ObjectStorageConfig {
  private final ObjectStorageProperties properties;

  /**
   * Register S3Client Bean.
   *
   * @return S3Client instance
   */
  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
            .credentialsProvider(this::awsCredentials)
            .region(Region.of(properties.getRegion()))
            .build();
  }

  /**
   * Create AwsCredentials.
   *
   * @return AwsCredentials
   */
  private AwsCredentials awsCredentials() {
    return AwsBasicCredentials.create(
            properties.getAccessKey(),
            properties.getPrivateKey());
  }
}
