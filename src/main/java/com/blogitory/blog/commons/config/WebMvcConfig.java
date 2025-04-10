package com.blogitory.blog.commons.config;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.interceptor.AuthenticatedInterceptor;
import com.blogitory.blog.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration of Web MVC.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
  private final BlogService blogService;
  private final NoticeService noticeService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(addUserInModelInterceptor())
            .excludePathPatterns("/static/**")
            .excludePathPatterns("/api/**")
            .addPathPatterns("/**");
  }

  @Bean
  public HandlerInterceptor addUserInModelInterceptor() {
    return new AuthenticatedInterceptor(blogService, noticeService);
  }
}
