package com.blogitory.blog.main;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.utils.IoUtils;

/**
 * Index rest controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
public class IndexRestController {

  /**
   * Robots.txt
   *
   * @return robots.txt
   * @throws IOException ioe
   */
  @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
  public byte[] getRobots() throws IOException {
    InputStream txt = getClass().getResourceAsStream("/static/bot/robots.txt");

    if (txt == null) {
      return new byte[0];
    }

    return IoUtils.toByteArray(txt);
  }
}
