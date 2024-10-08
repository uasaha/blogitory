package com.blogitory.blog.comment.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;
import static com.blogitory.blog.commons.utils.UrlUtil.getPostsKey;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.request.ModifyCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetLatestCommentListResponseDto;
import com.blogitory.blog.comment.service.CommentService;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comment Rest Controller.
 *
 * @author woonseok
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
  @GetMapping("/@{username}/{blogUrl}/{postsUrl}/comments")
  public ResponseEntity<Pages<GetCommentResponseDto>> getComments(
          @PathVariable("username") String username,
          @PathVariable("blogUrl") String blogUrl,
          @PathVariable("postsUrl") String postsUrl,
          @PageableDefault Pageable pageable) {
    String postsKey = getPostsKey(username, blogUrl, postsUrl);
    Pages<GetCommentResponseDto> comments = commentService.getComments(postsKey, pageable);

    return ResponseEntity.ok(comments);
  }

  /**
   * Get count of comment by post.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl posts url
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/{postsUrl}/comments/count")
  public ResponseEntity<Long> countComments(@PathVariable("username") String username,
                                            @PathVariable("blogUrl") String blogUrl,
                                            @PathVariable("postsUrl") String postsUrl) {
    String postsKey = getPostsKey(username, blogUrl, postsUrl);

    return ResponseEntity.ok(commentService.getCommentCountByPost(postsKey));
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
  @GetMapping("/@{username}/{blogUrl}/{postsUrl}/comments/{commentNo}/child")
  public ResponseEntity<Pages<GetChildCommentResponseDto>> getChildComments(
          @PathVariable("username") String username,
          @PathVariable("blogUrl") String blogUrl,
          @PathVariable("postsUrl") String postsUrl,
          @PathVariable("commentNo") Long commentNo,
          @PageableDefault Pageable pageable) {
    String postsKey = getPostsKey(username, blogUrl, postsUrl);
    Pages<GetChildCommentResponseDto> comments =
            commentService.getChildComments(postsKey, commentNo, pageable);

    return ResponseEntity.ok(comments);
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

  /**
   * Modify comment.
   *
   * @param commentNo  comment no
   * @param requestDto new contents
   * @return 200 OK
   */
  @RoleUser
  @PutMapping("/comments/{commentNo}")
  public ResponseEntity<Void> modifyComment(
          @PathVariable("commentNo") Long commentNo,
          @RequestBody @Valid ModifyCommentRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    commentService.modifyComment(memberNo, commentNo, requestDto.getContents());

    return ResponseEntity.ok().build();
  }

  /**
   * Delete comment.
   *
   * @param commentNo comment no
   * @return 200 OK
   */
  @RoleUser
  @DeleteMapping("/comments/{commentNo}")
  public ResponseEntity<Void> deleteComment(@PathVariable("commentNo") Long commentNo) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    commentService.deleteComment(memberNo, commentNo);

    return ResponseEntity.ok().build();
  }

  /**
   * Get recent comments. (limit 4)
   *
   * @param username username
   * @param blogUrl  blog
   * @return comments
   */
  @GetMapping("/@{username}/{blogUrl}/comments/recent")
  public ResponseEntity<List<GetLatestCommentListResponseDto>> getRecentComments(
          @PathVariable("username") String username,
          @PathVariable("blogUrl") String blogUrl) {
    String blogKey = getBlogKey(username, blogUrl);

    return ResponseEntity.ok(commentService.getRecentComments(username, blogKey));
  }
}
