package com.blogitory.blog.commons.advice;

import com.blogitory.blog.commons.dto.ApiListResponse;
import com.blogitory.blog.commons.dto.ApiResponse;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Custom Rest Controller Advice for handle common response.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RestControllerAdvice
public class CustomRestControllerAdvice implements ResponseBodyAdvice<Object> {
  /**
   * All RestController through this advice.
   *
   * @param returnType    return type
   * @param converterType converter type
   * @return true
   */
  @Override
  public boolean supports(MethodParameter returnType,
                          Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  /**
   * convert to common response.
   *
   * @param body                  body
   * @param returnType            return type
   * @param selectedContentType   selectedContentType
   * @param selectedConverterType converterType
   * @param request               ServerHttpRequest
   * @param response              ServerHttpResponse
   * @return common response
   * @see ApiResponse
   * @see ApiListResponse
   */
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
    if (body instanceof Collection<?>) {
      return new ApiListResponse<>((Collection<?>) body, ((Collection<?>) body).size());
    }
    return new ApiResponse<>(body);
  }
}
