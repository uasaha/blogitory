package com.blogitory.blog.visitant.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get visitant count response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetVisitantCountResponseDto {
  LocalDate date;
  Integer visitants;
}
