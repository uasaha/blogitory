package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.properties.ObjectStorageProperties;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * ObjectStorageConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class ObjectStorageConfigTest {

  @Test
  void s3Client() {
    ObjectStorageProperties properties = mock(ObjectStorageProperties.class);
    ObjectStorageConfig config = new ObjectStorageConfig(properties);

    when(properties.getRegion()).thenReturn("us-east-1");
    when(properties.getAccessKey()).thenReturn("access-key");
    when(properties.getPrivateKey()).thenReturn("private-key");

    S3Client client = config.s3Client();

    assertInstanceOf(S3Client.class, client);
  }
}