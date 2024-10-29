package com.blogitory.blog.tempposts.repository.impl;


import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.entity.QTempPosts;
import com.blogitory.blog.tempposts.entity.TempPosts;
import com.blogitory.blog.tempposts.repository.TempPostsRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Temp posts repository implementation for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class TempPostsRepositoryImpl extends QuerydslRepositorySupport
        implements TempPostsRepositoryCustom {
  public TempPostsRepositoryImpl() {
    super(TempPosts.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetTempPostsResponseDto> getTempPostsByMemberNo(Integer memberNo) {
    QTempPosts tempPosts = QTempPosts.tempPosts;
    QMember member = QMember.member;

    return from(tempPosts)
            .select(Projections.constructor(
                    GetTempPostsResponseDto.class,
                    tempPosts.tempPostsId,
                    tempPosts.title,
                    tempPosts.createdAt))
            .innerJoin(member).on(tempPosts.member.eq(member))
            .orderBy(tempPosts.createdAt.desc())
            .limit(10L)
            .where(member.memberNo.eq(memberNo))
            .fetch();
  }
}
