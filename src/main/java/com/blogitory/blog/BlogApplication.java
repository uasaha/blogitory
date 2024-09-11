package com.blogitory.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Blogitory start.
 *
 * @author woonseok
 * @since 1.0
 */
@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
public class BlogApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlogApplication.class, args);
  }

}
