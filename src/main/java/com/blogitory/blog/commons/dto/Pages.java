package com.blogitory.blog.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Dto class for pagenation response.
 *
 * @author woonseok
 * @since 1.0
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public record Pages<E>(List<E> body,
                       long currentPage,
                       boolean hasPrevious,
                       boolean hasNext,
                       Long total) {
  public boolean isEmpty() {
    return body.isEmpty();
  }
}
