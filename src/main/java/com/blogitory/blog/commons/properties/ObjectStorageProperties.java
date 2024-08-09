package com.blogitory.blog.commons.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Properties for Object storage.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Configuration
public class ObjectStorageProperties {
  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKey;
  @Value("${cloud.aws.credentials.secretKey}")
  private String privateKey;
  @Value("${cloud.aws.region.static}")
  private String region;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  @Value("${cloud.aws.s3.return-url}")
  private String returnUrl;
  @Value("${cloud.aws.cdn.cname}")
  private String cdnCname;
}
