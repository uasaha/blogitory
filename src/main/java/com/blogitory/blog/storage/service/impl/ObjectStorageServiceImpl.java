package com.blogitory.blog.storage.service.impl;

import com.blogitory.blog.commons.properties.ObjectStorageProperties;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.exception.FileUploadException;
import com.blogitory.blog.storage.service.ObjectStorageService;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Implementation of ObjectStorageService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
public class ObjectStorageServiceImpl implements ObjectStorageService {
  private final S3Client s3Client;
  private final ObjectStorageProperties objectStorageProperties;
  private static final String CONTENT_TYPE = "multipart/formed-data";

  /**
   * {@inheritDoc}
   */
  @Override
  public FileUploadResponseDto uploadFile(String type, MultipartFile file) {
    if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
      return null;
    }

    String originalFileName = file.getOriginalFilename();
    Integer imageIdx = originalFileName.lastIndexOf(".");
    String fileEx = originalFileName.substring(imageIdx);
    String fileName = UUID.randomUUID() + fileEx;
    String keyPrefix = type + "/";

    try {
      PutObjectRequest request = PutObjectRequest.builder()
              .bucket(objectStorageProperties.getBucket())
              .contentType(CONTENT_TYPE)
              .contentLength(file.getSize())
              .key(keyPrefix + fileName)
              .tagging(type)
              .build();

      RequestBody body = RequestBody.fromBytes(file.getBytes());

      s3Client.putObject(request, body);
    } catch (IOException e) {
      throw new FileUploadException();
    }

    return FileUploadResponseDto.builder()
            .url(objectStorageProperties.getReturnUrl() + keyPrefix + fileName)
            .originName(originalFileName)
            .savedName(fileName)
            .extension(fileEx)
            .savePath(type)
            .build();
  }
}
