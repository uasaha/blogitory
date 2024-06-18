package com.blogitory.blog.blog.repository;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface BlogRepositoryCustom {
  List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);
}
