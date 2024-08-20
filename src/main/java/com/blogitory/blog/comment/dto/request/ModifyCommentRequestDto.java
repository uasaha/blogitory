package com.blogitory.blog.comment.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for Modify Comment.
 *
 * @author woonseok
 * @Date 2024-08-20
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class ModifyCommentRequestDto {
  @Size(min = 1, max = 2000)
  private String contents;
}
