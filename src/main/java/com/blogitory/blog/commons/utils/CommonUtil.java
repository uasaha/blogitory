package com.blogitory.blog.commons.utils;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Util class for Commons.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
public class CommonUtil {

  /**
   * Random bean.
   *
   * @return Random instance
   */
  @Bean
  public Random random() {
    return new Random();
  }
}
