package com.blogitory.blog.image.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.image.dto.UpdateThumbnailResponseDto;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.member.service.MemberService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Image rest controller test.
 *
 * @author woonseok
 * @since 1.0
 */
@WebMvcTest(value = {ImageRestController.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class ImageRestControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  MemberService memberService;

  @MockBean
  ImageService imageService;

  @MockBean
  BlogService blogService;

  @Test
  @DisplayName("프로필 썸네일 업데이트 성공")
  @WithMockUser("1")
  void updateThumbnail() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file",
            "content".getBytes(StandardCharsets.UTF_8));
    UpdateThumbnailResponseDto responseDto =
            new UpdateThumbnailResponseDto("url", "origin_name");

    when(imageService.uploadThumbnail(any(), any())).thenReturn(responseDto);

    mvc.perform(MockMvcRequestBuilders.multipart("/api/images/thumbnail")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("url").value(responseDto.getUrl()))
            .andExpect(jsonPath("originName").value(responseDto.getOriginName()));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("프로필 썸네일 삭제")
  void deleteThumbnail() throws Exception {
    doNothing().when(imageService).removeThumbnail(anyInt());

    mvc.perform(delete("/api/images/thumbnail"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시글 이미지 등록")
  void uploadPostsImages() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file",
            "content".getBytes(StandardCharsets.UTF_8));
    UpdateThumbnailResponseDto responseDto =
            new UpdateThumbnailResponseDto("url", "origin_name");

    when(imageService.uploadPostsImages(any(), any())).thenReturn(responseDto);

    mvc.perform(MockMvcRequestBuilders.multipart("/api/images/posts")
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("url").value(responseDto.getUrl()))
            .andExpect(jsonPath("originName").value(responseDto.getOriginName()));
  }
}