package com.blogitory.blog.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for update profile.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class MemberUpdateProfileRequestDto {
  @NotBlank
  @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long")
  @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$")
  private String name;

  @Size(max = 200)
  private String bio;

  @Email
  private String email;

  @Size(max = 5)
  private List<String> linkList;
}
