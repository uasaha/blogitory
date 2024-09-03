package com.blogitory.blog.blog.dto.request;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.member.entity.Member;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Blog create request dto.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateBlogRequestDto {
  @Size(min = 2, max = 20)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s_-]+$")
  private String name;

  @Size(min = 1, max = 100)
  @Pattern(regexp = "^[a-zA-Z0-9-]+$")
  private String url;

  @Size(max = 200)
  private String bio;

  /**
   * new Blog of member.
   *
   * @param member member
   * @return new Blog
   */
  public Blog of(Member member) {
    return new Blog(
            null,
            member,
            this.name,
            this.bio,
            "@" + member.getUsername() + "/" + this.url,
            null,
            "light",
            false,
            LocalDateTime.now()
    );
  }
}
