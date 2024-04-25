package com.blogitory.blog.main;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.HashMap;
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
 **/
@WebMvcTest(value = {IndexController.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class IndexControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  private MemberService memberService;

  @BeforeEach
  void setUp() {

  }

  @Test
  @DisplayName("인덱스 페이지")
  void indexPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  @DisplayName("회원가입 페이지")
  void signupPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/signup"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  @DisplayName("설정 페이지")
  @WithMockUser("1")
  void settings() throws Exception {
    MemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    mvc.perform(MockMvcRequestBuilders.get("/settings")
                    .flashAttr("members", persistInfoDto))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("profile")))
            .andDo(print());
  }

  @Test
  @DisplayName("프로필 설정 페이지")
  @WithMockUser("1")
  void profileSettings() throws Exception {
    MemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    MemberMyProfileResponseDto responseDto = new MemberMyProfileResponseDto(
            "test@email.com",
            "name",
            "profileThumb",
            "introEmail",
            "github",
            "twitter",
            "facebook",
            "homepage",
            LocalDateTime.now());

    when(memberService.myProfile(any())).thenReturn(responseDto);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);

    mvc.perform(MockMvcRequestBuilders.get("/settings/profile")
                    .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("profile")))
            .andDo(print());
  }
}