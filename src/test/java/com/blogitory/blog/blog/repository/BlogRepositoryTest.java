package com.blogitory.blog.blog.repository;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Blog repository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class BlogRepositoryTest {
  @Autowired
  BlogRepository blogRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("블로그 저장")
  void blogSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    Blog actual = blogRepository.save(blog);

    assertAll(
            () -> assertEquals(blog.getBlogNo(), actual.getBlogNo()),
            () -> assertEquals(blog.getMember().getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertEquals(blog.getName(), actual.getName()),
            () -> assertEquals(blog.getUrlName(), actual.getUrlName()),
            () -> assertEquals(blog.getBackground(), actual.getBackground()),
            () -> assertEquals(blog.getIntro(), actual.getIntro()),
            () -> assertEquals(blog.getTheme(), actual.getTheme())
    );
  }


}