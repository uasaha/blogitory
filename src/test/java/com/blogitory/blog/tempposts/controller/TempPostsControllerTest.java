package com.blogitory.blog.tempposts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.commons.config.WebMvcConfig;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.service.TempPostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * TempPostsController test.
 *
 * @author woonseok
 * @Date 2024-08-09
 * @since 1.0
 **/
@WebMvcTest(value = {TempPostsController.class, TestSecurityConfig.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
class TempPostsControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  TempPostsService tempPostsService;

  @WithMockUser("1")
  @Test
  @DisplayName("임시 게시물 페이지")
  void tempPostsPage() throws Exception {
    UUID uuid = UUID.randomUUID();
    GetTempPostsResponseDto responseDto = new GetTempPostsResponseDto(uuid, LocalDateTime.now());
    responseDto.setTempPostsTitle("title");

    when(tempPostsService.getTempPostsListByMemberNo(any())).thenReturn(List.of(responseDto));

    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);

    mockMvc.perform(get("/tp")
                    .flashAttrs(attrs))
            .andExpect(status().isOk());
  }
}