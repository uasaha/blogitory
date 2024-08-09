package com.blogitory.blog.storage.service.impl;

import com.blogitory.blog.commons.properties.ObjectStorageProperties;
import com.blogitory.blog.storage.dto.UploadFileResponseDto;
import com.blogitory.blog.storage.exception.FileUploadException;
import com.blogitory.blog.storage.exception.NoOriginalFileNameException;
import com.blogitory.blog.storage.service.ObjectStorageService;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
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
  public UploadFileResponseDto uploadFile(String type, MultipartFile file) {
    if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
      return null;
    }

    String originalFileName;

    if (file.getOriginalFilename() == null) {
      throw new NoOriginalFileNameException();
    }

    originalFileName = file.getOriginalFilename();
    int imageIdx = originalFileName.lastIndexOf(".");
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

    return UploadFileResponseDto.builder()
            .url(objectStorageProperties.getCdnCname() + keyPrefix + fileName)
            .originName(originalFileName)
            .savedName(fileName)
            .extension(fileEx)
            .savePath(type)
            .build();
  }

  @Override
  public void deleteFile(String type, String filename) {
    String key = type + "/" + filename;

    DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(objectStorageProperties.getBucket())
            .key(key)
            .build();

    s3Client.deleteObject(request);
  }
}
