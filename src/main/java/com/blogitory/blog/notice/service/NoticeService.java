package com.blogitory.blog.notice.service;

import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.notice.dto.GetNoticeResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * Notice service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface NoticeService {

  /**
   * Get all notice by pageable.
   *
   * @param pageable pageable
   * @param memberNo member no
   * @return notice response
   */
  Pages<GetNoticeResponseDto> getAllNotice(Pageable pageable, Integer memberNo);

  /**
   * Has not read notice.
   *
   * @param memberNo member no
   * @return is read
   */
  boolean hasNonReadNotice(Integer memberNo);

  /**
   * Read notice.
   *
   * @param memberNo member no
   * @param noticeNo notice no
   */
  void read(Integer memberNo, Long noticeNo);

  /**
   * Delete notice.
   *
   * @param memberNo member no
   * @param noticeNo notice no
   */
  void delete(Integer memberNo, Long noticeNo);

  /**
   * Create follow notice.
   *
   * @param followFrom follow from member entity
   * @param followTo   follow to member entity
   */
  void followNotice(Member followFrom, Member followTo);

  /**
   * Create comment notice.
   *
   * @param name      writer name
   * @param comment   comment
   * @param commentTo posts writer
   */
  void commentNotice(String name, Comment comment, Member commentTo);

  /**
   * Create heart notice.
   *
   * @param heart   heart
   * @param heartTo posts writer
   */
  void heartNotice(Heart heart, Member heartTo);

  /**
   * Create new posts notice.
   *
   * @param post    posts
   * @param targets followers of posts writer
   */
  void newPostsNotice(Posts post, List<Member> targets);
}
