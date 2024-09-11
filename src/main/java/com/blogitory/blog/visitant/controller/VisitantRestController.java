package com.blogitory.blog.visitant.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;

import com.blogitory.blog.commons.annotaion.RoleAdmin;
import com.blogitory.blog.visitant.service.VisitantService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

  /**
   * Save and delete visitant now.
   *
   * @return 200 ok
   * @throws JsonProcessingException object mapper
   */
  @RoleAdmin
  @GetMapping("/admin/visitants/now")
  public ResponseEntity<Void> nowSaveAndDelete() throws JsonProcessingException {
    visitantService.saveAndDelete();

    return ResponseEntity.ok().build();
  }
}
