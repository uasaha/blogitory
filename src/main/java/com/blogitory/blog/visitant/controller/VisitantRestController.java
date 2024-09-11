package com.blogitory.blog.visitant.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;

import com.blogitory.blog.visitant.service.VisitantService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Visitant rest controller.
 *
 * @author woonseok
 * @Date 2024-09-10
 * @since 1.0
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class VisitantRestController {
  private final VisitantService visitantService;

  /**
   * Get visitant, total & today.
   *
   * @param username username
   * @param blogUrl  blog url
   * @return visitant count
   */
  @GetMapping("/@{username}/{blogUrl}/visitants")
  public ResponseEntity<Map<String, Integer>> getVisitants(@PathVariable String username,
                                                           @PathVariable String blogUrl) {
    String blogKey = getBlogKey(username, blogUrl);

    return ResponseEntity.ok(visitantService.getVisitantCount(blogKey));
  }
}
