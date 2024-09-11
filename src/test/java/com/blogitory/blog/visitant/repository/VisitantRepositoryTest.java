package com.blogitory.blog.visitant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.visitant.entity.Visitant;
import com.blogitory.blog.visitant.entity.VisitantDummy;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * VisitantRepositoryTest.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class VisitantRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  VisitantRepository visitantRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `visitant` ALTER COLUMN `visitant_no` RESTART")
            .executeUpdate();
  }

  @Test
  void getCountByBlogUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Visitant visitant = VisitantDummy.dummy(blog);
    visitantRepository.save(visitant);

    int result = visitantRepository.getCountByBlogUrl(blog.getUrlName());

    assertEquals(0, result);
  }

  @Test
  void getCountByBlogUrlAndDate() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Visitant visitant = VisitantDummy.dummy(blog);
    visitantRepository.save(visitant);

    int result = visitantRepository.getCountByBlogUrlAndDate(blog.getUrlName(), visitant.getVisitDate());

    assertEquals(0, result);
  }

  @Test
  void findByBlogUrlAndDate() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Visitant visitant = VisitantDummy.dummy(blog);
    visitantRepository.save(visitant);

    Optional<Visitant> result = visitantRepository.findByBlogUrlAndDate(blog.getUrlName(), visitant.getVisitDate());

    assertTrue(result.isPresent());
    assertEquals(visitant.getVisitDate(), result.get().getVisitDate());
  }
}