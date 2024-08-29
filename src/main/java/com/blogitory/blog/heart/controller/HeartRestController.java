package com.blogitory.blog.heart.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.commons.utils.UrlUtil;
import com.blogitory.blog.heart.service.HeartService;
import com.blogitory.blog.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Heart rest controller.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HeartRestController {
  private final HeartService heartService;

  /**
   * Checking heart.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl post url
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/{postsUrl}/heart")
  public ResponseEntity<Boolean> checkHeart(@PathVariable String username,
                                            @PathVariable String blogUrl,
                                            @PathVariable String postsUrl) {
    if (!SecurityUtils.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postsKey = UrlUtil.getPostsKey(username, blogUrl, postsUrl);

    return ResponseEntity.ok(heartService.existHeart(memberNo, postsKey));
  }

  /**
   * Counts heart.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl post url
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/{postsUrl}/hearts/count")
  public ResponseEntity<Long> countHearts(@PathVariable String username,
                                          @PathVariable String blogUrl,
                                          @PathVariable String postsUrl) {
    String postsKey = UrlUtil.getPostsKey(username, blogUrl, postsUrl);

    return ResponseEntity.ok(heartService.countHeart(postsKey));
  }

  /**
   * Create heart.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl post url
   * @return 201
   */
  @RoleUser
  @PostMapping("/@{username}/{blogUrl}/{postsUrl}/heart")
  public ResponseEntity<Void> doHeart(@PathVariable String username,
                                      @PathVariable String blogUrl,
                                      @PathVariable String postsUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postsKey = UrlUtil.getPostsKey(username, blogUrl, postsUrl);

    heartService.heart(memberNo, postsKey);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Cancel heart.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl posts url
   * @return 204
   */
  @RoleUser
  @DeleteMapping("/@{username}/{blogUrl}/{postsUrl}/heart")
  public ResponseEntity<Void> deleteHeart(@PathVariable String username,
                                          @PathVariable String blogUrl,
                                          @PathVariable String postsUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postsKey = UrlUtil.getPostsKey(username, blogUrl, postsUrl);

    heartService.deleteHeart(memberNo, postsKey);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
