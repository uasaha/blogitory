package com.blogitory.blog.blog.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.blog.service.impl.BlogServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Blog service test.
 *
 * @author woonseok
 * @since 1.0
 */
class BlogServiceTest {
  /**
   * The Blog repository.
   */
  BlogRepository blogRepository;
  /**
   * The Blog service.
   */
  BlogService blogService;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    blogRepository = mock(BlogRepository.class);
    blogService = new BlogServiceImpl(blogRepository);
  }

  /**
   * Gets blog list by member no.
   */
  @Test
  void getBlogListByMemberNo() {
    List<BlogListInSettingsResponseDto> expect = List.of(
            new BlogListInSettingsResponseDto(
                    "name",
                    "url",
                    "intro",
                    LocalDateTime.of(2000, 02, 02, 02, 02, 02),
                    "",
                    ""));

    when(blogRepository.getBlogListByMemberNo(any())).thenReturn(expect);

    List<BlogListInSettingsResponseDto> actual = blogService.getBlogListByMemberNo(1);

    BlogListInSettingsResponseDto expectOne = expect.get(0);
    BlogListInSettingsResponseDto actualOne = actual.get(0);

    assertAll(
            () -> assertEquals(expectOne.getBlogBio(), actualOne.getBlogBio()),
            () -> assertEquals(expectOne.getBlogUrl(), actualOne.getBlogUrl()),
            () -> assertEquals(expectOne.getBlogIntro(), actualOne.getBlogIntro()),
            () -> assertEquals(expectOne.getCreatedAt(), actualOne.getCreatedAt()),
            () -> assertEquals(expectOne.getThumbUrl(), actualOne.getThumbUrl()),
            () -> assertEquals(expectOne.getThumbOriginName(), actualOne.getThumbOriginName()),
            () -> assertTrue(expectOne.isThumbIsNull())
            );
  }
}