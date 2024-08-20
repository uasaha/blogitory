package com.blogitory.blog.commons.dto;

import java.util.List;

/**
 * Dto class for pagenation response.
 *
 * @author woonseok
 * @Date 2024-08-13
 * @since 1.0
 **/
public record Pages<E>(List<E> body,
                       long currentPage,
                       boolean hasPrevious,
                       boolean hasNext,
                       Long total) {
  public boolean isEmpty() {
    return body.isEmpty();
  }
}