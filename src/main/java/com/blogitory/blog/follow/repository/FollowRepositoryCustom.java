package com.blogitory.blog.follow.repository;

import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom Follow Repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface FollowRepositoryCustom {

  /**
   * Get count of followee.
   *
   * @param followFromNo member no
   * @return count
   */
  Long countFollowee(Integer followFromNo);

  /**
   * Get count of follower.
   *
   * @param followToNo member no
   * @return count
   */
  Long countFollower(Integer followToNo);

  /**
   * Get all about follower/followee by member.
   *
   * @param memberNo member no
   * @return follow entity
   */
  List<Follow> findRelatedByMemberNo(Integer memberNo);

  /**
   * Get follower by member.
   *
   * @param memberNo member no
   * @return members
   */
  List<Member> findFollowersByMemberNo(Integer memberNo);

  /**
   * Find follow by from member no, to member username. (PK)
   *
   * @param fromNo     from member no
   * @param toUsername to username
   * @return follow entity
   */
  Optional<Follow> findByFromNoAndToUsername(Integer fromNo, String toUsername);

  /**
   * Get all followee by to username.
   *
   * @param toUsername to username
   * @return followee
   */
  List<GetFollowResponseDto> getAllFollowerByToUsername(String toUsername);

  /**
   * Get all follower by from username.
   *
   * @param fromUsername from username
   * @return followers
   */
  List<GetFollowResponseDto> getAllFollowingByFromUsername(String fromUsername);
}
