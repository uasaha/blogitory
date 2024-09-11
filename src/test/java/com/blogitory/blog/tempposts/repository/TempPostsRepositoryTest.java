package com.blogitory.blog.tempposts.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.entity.TempPosts;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Temp posts repository test.
 *
 * @author woonseok
 * @Date 2024-08-09
 * @since 1.0
 **/
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class TempPostsRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TempPostsRepository tempPostsRepository;

  @AfterEach
  void tearDown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
  }

  @Test
  void getTempPostsByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    UUID uuid = UUID.randomUUID();
    TempPosts tempPosts = new TempPosts(uuid, member);
    tempPosts = tempPostsRepository.save(tempPosts);

    List<GetTempPostsResponseDto> tpList = tempPostsRepository.getTempPostsByMemberNo(member.getMemberNo());

    assertFalse(tpList.isEmpty());
    GetTempPostsResponseDto tp = tpList.getFirst();

    assertNotNull(tp);
    assertEquals(tempPosts.getTempPostsId().toString(), tp.getTempPostsId());
  }
}