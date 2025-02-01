package com.blogitory.blog.posts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.commons.config.WebMvcConfig;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.GetBeforeNextPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.viewer.service.ViewerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Posts controller test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {PostsController.class, TestSecurityConfig.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
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

  @MockBean
  ViewerService viewerService;

  @WithMockUser("1")
  @Test
  @DisplayName("글 작성 페이지")
  void issuePostsPage() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    List<GetBlogWithCategoryResponseDto> blogs = List.of(new GetBlogWithCategoryResponseDto(tpDto.getBlogNo(), "blogName", List.of(
            new GetCategoryInSettingsResponseDto(1L, "name", false))));

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
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
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
    GetCategoryResponseDto categoryResponseDto = new GetCategoryResponseDto(1L, "cname", false, 0L);

    GetBlogResponseDto blogResponse = new GetBlogResponseDto(
            "blogThumbUrl",
            "blogThumbOriginName",
            "blogUrl",
            "blogName",
            "name",
            "username",
            "blogBio",
            0L);

    blogResponse.categories(List.of(categoryResponseDto));
    blogResponse.tags(List.of());

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

    GetBeforeNextPostsResponseDto beforeNextPostsResponseDto =
            new GetBeforeNextPostsResponseDto("", "", 1L, List.of());

    when(blogService.getBlogByUrl(anyString())).thenReturn(blogResponse);
    when(postsService.getPostByUrl(anyString())).thenReturn(postResponse);
    when(followService.isFollowed(anyInt(), anyString())).thenReturn(true);
    when(postsService.getRelatedPosts(anyString())).thenReturn(beforeNextPostsResponseDto);

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

  @WithMockUser("1")
  @Test
  @DisplayName("좋아요 표시한 게시물 페이지")
  void hearts() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mvc.perform(get("/hearts")
                    .flashAttrs(attrs))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pageable"));
  }

  @Test
  @DisplayName("검색 페이지")
  void searchPage() throws Exception {
    mvc.perform(get("/search?q=asd"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("keyword"));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("조회수 페이지")
  void viewerPage() throws Exception {
    GetPostResponseDto responseDto = new GetPostResponseDto(
            null, null, null,
            null, null, null,
            null, null, null,
            null, null, null,
            null, null);

    when(postsService.getPostByUrl(anyString())).thenReturn(responseDto);
    when(viewerService.getViewersCount(anyInt(), anyString()))
            .thenReturn(1);

    mvc.perform(get("/@test/test/test/viewers"))
            .andExpect(status().isOk());
  }
}