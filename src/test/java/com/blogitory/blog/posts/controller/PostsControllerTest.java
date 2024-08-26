package com.blogitory.blog.posts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {PostsController.class, TestSecurityConfig.class})
class PostsControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @MockBean
  PostsService postsService;

  @MockBean
  FollowService followService;

  @BeforeEach
  void setUp() {
  }

  @WithMockUser("1")
  @Test
  @DisplayName("글 작성 페이지")
  void issuePostsPage() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "detail",
            List.of("tag"));

    List<GetBlogWithCategoryResponseDto> blogs = List.of(new GetBlogWithCategoryResponseDto(tpDto.getBlogNo(), "blogName", List.of(
            new GetCategoryResponseDto(1L, "name", false))));

    when(postsService.loadTempPosts(any(), any())).thenReturn(tpDto);
    when(blogService.getBlogListWithCategory(anyInt())).thenReturn(blogs);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mvc.perform(get("/posts")
            .param("tp", "12345")
                    .flashAttrs(attrs))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("글 작성 페이지 - 블로그 없음")
  void issuePostsPageNoBlog() throws Exception {
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "detail",
            List.of("tag"));

    List<GetBlogWithCategoryResponseDto> blogs = List.of();

    when(postsService.loadTempPosts(any(), any())).thenReturn(tpDto);
    when(blogService.getBlogListWithCategory(anyInt())).thenReturn(blogs);

    mvc.perform(get("/posts")
                    .param("tp", "12345"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/settings/blog"));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시글 조회")
  void postsPage() throws Exception {
    GetCategoryResponseDto categoryResponseDto = new GetCategoryResponseDto(1L, "cname", false);

    GetBlogResponseDto blogResponse = new GetBlogResponseDto(
            "blogThumbUrl",
            "blogThumbOriginName",
            "blogUrl",
            "blogName",
            "name",
            "username",
            "blogBio",
            List.of(categoryResponseDto),
            List.of());

    GetPostResponseDto postResponse = new GetPostResponseDto(
            "username",
            "memberName",
            "blogName",
            "blogUrl",
            1L,
            "categoryName",
            "postUrl",
            "subject",
            "postsThumb",
            "summary",
            "detail",
            10,
            LocalDateTime.now(),
            null);

    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);

    when(blogService.getBlogByUrl(anyString())).thenReturn(blogResponse);
    when(postsService.getPostByUrl(anyString())).thenReturn(postResponse);
    when(followService.isFollowed(anyInt(), anyString())).thenReturn(true);

    mvc.perform(get("/@username/blog/post")
                    .flashAttrs(attrs))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시글 수정 페이지")
  void modifyPostsPage() throws Exception {
    GetPostForModifyResponseDto responseDto = new GetPostForModifyResponseDto();
    ReflectionTestUtils.setField(responseDto, "blogName", "blog");
    ReflectionTestUtils.setField(responseDto, "categoryName", "categoryName");
    ReflectionTestUtils.setField(responseDto, "title", "title");
    ReflectionTestUtils.setField(responseDto, "postUrl", "postUrl");
    ReflectionTestUtils.setField(responseDto, "thumbnailUrl", "thumbnailUrl");
    ReflectionTestUtils.setField(responseDto, "summary", "summary");
    ReflectionTestUtils.setField(responseDto, "detail", "detail");
    responseDto.tags(List.of());

    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);


    when(postsService.getPostForModifyByUrl(anyInt(), anyString())).thenReturn(responseDto);

    mvc.perform(get("/@username/blog/post/mod")
                    .flashAttrs(attrs))
            .andExpect(status().isOk());
  }
}