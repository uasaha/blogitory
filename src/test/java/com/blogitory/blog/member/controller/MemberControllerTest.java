package com.blogitory.blog.member.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogInProfileResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.follow.dto.response.GetAllFollowResponseDto;
import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileLinkResponseDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileResponseDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.exception.MemberPwdChangeFailedException;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Member Controller Test.
 */
@WebMvcTest(value = {MemberController.class, TestSecurityConfig.class})
class MemberControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MemberService memberService;

  @MockBean
  BlogService blogService;

  @MockBean
  FollowService followService;

  @Test
  @DisplayName("회원가입 페이지")
  void signup() throws Exception {
    Member member = MemberDummy.dummy();

    doNothing().when(memberService).signup(any());

    mvc.perform(post("/signup").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", member.getUsername())
            .param("name", member.getName())
            .param("email", member.getEmail())
            .param("pwd", "@poilklj1234"))
            .andExpect(status().is3xxRedirection());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("프로필 페이지")
  void profile() throws Exception {
    GetMemberProfileResponseDto profileDto =
            new GetMemberProfileResponseDto(
                    "username",
                    "name",
                    "bio",
                    "profileThumb",
                    "introEmail",
                    List.of(new GetMemberProfileLinkResponseDto(1L, "link")),
                    List.of(new GetBlogInProfileResponseDto("url", "blog", "bio")),
                    1L,
                    1L);

    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);

    when(memberService.getProfileByUsername(anyString())).thenReturn(profileDto);
    when(followService.isFollowed(anyInt(), anyString())).thenReturn(true);

    mvc.perform(get("/@" + profileDto.getUsername()).with(csrf())
                    .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("isFollowed"));
  }

  @Test
  @DisplayName("비밀번호 수정 페이지")
  void updatePasswordPage() throws Exception {
    mvc.perform(get("/users/passwords")
                    .param("ui", "ui"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  @DisplayName("비밀번호 수정")
  void updatePassword() throws Exception {
    doNothing().when(memberService).updatePassword(any());

    mvc.perform(post("/users/passwords")
                    .param("ui", "ui")
                    .param("pwd", "@Test12345"))
            .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("비밀번호 수정 성공")
  void updatePasswordSuccessPage() throws Exception {
    mvc.perform(get("/users/passwords/su"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  @DisplayName("비밀번호 수정 실패")
  void updatePasswordFailPage() throws Exception {
    doThrow(MemberPwdChangeFailedException.class)
            .when(memberService).updatePassword(any());

    mvc.perform(post("/users/passwords")
                    .param("ui", "ui")
                    .param("pwd", "@Test12345"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("팔로워 전체 조회")
  void follows() throws Exception {
    Member followTo = MemberDummy.dummy();

    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");

    GetFollowResponseDto followResponseDto =
            new GetFollowResponseDto(followTo.getProfileThumb(),
                    followTo.getUsername(), followTo.getName());

    GetFollowResponseDto followingResponseDto =
            new GetFollowResponseDto(followFrom.getProfileThumb(),
                    followFrom.getUsername(), followFrom.getName());

    GetMemberProfileResponseDto profileDto =
            new GetMemberProfileResponseDto(
                    "username",
                    "name",
                    "bio",
                    "profileThumb",
                    "introEmail",
                    List.of(new GetMemberProfileLinkResponseDto(1L, "link")),
                    List.of(new GetBlogInProfileResponseDto("url", "blog", "bio")),
                    1L,
                    1L);

    GetAllFollowResponseDto allFollowResponseDto = new GetAllFollowResponseDto(false, List.of(followResponseDto), List.of(followingResponseDto));

    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);

    when(memberService.getProfileByUsername(anyString()))
            .thenReturn(profileDto);
    when(followService.isFollowed(anyInt(), anyString())).thenReturn(true);
    when(followService.getAllFollowInfo(anyInt(), anyString())).thenReturn(allFollowResponseDto);

    mvc.perform(get("/@username/follows")
            .flashAttrs(attrs))
            .andExpect(status().isOk());
  }
}