package com.blogitory.blog.notice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.commons.config.WebMvcConfig;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * NoticeControllerTest.
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(value = {NoticeController.class, TestSecurityConfig.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
class NoticeControllerTest {

  @Autowired
  MockMvc mvc;

  @WithMockUser("1")
  @Test
  void notifications() throws Exception {
    GetMemberPersistInfoDto persistInfoDto = MemberPersistInfoDtoDummy.dummy();

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("members", persistInfoDto);
    attrs.put("noBlog", false);


    mvc.perform(MockMvcRequestBuilders.get("/notifications")
                    .flashAttrs(attrs))
            .andExpect(status().isOk());
  }
}