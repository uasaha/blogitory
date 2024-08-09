package com.blogitory.blog.blog.controller;

import com.blogitory.blog.blog.dto.request.CreateBlogRequestDto;
import com.blogitory.blog.blog.dto.request.UpdateBlogRequestDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.image.dto.UpdateThumbnailResponseDto;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.member.dto.request.LeftMemberRequestDto;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Blog Rest Controller.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
public class BlogRestController {
  private final BlogService blogService;
  private final ImageService imageService;

  /**
   * Create Blog.
   *
   * @param requestDto reqyestDto
   * @return 201
   */
  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CreateBlogRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    blogService.createBlog(requestDto, memberNo);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Modify Blog info.
   *
   * @param url        blog url
   * @param requestDto modify info
   * @return 204
   */
  @PutMapping
  public ResponseEntity<Void> update(@RequestParam String url,
                                     @Valid @RequestBody UpdateBlogRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    blogService.updateBlog(url, requestDto, memberNo);

    return ResponseEntity.noContent().build();
  }

  /**
   * Modify blog thumbnail picture.
   *
   * @param file new picture
   * @param url  Blog url
   * @return 200
   */
  @PostMapping("/thumbs")
  public ResponseEntity<UpdateThumbnailResponseDto> updateBlogThumb(
          @RequestParam("file") MultipartFile file,
          @RequestParam String url) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    UpdateThumbnailResponseDto responseDto = imageService.updateBlogThumbnail(memberNo, url, file);

    return ResponseEntity.ok(responseDto);
  }

  /**
   * Delete blog thumbnail picture.
   *
   * @param url Blog url
   * @return 204
   */
  @DeleteMapping("/thumbs")
  public ResponseEntity<Void> deleteBlogThumb(@RequestParam String url) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    imageService.removeBlogThumbnail(memberNo, url);

    return ResponseEntity.noContent().build();
  }

  /**
   * Delete blog.
   *
   * @param url        Blog url
   * @param requestDto password
   * @return 204
   */
  @PostMapping("/quit")
  public ResponseEntity<Void> deleteBlog(@RequestParam String url,
                                         @RequestBody LeftMemberRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    blogService.quitBlog(memberNo, url, requestDto.getPassword());

    return ResponseEntity.noContent().build();
  }
}
