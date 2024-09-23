package com.blogitory.blog.viewer.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get viewer count response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetViewerCountResponseDto {
  LocalDate date;
  Integer viewers;
}
