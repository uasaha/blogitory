package com.blogitory.blog.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @Date 2024-08-12
 * @since 1.0
 **/
@RestController
public class TestController {

  @Value("${server.port}")
  private String port;

  @Value("${spring.profiles.active}")
  private String profile;

  @GetMapping("/deploy/port")
  public String deployPort() {
    return "Profile: " + profile + ", Port: " + port;
  }
}
