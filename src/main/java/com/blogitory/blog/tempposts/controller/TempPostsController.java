package com.blogitory.blog.tempposts.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.service.TempPostsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Temp posts controller.
 *
 * @author woonseok
 * @Date 2024-08-05
 * @since 1.0
 **/
@RequiredArgsConstructor
@Controller
public class TempPostsController {
  private final TempPostsService tempPostsService;

  /**
   * Go to tamp posts page.
   *
   * @param model model
   * @return page
   */
  @RoleUser
  @GetMapping("/tp")
  public String tempPostsPage(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    List<GetTempPostsResponseDto> responseDto =
            tempPostsService.getTempPostsListByMemberNo(memberNo);

    model.addAttribute("tempPosts", responseDto);

    return "index/main/tp";
  }
}
