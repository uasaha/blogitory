package com.blogitory.blog.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Comment create request dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class CreateCommentRequestDto {
  @NotBlank
  private String postsUrl;
  @Size(min = 1, max = 2000)
  private String content;
}
