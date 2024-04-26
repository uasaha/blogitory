package com.blogitory.blog.storage.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.properties.ObjectStorageProperties;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.exception.FileUploadException;
import com.blogitory.blog.storage.service.impl.ObjectStorageServiceImpl;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * ObjectStorage service test.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
class ObjectStorageServiceTest {
  S3Client s3Client;
  ObjectStorageProperties properties;
  ObjectStorageService objectStorageService;

  @BeforeEach
  void setup() {
    s3Client = mock(S3Client.class);
    properties = mock(ObjectStorageProperties.class);
    objectStorageService = new ObjectStorageServiceImpl(s3Client, properties);
  }

  @Test
  @DisplayName("파일업로드 성공")
  void uploadFile() {
    String type = "type";
    MultipartFile file = new MockMultipartFile("filename.jpg", "filename.jpg", "contentType", "content".getBytes(StandardCharsets.UTF_8));
    String bucket = "bucket";
    String returnUrl = "returnUrl";

    when(properties.getBucket()).thenReturn(bucket);
    when(s3Client.putObject((PutObjectRequest) any(), (RequestBody) any())).thenReturn(null);
    when(properties.getReturnUrl()).thenReturn(returnUrl);

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(type, file);

    assertAll(
            () -> assertTrue(responseDto.getUrl().contains(returnUrl+type+"/")),
            () -> assertEquals("filename.jpg", responseDto.getOriginName()),
            () -> assertTrue(responseDto.getSavedName().contains(".jpg")),
            () -> assertEquals(".jpg", responseDto.getExtension()),
            () -> assertEquals(type, responseDto.getSavePath())
    );
  }

  @Test
  @DisplayName("파일 업로드 실패 - file.isEmpty")
  void uploadFileFailedIsEmpty() {
    MultipartFile file = mock(MockMultipartFile.class);
    String type = "type";

    when(file.isEmpty()).thenReturn(true);

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(type, file);

    assertNull(responseDto);
  }

  @Test
  @DisplayName("파일 업로드 실패 - originName is null")
  void uploadFileFailedNameIsNull() {
    MultipartFile file = mock(MockMultipartFile.class);
    String type = "type";

    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn(null);

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(type, file);

    assertNull(responseDto);
  }

  @Test
  @DisplayName("파일업로드 실패 - IOException")
  void uploadFileFailedIOException() throws IOException {
    String type = "type";
    MultipartFile file = mock(MockMultipartFile.class);

    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("filename.jpg");
    when(file.getBytes()).thenThrow(IOException.class);

    assertThrows(FileUploadException.class,
            () -> objectStorageService.uploadFile(type, file));
  }
}