package com.blogitory.blog.follow.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Follow rest controller.
 *
 * @author woonseok
 * @Date 2024-08-26
 * @since 1.0
 **/
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowRestController {
  private final FollowService followService;

  /**
   * Do follow.
   *
   * @param username target username
   * @return 201
   */
  @RoleUser
  @PostMapping("/@{username}")
  public ResponseEntity<Void> follow(@PathVariable String username) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    followService.follow(memberNo, username);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Do unfollow.
   *
   * @param username target username
   * @return 204
   */
  @RoleUser
  @DeleteMapping("/@{username}")
  public ResponseEntity<Void> unfollow(@PathVariable String username) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    followService.unFollow(memberNo, username);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
