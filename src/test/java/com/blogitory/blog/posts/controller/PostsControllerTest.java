package com.blogitory.blog.posts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
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
            new GetCategoryResponseDto(1L, "name"))));

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

  @Test
  @DisplayName("게시글 조회")
  void postsPage() throws Exception {
    GetCategoryResponseDto categoryResponseDto = new GetCategoryResponseDto(1L, "cname");

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
            "summary",
            "detail",
            10,
            LocalDateTime.now(),
            null);

    when(blogService.getBlogByUrl(anyString())).thenReturn(blogResponse);
    when(postsService.getPostByUrl(anyString())).thenReturn(postResponse);

    mvc.perform(get("/@username/blog/post"))
            .andExpect(status().isOk());
  }


}