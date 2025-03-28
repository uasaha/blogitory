package com.blogitory.blog.posts.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;
import static com.blogitory.blog.commons.utils.UrlUtil.getPostsKey;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.posts.dto.request.ModifyPostsRequestDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetBeforeNextPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetFeedPostsPagesResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostActivityResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
 * @since 1.0
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostsRestController {
  private final PostsService postsService;

  /**
   * Issue temp post key.
   *
   * @return temp post key
   */
  @RoleUser
  @GetMapping("/posts/key")
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
  @PostMapping("/posts/{tp}")
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
  @PostMapping("/posts")
  public ResponseEntity<CreatePostsResponseDto> createPosts(
          @RequestParam String tp, @RequestBody @Valid SaveTempPostsDto saveDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    CreatePostsResponseDto responseDto = postsService.createPosts(tp, memberNo, saveDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  /**
   * Modify posts.
   *
   * @param username   username
   * @param blogUrl    blog url
   * @param postUrl    post url
   * @param requestDto requests
   * @return 200
   */
  @RoleUser
  @PostMapping("/@{username}/{blogUrl}/{postUrl}")
  public ResponseEntity<Void> modifyPosts(@PathVariable String username,
                                          @PathVariable String blogUrl,
                                          @PathVariable String postUrl,
                                          @RequestBody @Valid ModifyPostsRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    postsService.modifyPosts(memberNo, postKey, requestDto);

    return ResponseEntity.ok().build();
  }

  /**
   * Delete posts.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @return 200
   */
  @RoleUser
  @DeleteMapping("/@{username}/{blogUrl}/{postUrl}")
  public ResponseEntity<Void> deletePosts(@PathVariable String username,
                                          @PathVariable String blogUrl,
                                          @PathVariable String postUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    postsService.deletePosts(memberNo, postKey);

    return ResponseEntity.ok().build();
  }

  /**
   * Delete temp post.
   *
   * @param tp temp post id
   * @return 200
   */
  @RoleUser
  @DeleteMapping("/posts")
  public ResponseEntity<Void> deleteTemp(@RequestParam String tp) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    postsService.deleteTempPosts(memberNo, tp);
    return ResponseEntity.ok().build();
  }

  /**
   * Get recent posts.
   *
   * @param pageable pageable
   * @return 200
   */
  @GetMapping("/posts/recent")
  public ResponseEntity<Pages<GetRecentPostResponseDto>> getRecentPosts(
          @PageableDefault(size = 12) Pageable pageable) {
    return ResponseEntity.ok(postsService.getRecentPost(pageable));
  }

  /**
   * Get recent posts by username.
   *
   * @param username username
   * @return 200
   */
  @GetMapping("/@{username}/posts/recent")
  public ResponseEntity<List<GetRecentPostResponseDto>> getRecentPostsByUsername(
          @PathVariable String username) {
    return ResponseEntity.ok(postsService.getRecentPostByUsername(username));
  }

  /**
   * Get recent posts by blog.
   *
   * @param username username
   * @param blogUrl  blog url
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/posts/recent")
  public ResponseEntity<Pages<GetRecentPostResponseDto>> getRecentPostsByBlog(
          @PathVariable String username,
          @PathVariable String blogUrl,
          @PageableDefault(size = 12) Pageable pageable) {
    String blogKey = getBlogKey(username, blogUrl);

    return ResponseEntity.ok(postsService.getRecentPostByBlog(pageable, blogKey));
  }

  /**
   * Get recent posts by category.
   *
   * @param username     username
   * @param blogUrl      blog url
   * @param categoryName category name
   * @param pageable     pageable
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/categories/{categoryName}")
  public ResponseEntity<Pages<GetRecentPostResponseDto>> getRecentPostsByCategory(
          @PathVariable String username,
          @PathVariable String blogUrl,
          @PathVariable String categoryName,
          @PageableDefault(size = 12) Pageable pageable) {
    String blogKey = getBlogKey(username, blogUrl);

    return ResponseEntity.ok(postsService.getRecentPostByCategory(pageable, blogKey, categoryName));
  }

  /**
   * Get recent posts by tag.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param tagName  tag name
   * @param pageable pageable
   * @return 200
   */
  @GetMapping("/@{username}/{blogUrl}/tags/{tagName}")
  public ResponseEntity<Pages<GetRecentPostResponseDto>> getRecentPostsByTag(
          @PathVariable String username,
          @PathVariable String blogUrl,
          @PathVariable String tagName,
          @PageableDefault(size = 12) Pageable pageable) {
    String blogKey = getBlogKey(username, blogUrl);

    return ResponseEntity.ok(postsService.getRecentPostByTag(pageable, blogKey, tagName));
  }

  /**
   * Close posts.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @return 200
   */
  @RoleUser
  @DeleteMapping("/@{username}/{blogUrl}/{postUrl}/visible")
  ResponseEntity<Void> closePosts(@PathVariable String username,
                                  @PathVariable String blogUrl,
                                  @PathVariable String postUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    postsService.closePosts(memberNo, postKey);

    return ResponseEntity.ok().build();
  }

  /**
   * Open posts.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @return 200
   */
  @RoleUser
  @PostMapping("/@{username}/{blogUrl}/{postUrl}/visible")
  ResponseEntity<Void> openPosts(@PathVariable String username,
                                 @PathVariable String blogUrl,
                                 @PathVariable String postUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    postsService.openPosts(memberNo, postKey);

    return ResponseEntity.ok().build();
  }

  /**
   * Get hearts posts.
   *
   * @param pageable pageable
   * @return posts
   */
  @RoleUser
  @GetMapping("/hearts")
  ResponseEntity<Pages<GetRecentPostResponseDto>> getHeartsPosts(
          @PageableDefault(size = 12) Pageable pageable) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    return ResponseEntity.ok(postsService.getPostsByHearts(memberNo, pageable));
  }

  /**
   * Get activity for chart.
   *
   * @param username username
   * @return 200
   */
  @GetMapping("/@{username}/activity")
  ResponseEntity<Map<String, List<GetPostActivityResponseDto>>> getActivityPosts(
          @PathVariable String username) {
    return ResponseEntity.ok(postsService.getPostActivity(username));
  }

  @GetMapping("/posts/search")
  ResponseEntity<Pages<GetRecentPostResponseDto>> search(
          @PageableDefault(size = 24) Pageable pageable,
          @RequestParam String q) {
    return ResponseEntity.ok(postsService.searchPosts(pageable, q));
  }

  @RoleUser
  @GetMapping("/feed")
  ResponseEntity<GetFeedPostsPagesResponseDto> feed(
          @PageableDefault(size = 12) Pageable pageable,
          @RequestParam(value = "s", required = false) Long start) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    return ResponseEntity.ok(postsService.feed(memberNo, start, pageable));
  }

  @GetMapping("/@{username}/{blogUrl}/{postUrl}/related")
  ResponseEntity<GetBeforeNextPostsResponseDto> getRelatedPosts(@PathVariable String username,
                                                                @PathVariable String blogUrl,
                                                                @PathVariable String postUrl) {
    String postsKey = getPostsKey(username, blogUrl, postUrl);
    return ResponseEntity.ok(postsService.getRelatedPosts(postsKey));
  }
}
