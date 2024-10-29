package com.blogitory.blog.notice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.commons.config.WebMvcConfig;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.notice.dto.DefaultNoticeResponseDto;
import com.blogitory.blog.notice.dto.GetNoticeResponseDto;
import com.blogitory.blog.notice.enums.NoticeType;
import com.blogitory.blog.notice.service.NoticeService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * NoticeRestControllerTest.
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(value = {NoticeRestController.class, TestSecurityConfig.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
class NoticeRestControllerTest {

  @MockBean
  NoticeService noticeService;

  @Autowired
  MockMvc mvc;

  @WithMockUser("1")
  @Test
  void readNotice() throws Exception {
    doNothing().when(noticeService).read(anyInt(), anyLong());

    mvc.perform(get("/api/notifications/1"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  void deleteNotice() throws Exception {
    doNothing().when(noticeService).delete(anyInt(), anyLong());

    mvc.perform(delete("/api/notifications/1"))
            .andExpect(status().isNoContent());
  }

  @WithMockUser("1")
  @Test
  void getNotices() throws Exception {
    GetNoticeResponseDto noticeResponseDto = new GetNoticeResponseDto(
            1L,
            new DefaultNoticeResponseDto(
                    NoticeType.FOLLOW, "url",
                    "sender", "content"),
            false,
            LocalDateTime.now());

    Pages<GetNoticeResponseDto> pages = new Pages<>(
            List.of(noticeResponseDto), 0,
            false, false,
            1L);

    when(noticeService.getAllNotice(any(), anyInt()))
            .thenReturn(pages);

    mvc.perform(get("/api/notifications"))
            .andExpect(status().isOk());
  }
}