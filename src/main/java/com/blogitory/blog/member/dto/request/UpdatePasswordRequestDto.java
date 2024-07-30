package com.blogitory.blog.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Modify password request dto.
 *
 * @author woonseok
 * @Date 2024-07-22
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class UpdatePasswordRequestDto {
  @NotBlank
  private String ui;

  @NotBlank
  @Size(min = 8, message = "Password must be between 8 and 100 characters long")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=?<>.,/|~`])"
          + "[A-Za-z\\d!@#$%^&*()_+=?<>.,/|~`]{8,}$")
  private String pwd;
}
