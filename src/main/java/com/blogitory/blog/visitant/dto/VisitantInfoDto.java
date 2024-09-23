package com.blogitory.blog.visitant.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Visitant info dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class VisitantInfoDto {
  private Integer memberNo;
  private String ip;
}
