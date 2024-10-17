package com.blogitory.blog.notice.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Notice controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
@Controller
public class NoticeController {

  /**
   * Go to notice page.
   *
   * @return page
   */
  @RoleUser
  @GetMapping("/notifications")
  public String notifications() {
    return "index/main/notifications";
  }
}
