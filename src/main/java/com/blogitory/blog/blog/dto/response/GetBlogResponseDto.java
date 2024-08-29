package com.blogitory.blog.blog.dto.response;

import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Get blog response dto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@Getter
public class GetBlogResponseDto {
  private final String blogThumbUrl;
  private final String blogThumbOriginName;
  private final String blogUrl;
  private final String blogName;
  private final String name;
  private final String username;
  private final String blogBio;
  private final long postsCnt;
  private List<GetCategoryResponseDto> categories = new ArrayList<>();
  private List<GetTagResponseDto> tags = new ArrayList<>();

  public GetBlogResponseDto(String blogThumbUrl,
                            String blogThumbOriginName,
                            String blogUrl,
                            String blogName,
                            String name,
                            String username,
                            String blogBio,
                            long postsCnt) {
    this.blogThumbUrl = blogThumbUrl;
    this.blogThumbOriginName = blogThumbOriginName;
    this.blogUrl = blogUrl;
    this.blogName = blogName;
    this.name = name;
    this.username = username;
    this.blogBio = blogBio;
    this.postsCnt = postsCnt;
  }

  public void categories(List<GetCategoryResponseDto> categories) {
    this.categories = categories.stream().distinct().toList();
  }

  public void tags(List<GetTagResponseDto> tags) {
    this.tags = tags.stream().distinct().toList();
  }
}
