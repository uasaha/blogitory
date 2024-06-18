package com.blogitory.blog.image.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
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
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 */
@WebMvcTest(value = {ImageRestController.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class ImageRestControllerTest {
  /**
   * The Mvc.
   */
  @Autowired
  MockMvc mvc;

  /**
   * The Member service.
   */
  @MockBean
  MemberService memberService;

  /**
   * The Image service.
   */
  @MockBean
  ImageService imageService;

  /**
   * Update thumbnail.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("프로필 썸네일 업데이트 성공")
  @WithMockUser("1")
  void updateThumbnail() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file",
            "content".getBytes(StandardCharsets.UTF_8));
    ThumbnailUpdateResponseDto responseDto =
            new ThumbnailUpdateResponseDto("url", "origin_name");

    when(imageService.uploadThumbnail(any(), any())).thenReturn(responseDto);

    mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/image/thumbnail")
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("url").value(responseDto.getUrl()))
            .andExpect(jsonPath("originName").value(responseDto.getOriginName()))
            .andDo(print());
  }
}