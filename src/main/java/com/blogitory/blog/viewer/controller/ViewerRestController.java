package com.blogitory.blog.viewer.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getPostsKey;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.viewer.dto.GetViewerCountResponseDto;
import com.blogitory.blog.viewer.service.ViewerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Viewer rest controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ViewerRestController {
  private final ViewerService viewerService;

  /**
   * Get monthly viewer count.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @return monthly viewer count
   */
  @RoleUser
  @GetMapping("/@{username}/{blogUrl}/{postUrl}/viewers/statistic")
  public ResponseEntity<List<GetViewerCountResponseDto>> getMonthlyViewerCounts(
          @PathVariable String username,
          @PathVariable String blogUrl,
          @PathVariable String postUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    return ResponseEntity.ok(viewerService.getViewerMonthlyCount(memberNo, postKey));
  }
}
