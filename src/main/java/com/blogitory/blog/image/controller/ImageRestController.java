package com.blogitory.blog.image.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image Rest Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageRestController {
  private final ImageService imageService;

  /**
   * Update User's Thumbnail Image.
   *
   * @param file new one
   * @return name & url
   */
  @RoleUser
  @PostMapping("/thumbnail")
  public ResponseEntity<ThumbnailUpdateResponseDto> updateThumbnail(
          @RequestParam("file") MultipartFile file) {
    return ResponseEntity.ok(imageService.uploadThumbnail(SecurityUtils.getCurrentUserNo(), file));
  }

  /**
   * Delete thumbnail image.
   *
   * @return 200
   */
  @RoleUser
  @DeleteMapping("/thumbnail")
  public ResponseEntity<Void> deleteThumbnail() {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    imageService.removeThumbnail(memberNo);

    return ResponseEntity.ok().build();
  }


}
