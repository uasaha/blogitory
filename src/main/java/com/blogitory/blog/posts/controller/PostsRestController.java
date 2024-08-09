package com.blogitory.blog.posts.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Posts rest controller.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsRestController {
  private final PostsService postsService;

  /**
   * Issue temp post key.
   *
   * @return temp post key
   */
  @RoleUser
  @GetMapping("/key")
  public ResponseEntity<String> key() {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    return ResponseEntity.ok(postsService.getTempPostsId(memberNo));
  }

  /**
   * Save temp post.
   *
   * @param tp      temp post id
   * @param saveDto request
   * @return 200
   */
  @RoleUser
  @PostMapping("/{tp}")
  public ResponseEntity<Void> tempSave(@PathVariable String tp,
                                       @RequestBody SaveTempPostsDto saveDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    postsService.saveTempPosts(tp, saveDto, memberNo);

    return ResponseEntity.ok().build();
  }

  /**
   * Create posts.
   *
   * @param tp      temp post id
   * @param saveDto request
   * @return 204
   */
  @RoleUser
  @PostMapping
  public ResponseEntity<CreatePostsResponseDto> createPosts(
          @RequestParam String tp, @RequestBody @Valid SaveTempPostsDto saveDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    CreatePostsResponseDto responseDto = postsService.createPosts(tp, memberNo, saveDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  /**
   * Delete temp post.
   *
   * @param tp temp post id
   * @return 200
   */
  @RoleUser
  @DeleteMapping
  public ResponseEntity<Void> deleteTemp(@RequestParam String tp) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    postsService.deleteTempPosts(memberNo, tp);
    return ResponseEntity.ok().build();
  }
}
