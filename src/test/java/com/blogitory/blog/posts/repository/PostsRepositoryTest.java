package com.blogitory.blog.posts.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.entity.HeartDummy;
import com.blogitory.blog.heart.repository.HeartRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.dto.response.GetFeedPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostManageResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.entity.PostsTagDummy;
import com.blogitory.blog.poststag.repository.PostsTagRepository;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.entity.TagDummy;
import com.blogitory.blog.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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
 * Posts repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class PostsRepositoryTest {

  @Autowired
  PostsRepository postsRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  PostsTagRepository postsTagRepository;

  @Autowired
  HeartRepository heartRepository;

  @Autowired
  FollowRepository followRepository;

  @Autowired
  EntityManager entityManager;

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `tag` ALTER COLUMN `tag_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts_tag` ALTER COLUMN `posts_tag_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `follow` ALTER COLUMN `follow_no` RESTART")
            .executeUpdate();
  }

  /**
   * Posts save.
   */
  @Test
  @DisplayName("글 저장")
  void postsSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    Posts actual = postsRepository.save(posts);

    assertAll(
            () -> assertEquals(posts.getPostsNo(), actual.getPostsNo()),
            () -> assertEquals(posts.getCategory().getCategoryNo(), actual.getCategory().getCategoryNo()),
            () -> assertEquals(posts.getSubject(), actual.getSubject()),
            () -> assertEquals(posts.getUrl(), actual.getUrl()),
            () -> assertEquals(posts.getSummary(), actual.getSummary()),
            () -> assertEquals(posts.getViews(), actual.getViews()),
            () -> assertEquals(posts.getThumbnail(), actual.getThumbnail()),
            () -> assertEquals(posts.getDetail(), actual.getDetail()),
            () -> assertEquals(posts.getUpdatedAt(), actual.getUpdatedAt()),
            () -> assertNotNull(actual.getCreatedAt()),
            () -> assertEquals(posts.isOpen(), actual.isOpen()),
            () -> assertEquals(posts.isDeleted(), actual.isDeleted())
    );
  }

  @Test
  @DisplayName("글 조회")
  void getPosts() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Optional<GetPostResponseDto> responseDto =
            postsRepository.getPostByPostUrl(posts.getUrl());

    assertTrue(responseDto.isPresent());
    GetPostResponseDto actual = responseDto.get();
    assertNotNull(actual);
    assertEquals(member.getUsername(), actual.getUsername());
    assertEquals(member.getName(), actual.getMemberName());
    assertEquals(blog.getName(), actual.getBlogName());
    assertEquals(blog.getUrlName(), actual.getBlogUrl());
    assertEquals(category.getCategoryNo(), actual.getCategoryNo());
    assertEquals(category.getName(), actual.getCategoryName());
    assertEquals(posts.getUrl(), actual.getPostUrl());
    assertEquals(posts.getSubject(), actual.getSubject());
    assertEquals(posts.getSummary(), actual.getSummary());
    assertEquals(posts.getViews(), actual.getViews());
    assertEquals(posts.getDetail(), actual.getDetail());
  }

  @Test
  @DisplayName("글 수정을 위한 정보 조회")
  void getPostForModifyByUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Optional<GetPostForModifyResponseDto> postOptional =
            postsRepository.getPostForModifyByUrl(member.getMemberNo(), posts.getUrl());

    assertTrue(postOptional.isPresent());

    GetPostForModifyResponseDto actual = postOptional.get();
    assertEquals(blog.getName(), actual.getBlogName());
    assertEquals(category.getName(), actual.getCategoryName());
    assertEquals(posts.getUrl(), actual.getPostUrl());
    assertEquals(posts.getThumbnail(), actual.getThumbnailUrl());
    assertEquals(posts.getSummary(), actual.getSummary());
    assertEquals(posts.getDetail(), actual.getDetail());
  }

  @Test
  @DisplayName("최근 글 조회")
  void getRecentPosts() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetRecentPostResponseDto> page = postsRepository.getRecentPosts(pageable);

    assertFalse(page.hasPrevious());
    assertFalse(page.hasNext());
    assertEquals(1L, page.getTotalElements());
    assertEquals(posts.getSubject(), page.getContent().getFirst().getTitle());
  }

  @Test
  @DisplayName("회원 최근 글 조회")
  void getRecentPostsByUsername() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    List<GetRecentPostResponseDto> list = postsRepository.getRecentPostByUsername(pageable, member.getUsername());
    GetRecentPostResponseDto actual = list.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("회원 최근 글 조회")
  void getRecentPostsByBlog() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    List<GetRecentPostResponseDto> list = postsRepository.getRecentPostByBlog(pageable, blog.getUrlName()).getContent();
    GetRecentPostResponseDto actual = list.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("인기글 조회")
  void getPopularPostsByBlog() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    List<GetPopularPostResponseDto> responseList =
            postsRepository.getPopularPostsByBlog(blog.getUrlName());

    assertFalse(responseList.isEmpty());

    GetPopularPostResponseDto actual = responseList.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("카테고리 글 조회")
  void getRecentPostsByCategory() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetRecentPostResponseDto> page = postsRepository
            .getRecentPostByCategory(pageable, blog.getUrlName(), category.getName());

    assertEquals(1L, page.getTotalElements());

    GetRecentPostResponseDto actual = page.getContent().getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("태그 글 조회")
  void getRecentPostsByTag() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Tag tag = TagDummy.dummy();
    tag = tagRepository.save(tag);
    PostsTag postsTag = PostsTagDummy.dummy(tag, posts, blog);
    postsTag = postsTagRepository.save(postsTag);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetRecentPostResponseDto> page = postsRepository
            .getRecentPostsByTag(pageable, blog.getUrlName(), tag.getName());

    assertEquals(1L, page.getTotalElements());

    GetRecentPostResponseDto actual = page.getContent().getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("회원이 작성한 전체 글 조회")
  void getPostsByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Pageable pageable = PageRequest.of(0, 10);

    Page<GetPostManageResponseDto> page = postsRepository.getPostsByMemberNo(pageable, member.getMemberNo());

    assertEquals(1L, page.getTotalElements());

    GetPostManageResponseDto actual = page.getContent().getFirst();

    assertEquals(posts.getSubject(), actual.getPostTitle());
  }

  @Test
  @DisplayName("좋아요 표시한 글 조회")
  void getPostsByHearts() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Heart heart = HeartDummy.dummy(member, posts);
    heartRepository.save(heart);
    Pageable pageable = PageRequest.of(0, 10);

    Page<GetRecentPostResponseDto> page = postsRepository.getPostsByHearts(member.getMemberNo(), pageable);

    assertEquals(1L, page.getTotalElements());

    GetRecentPostResponseDto actual = page.getContent().getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("피드 시작 번호 조회")
  void getFeedStartPostsNoByMemberNo() {
    Member followee = MemberDummy.dummy();
    followee = memberRepository.save(followee);
    Blog blog = BlogDummy.dummy(followee);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Member follower = MemberDummy.dummy();
    ReflectionTestUtils.setField(follower, "memberNo", 2);
    memberRepository.save(follower);

    Follow follow = FollowDummy.dummy(followee, follower);
    followRepository.save(follow);

    Long startNo = postsRepository.getFeedStartPostsNoByMemberNo(follower.getMemberNo());

    assertEquals(posts.getPostsNo(), startNo);
  }

  @Test
  @DisplayName("피드 조회")
  void getFeedPostsByMemberNo() {
    Member followee = MemberDummy.dummy();
    followee = memberRepository.save(followee);
    Blog blog = BlogDummy.dummy(followee);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Member follower = MemberDummy.dummy();
    ReflectionTestUtils.setField(follower, "memberNo", 2);
    memberRepository.save(follower);

    Follow follow = FollowDummy.dummy(followee, follower);
    followRepository.save(follow);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetFeedPostsResponseDto> result =
            postsRepository.getFeedPostsByMemberNo(follower.getMemberNo(),
                    posts.getPostsNo(), pageable);

    assertEquals(1L, result.getTotalElements());
    assertEquals(posts.getUrl(), result.getContent().getFirst().getPostsUrl());
  }
}