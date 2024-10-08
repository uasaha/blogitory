package com.blogitory.blog.blog.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Blog modify request dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class UpdateBlogRequestDto {
  @Size(min = 1, max = 20)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s_-]+$")
  private String name;
  @Size(max = 200)
  private String bio;
}
