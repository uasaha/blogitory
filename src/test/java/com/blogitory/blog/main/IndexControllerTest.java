package com.blogitory.blog.main;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberAlertInSettingsResponseDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileInSettingsResponseDto;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.posts.service.PostsService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Index Controller Test.
 *
 * @author woonseok
 * @since 1.0
 */
@WebMvcTest(value = {IndexController.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class IndexControllerTest {

  /**
   * The Mvc.
   */
  @Autowired
  MockMvc mvc;

  @MockBean
  private MemberService memberService;

  @MockBean
  private BlogService blogService;

  @MockBean
  private PostsService postsService;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {

  }

  /**
   * Index page.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("인덱스 페이지")
  void indexPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  /**
   * Signup page.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("회원가입 페이지")
  void signupPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/signup"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  /**
   * Blog settings.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("블로그 설정 페이지")
  @WithMockUser("1")
  void blogSettings() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    GetBlogInSettingsResponseDto responseDto = new GetBlogInSettingsResponseDto(
            "name",
            "bio",
            "url",
            LocalDateTime.of(2000, 2, 2, 2, 2, 2),
            "",
            "",
            List.of());

    List<GetBlogInSettingsResponseDto> responseDtoList = List.of(responseDto);

    when(blogService.getBlogListByMemberNo(anyInt()))
            .thenReturn(responseDtoList);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mvc.perform(MockMvcRequestBuilders.get("/settings/blog")
            .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("name")));
  }

  /**
   * Blog settings no blog.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("블로그 설정 페이지 - 블로그 없음")
  @WithMockUser("1")
  void blogSettingsNoBlog() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    List<GetBlogInSettingsResponseDto> responseDtoList = List.of();

    when(blogService.getBlogListByMemberNo(anyInt()))
            .thenReturn(responseDtoList);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", true);

    mvc.perform(MockMvcRequestBuilders.get("/settings/blog")
                    .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("name")));
  }

  @Test
  @DisplayName("셔플 페이지")
  void shufflePage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/shuffle"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  @DisplayName("피드 페이지")
  void feedPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/feed"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("설정 페이지")
  void settingsPage() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    GetMemberProfileInSettingsResponseDto responseDto =
            new GetMemberProfileInSettingsResponseDto(
                    "username",
                    "name",
                    "pfp",
                    "email",
                    "bio",
                    "intro",
                    List.of());
    when(memberService.getSettingsProfile(any())).thenReturn(responseDto);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mvc.perform(MockMvcRequestBuilders.get("/settings")
            .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("name")));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("알림 설정 페이지")
  void notificationPage() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    GetMemberAlertInSettingsResponseDto responseDto =
            new GetMemberAlertInSettingsResponseDto(true,
                    true,
                    true,
                    true);

    when(memberService.getSettingsAlert(any())).thenReturn(responseDto);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mvc.perform(MockMvcRequestBuilders.get("/settings/notification")
                    .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("name")));
  }
}