package com.blogitory.blog.comment.controller;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetCommentListResponseDto;
import com.blogitory.blog.comment.service.CommentService;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comment Rest Controller.
 *
 * @author woonseok
 * @Date 2024-08-07
 * @since 1.0
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentRestController {
  private final CommentService commentService;

  /**
   * Get Comments By Posts.
   *
   * @param username username
   * @param blogUrl  blogUrl
   * @param postsUrl postsUrl
   * @param pageable pageable
   * @return comment list
   */
  @GetMapping("/@{username}/{blogUrl}/posts/{postsUrl}/comments")
  public ResponseEntity<List<GetCommentListResponseDto>> getComments(
          @PathVariable("username") String username,
          @PathVariable("blogUrl") String blogUrl,
          @PathVariable("postsUrl") String postsUrl,
          @PageableDefault Pageable pageable) {
    return ResponseEntity.ok().build();
  }

  /**
   * Get Child comments.
   *
   * @param username  username
   * @param blogUrl   blogUrl
   * @param postsUrl  postsUrl
   * @param commentNo parent comment no
   * @return child comment list
   */
  @GetMapping("/@{username}/{blogUrl}/posts/{postsUrl}/comments/{commentNo}/childs")
  public ResponseEntity<List<GetCommentListResponseDto>> getChildComments(
          @PathVariable("username") String username,
          @PathVariable("blogUrl") String blogUrl,
          @PathVariable("postsUrl") String postsUrl,
          @PathVariable("commentNo") Long commentNo) {
    return ResponseEntity.ok().build();
  }

  /**
   * Create comment.
   *
   * @param requestDto requestDto
   * @param parentNo   parent comment no
   * @return 204
   */
  @RoleUser
  @PostMapping("/comments")
  public ResponseEntity<Void> createComment(
          @RequestBody @Valid CreateCommentRequestDto requestDto,
          @RequestParam(required = false, name = "pn") Long parentNo) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    commentService.createComment(memberNo, parentNo, requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
