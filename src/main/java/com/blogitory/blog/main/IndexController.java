package com.blogitory.blog.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for index page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
public class IndexController {
  /**
   * Go to main page.
   *
   * @return main page
   */
  @GetMapping("/")
  public String indexPage() {
    return "index/index";
  }

  /**
   * Go to signup page.
   *
   * @return signup page
   */
  @GetMapping("/signup")
  public String signupPage() {
    return "index/signup";
  }
}
