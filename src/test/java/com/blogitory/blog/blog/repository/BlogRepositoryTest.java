package com.blogitory.blog.blog.repository;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.entity.ImageDummy;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.entity.ImageCategoryDummy;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
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

/**
 * Blog repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class BlogRepositoryTest {

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ImageCategoryRepository imageCategoryRepository;

  @Autowired
  ImageRepository imageRepository;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  PostsTagRepository postsTagRepository;

  @Autowired
  PostsRepository postsRepository;

  @Autowired
  EntityManager entityManager;

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image_category` ALTER COLUMN `image_category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image` ALTER COLUMN `image_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts_tag` ALTER COLUMN `posts_tag_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
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
            () -> assertEquals(blog.getBio(), actual.getBio()),
            () -> assertEquals(blog.getUrlName(), actual.getUrlName()),
            () -> assertEquals(blog.getBackground(), actual.getBackground()),
            () -> assertEquals(blog.getTheme(), actual.getTheme()),
            () -> assertEquals(blog.isDeleted(), actual.isDeleted())
    );
  }

  @Test
  @DisplayName("회원번호로 블로그 조회")
  void getBlogListByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    ImageCategory imageCategory = ImageCategoryDummy.dummy();
    imageCategory = imageCategoryRepository.save(imageCategory);

    Image image = ImageDummy.blogDummy(imageCategory, blog);
    image = imageRepository.save(image);

    List<GetBlogInSettingsResponseDto> actual =
            blogRepository.getBlogListByMemberNo(member.getMemberNo());

    GetBlogInSettingsResponseDto actualGet = actual.getFirst();

    Blog expect = blog;
    Image expectImage = image;

    assertAll(
            () -> assertEquals(expect.getBio(), actualGet.getBlogBio()),
            () -> assertEquals(expect.getUrlName(), actualGet.getBlogUrl()),
            () -> assertFalse(actualGet.isThumbIsNull()),
            () -> assertEquals(expectImage.getUrl(), actualGet.getThumbUrl()),
            () -> assertEquals(expectImage.getOriginName(), actualGet.getThumbOriginName())
    );
  }

  @Test
  @DisplayName("유저네임으로 블로그 리스트 조회")
  void getBlogListInHeaderByUsername() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    List<GetBlogInHeaderResponseDto> actual =
            blogRepository.getBlogListInHeaderByUsername(member.getUsername());

    GetBlogInHeaderResponseDto actualGet = actual.getFirst();

    Blog expect = blog;

    assertEquals(expect.getName(), actualGet.getName());
    assertEquals(expect.getUrlName(), actualGet.getUrl());
  }

  @Test
  @DisplayName("블로그 URL로 블로그 조회")
  void getBlogByUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    ImageCategory imageCategory = ImageCategoryDummy.dummy();
    imageCategory = imageCategoryRepository.save(imageCategory);

    Image image = ImageDummy.blogDummy(imageCategory, blog);
    image = imageRepository.save(image);

    Tag tag = TagDummy.dummy();
    tag = tagRepository.save(tag);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    PostsTag postsTag = PostsTagDummy.dummy(tag, posts, blog);
    postsTagRepository.save(postsTag);

    Optional<GetBlogResponseDto> optionalResponseDto =
            blogRepository.getBlogByUrl(blog.getUrlName());

    assertTrue(optionalResponseDto.isPresent());

    GetBlogResponseDto responseDto = optionalResponseDto.get();

    assertEquals(image.getUrl(), responseDto.getBlogThumbUrl());
    assertEquals(image.getOriginName(), responseDto.getBlogThumbOriginName());
    assertEquals(blog.getUrlName(), responseDto.getBlogUrl());
    assertEquals(blog.getName(), responseDto.getBlogName());
    assertEquals(member.getName(), responseDto.getName());
    assertEquals(member.getUsername(), responseDto.getUsername());
    assertEquals(blog.getBio(), responseDto.getBlogBio());
  }

  @Test
  @DisplayName("카테고리를 포함한 모든 블로그 조회")
  void getBlogWithCategoryList() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    List<GetBlogWithCategoryResponseDto> responseDtoList =
            blogRepository.getBlogWithCategoryList(member.getMemberNo());

    assertFalse(responseDtoList.isEmpty());

    GetBlogWithCategoryResponseDto responseDto = responseDtoList.getFirst();

    assertEquals(blog.getBlogNo(), responseDto.getBlogNo());
    assertEquals(blog.getName(), responseDto.getBlogName());
    assertEquals(category.getCategoryNo(), responseDto.getCategories().getFirst().getCategoryNo());
  }
}