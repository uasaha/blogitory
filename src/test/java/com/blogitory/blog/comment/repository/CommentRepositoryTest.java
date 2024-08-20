package com.blogitory.blog.comment.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.entity.CommentDummy;
import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Comment repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class CommentRepositoryTest {

  /**
   * The Comment repository.
   */
  @Autowired
  CommentRepository commentRepository;

  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Posts repository.
   */
  @Autowired
  PostsRepository postsRepository;

  /**
   * The Blog repository.
   */
  @Autowired
  BlogRepository blogRepository;

  /**
   * The Category repository.
   */
  @Autowired
  CategoryRepository categoryRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `comment` ALTER COLUMN `comment_no` RESTART")
            .executeUpdate();
  }

  /**
   * Comment save.
   */
  @Test
  @DisplayName("댓글 저장")
  void commentSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Comment comment = CommentDummy.dummy(member, posts);
    Comment actual = commentRepository.save(comment);

    assertAll(
            () -> assertEquals(comment.getCommentNo(), actual.getCommentNo()),
            () -> assertEquals(comment.getMember().getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertEquals(comment.getPosts().getPostsNo(), actual.getPosts().getPostsNo()),
            () -> assertNull(comment.getParentComment()),
            () -> assertEquals(comment.getContents(), actual.getContents()),
            () -> assertEquals(comment.getUpdatedAt(), actual.getUpdatedAt()),
            () -> assertEquals(comment.isDeleted(), actual.isDeleted())
    );
  }

  @Test
  @DisplayName("댓글 조회")
  void getComments() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Comment comment = CommentDummy.dummy(member, posts);
    comment = commentRepository.save(comment);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetCommentResponseDto> comments =
            commentRepository.getComments(posts.getUrl(), pageable);

    GetCommentResponseDto result = comments.getContent().getFirst();

    assertEquals(comment.getCommentNo(), result.getCommentNo());
    assertEquals(comment.getContents(), result.getContent());
  }

  @Test
  @DisplayName("답글 조회")
  void getChildComments() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Comment comment = CommentDummy.dummy(member, posts);
    comment = commentRepository.save(comment);

    Comment child = CommentDummy.dummy(member, posts, comment);
    ReflectionTestUtils.setField(child, "commentNo", 2L);
    child = commentRepository.save(child);

    Pageable pageable = PageRequest.of(0, 10);


    Page<GetChildCommentResponseDto> comments =
            commentRepository.getChildCommentsByParent(posts.getUrl(), comment.getCommentNo(), pageable);

    GetChildCommentResponseDto result = comments.getContent().getFirst();

    assertEquals(child.getCommentNo(), result.getCommentNo());
    assertEquals(child.getContents(), result.getContent());
  }
}