package com.blogitory.blog.posts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.posts.dto.request.ModifyPostsRequestDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Posts Rest controller test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {PostsRestController.class, TestSecurityConfig.class})
class PostsRestControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostsService postsService;

  @MockBean
  BlogService blogService;

  @BeforeEach
  void setUp() {
  }

  @WithMockUser("1")
  @Test
  @DisplayName("임시 게시물 키 발급")
  void key() throws Exception {
    String key = "key";

    when(postsService.getTempPostsId(any())).thenReturn(key);

    mvc.perform(get("/api/posts/key"))
            .andExpect(status().isOk())
            .andExpect(content().string(key));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 임시 저장")
  void tempSave() throws Exception {
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());

    doNothing().when(postsService).saveTempPosts(anyString(), any(), any());

    mvc.perform(post("/api/posts/tp-qwejnqwe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postsDto)))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 등록")
  void createPosts() throws Exception {
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());

    CreatePostsResponseDto responseDto = new CreatePostsResponseDto("postsUrl");

    when(postsService.createPosts(anyString(), any(), any())).thenReturn(responseDto);

    mvc.perform(post("/api/posts?tp=lqgnqwngl")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postsDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.postsUrl").value("postsUrl"));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("임시 게시물 삭제")
  void deleteTemp() throws Exception {
    doNothing().when(postsService).deleteTempPosts(anyInt(), anyString());

    mvc.perform(delete("/api/posts?tp=qklwntqnwtq"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 수정")
  void modifyPosts() throws Exception {
    ModifyPostsRequestDto requestDto = new ModifyPostsRequestDto();

    doNothing().when(postsService).modifyPosts(any(), anyString(), any());

    mvc.perform(post("/api/@username/blog/post")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 삭제")
  void deletePosts() throws Exception {
    doNothing().when(postsService).deletePosts(anyInt(), anyString());

    mvc.perform(delete("/api/@username/blog/post")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("최근 글 조회")
  void getRecentPosts() throws Exception {
    Pages<GetRecentPostResponseDto> pages = new Pages<>(List.of(), 0, false, false, 0L);
    when(postsService.getRecentPost(any())).thenReturn(pages);

    mvc.perform(get("/api/posts/recent"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("회원의 최근 글 조회")
  void getRecentPostsByUsername() throws Exception {
    List<GetRecentPostResponseDto> list = List.of();
    when(postsService.getRecentPostByUsername(anyString())).thenReturn(list);

    mvc.perform(get("/api/@username/posts/recent"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("블로그의 최근 글 조회")
  void getRecentPostsByBlog() throws Exception {
    List<GetRecentPostResponseDto> list = List.of();

    Pages<GetRecentPostResponseDto> pages = new Pages<>(list, 0, false, false, 0L);
    when(postsService.getRecentPostByBlog(any(), anyString())).thenReturn(pages);

    mvc.perform(get("/api/@username/blog/posts/recent?page=0&size=4"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("카테고리의 최근 글 조회")
  void getRecentPostsByCategory() throws Exception {
    List<GetRecentPostResponseDto> list = List.of();

    Pages<GetRecentPostResponseDto> pages = new Pages<>(list, 0, false, false, 0L);
    when(postsService.getRecentPostByCategory(any(), anyString(), anyString())).thenReturn(pages);

    mvc.perform(get("/api/@username/blog/categories/recent?page=0&size=4"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("태그의 최근 글 조회")
  void getRecentPostsByTag() throws Exception {
    List<GetRecentPostResponseDto> list = List.of();

    Pages<GetRecentPostResponseDto> pages = new Pages<>(list, 0, false, false, 0L);
    when(postsService.getRecentPostByCategory(any(), anyString(), anyString())).thenReturn(pages);

    mvc.perform(get("/api/@username/blog/tags/recent?page=0&size=4"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시글 비공개")
  void closePosts() throws Exception {
    doNothing().when(postsService).closePosts(anyInt(), anyString());

    mvc.perform(delete("/api/@username/blog/post/visible"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시글 공개")
  void openPosts() throws Exception {
    doNothing().when(postsService).openPosts(anyInt(), anyString());

    mvc.perform(post("/api/@username/blog/post/visible"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("좋아요 게시물 조회")
  void getHeartsPosts() throws Exception {
    List<GetRecentPostResponseDto> list = List.of();
    Pages<GetRecentPostResponseDto> pages = new Pages<>(list, 0, false, false, 0L);

    when(postsService.getPostsByHearts(any(), any())).thenReturn(pages);

    mvc.perform(get("/api/hearts"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("최근 활동 조회")
  void getActivity() throws Exception {
    when(postsService.getPostActivity(anyString())).thenReturn(Map.of());

    mvc.perform(get("/api/@username/activity"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("게시글 검색")
  void search() throws Exception {
    when(postsService.searchPosts(any(), anyString()))
            .thenReturn(new Pages<>(List.of(), 0L, false, false, 0L));

    mvc.perform(get("/api/posts/search?q=as"))
            .andExpect(status().isOk());
  }
}