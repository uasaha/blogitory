package com.blogitory.blog.blog.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.dto.request.CreateBlogRequestDto;
import com.blogitory.blog.blog.dto.request.UpdateBlogRequestDto;
import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.exception.BlogLimitException;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.blog.service.impl.BlogServiceImpl;
import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.tag.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Blog service test.
 *
 * @author woonseok
 * @since 1.0
 */
class BlogServiceTest {
  BlogRepository blogRepository;
  MemberRepository memberRepository;
  CategoryRepository categoryRepository;
  TagRepository tagRepository;
  PasswordEncoder passwordEncoder;
  BlogService blogService;


  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    blogRepository = mock(BlogRepository.class);
    memberRepository = mock(MemberRepository.class);
    categoryRepository = mock(CategoryRepository.class);
    tagRepository = mock(TagRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);

    blogService = new BlogServiceImpl(memberRepository, blogRepository, categoryRepository, tagRepository, passwordEncoder);
  }

  /**
   * Gets blog list by member no.
   */
  @Test
  void getBlogListByMemberNo() {
    List<GetBlogInSettingsResponseDto> expect = List.of(
            new GetBlogInSettingsResponseDto(
                    "name",
                    "url",
                    "url",
                    LocalDateTime.of(2000, 2, 2, 2, 2, 2),
                    "",
                    "",
                    List.of()));

    when(blogRepository.getBlogListByMemberNo(any())).thenReturn(expect);

    List<GetBlogInSettingsResponseDto> actual = blogService.getBlogListByMemberNo(1);

    GetBlogInSettingsResponseDto expectOne = expect.getFirst();
    GetBlogInSettingsResponseDto actualOne = actual.getFirst();

    assertAll(
            () -> assertEquals(expectOne.getBlogBio(), actualOne.getBlogBio()),
            () -> assertEquals(expectOne.getBlogUrl(), actualOne.getBlogUrl()),
            () -> assertEquals(expectOne.getCreatedAt(), actualOne.getCreatedAt()),
            () -> assertEquals(expectOne.getThumbUrl(), actualOne.getThumbUrl()),
            () -> assertEquals(expectOne.getThumbOriginName(), actualOne.getThumbOriginName()),
            () -> assertTrue(expectOne.isThumbIsNull())
    );
  }

  @Test
  @DisplayName("블로그 생성 성공")
  void createBlogSuccess() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    ReflectionTestUtils.setField(member, "blogs", List.of(blog));

    CreateBlogRequestDto requestDto = new CreateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "url", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));
    when(blogRepository.save(any(Blog.class))).thenReturn(blog);
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    blogService.createBlog(requestDto, member.getMemberNo());

    verify(blogRepository, times(1)).save(any());
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  @DisplayName("블로그 생성 실패 - 없는 회원")
  void createBlogFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    ReflectionTestUtils.setField(member, "blogs", List.of(blog));

    CreateBlogRequestDto requestDto = new CreateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "url", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    when(memberRepository.findById(member.getMemberNo())).thenThrow(new NotFoundException(member.getClass()));

    assertThrows(NotFoundException.class, () -> blogService.createBlog(requestDto, 1));
  }

  @Test
  @DisplayName("블로그 생성 실패 - 3개 초과 시도")
  void createBlogFailedOver() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    ReflectionTestUtils.setField(member, "blogs", List.of(blog, blog, blog));

    CreateBlogRequestDto requestDto = new CreateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "url", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));

    assertThrows(BlogLimitException.class, () -> blogService.createBlog(requestDto, 1));
  }

  @Test
  @DisplayName("블로그 수정 성공")
  void updateBlog() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.of(blog));

    UpdateBlogRequestDto requestDto = new UpdateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    blogService.updateBlog(blog.getUrlName(), requestDto, member.getMemberNo());

    assertEquals("blog", blog.getName());
    assertEquals("blog bio", blog.getBio());
  }

  @Test
  @DisplayName("블로그 수정 실패 - 없는 블로그")
  void updateBlogFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.empty());

    UpdateBlogRequestDto requestDto = new UpdateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    assertThrows(NotFoundException.class,
            () -> blogService.updateBlog("urlName",
                    requestDto, 1));
  }

  @Test
  @DisplayName("블로그 수정 실패 - 다른 블로그")
  void updateBlogFailedAnother() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    ReflectionTestUtils.setField(member, "memberNo", 2);

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.of(blog));

    UpdateBlogRequestDto requestDto = new UpdateBlogRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    assertThrows(AuthorizationException.class,
            () -> blogService.updateBlog("urlName",
                    requestDto, 3));

  }

  @Test
  @DisplayName("블로그 삭제 성공")
  void deleteBlog() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);

    ReflectionTestUtils.setField(member, "blogs", List.of(blog));

    when(memberRepository.findById(member.getMemberNo()))
            .thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    String msg = "B-D-";

    blogService.quitBlog(member.getMemberNo(), blog.getUrlName(), member.getPassword());

    assertTrue(blog.isDeleted());
    assertTrue(blog.getName().startsWith(msg));
    assertTrue(blog.getUrlName().startsWith(msg));
  }

  @Test
  @DisplayName("블로그 삭제 실패 - 없는 회원")
  void deleteBlogFailed() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
            () -> blogService.quitBlog(3,
                    "urlName", ""));
  }

  @Test
  @DisplayName("블로그 삭제 실패 - 미소유 블로그")
  void deleteBlogFailedAnother() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(true);

    assertThrows(NotFoundException.class,
            () -> blogService.quitBlog(3, "urlName", ""));
  }

  @Test
  @DisplayName("블로그 삭제 실패 - 비밀번호 미일치")
  void deleteBlogFailedPassword() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    assertThrows(AuthorizationException.class,
            () -> blogService.quitBlog(1, "urlName", ""));
  }

  @Test
  @DisplayName("유저네임으로 블로그 리스트 조회")
  void blogListForHeader() {
    Member member = MemberDummy.dummy();
    GetBlogInHeaderResponseDto responseDto =
            new GetBlogInHeaderResponseDto("blog", "blog-url");

    List<GetBlogInHeaderResponseDto> responseDtoList = List.of(responseDto);

    when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
    when(blogRepository.getBlogListInHeaderByUsername(anyString()))
            .thenReturn(responseDtoList);

    List<GetBlogInHeaderResponseDto> actual =
            blogService.blogListForHeader(member.getUsername());

    assertFalse(actual.isEmpty());
    assertEquals(responseDtoList.getFirst().getName(), actual.getFirst().getName());
    assertEquals(responseDtoList.getFirst().getUrl(), actual.getFirst().getUrl());
  }

  @Test
  @DisplayName("블로그 url로 블로그 조회")
  void getBlogByUrl() {
    GetCategoryResponseDto categoryResponseDto = new GetCategoryResponseDto(1L, "cname", false, 0L);

    GetBlogResponseDto responseDto = new GetBlogResponseDto(
            "blogThumbUrl",
            "blogThumbOriginName",
            "blogUrl",
            "blogName",
            "name",
            "username",
            "blogBio",
            0L);

    responseDto.categories(List.of(categoryResponseDto));

    when(blogRepository.getBlogByUrl(anyString()))
            .thenReturn(Optional.of(responseDto));

    GetBlogResponseDto actual = blogService.getBlogByUrl("blogUrl");

    assertEquals(responseDto.getBlogThumbUrl(), actual.getBlogThumbUrl());
    assertEquals(responseDto.getBlogThumbOriginName(), actual.getBlogThumbOriginName());
    assertEquals(responseDto.getBlogUrl(), actual.getBlogUrl());
    assertEquals(responseDto.getBlogName(), actual.getBlogName());
    assertEquals(responseDto.getBlogBio(), actual.getBlogBio());
    assertEquals(responseDto.getName(), actual.getName());
    assertEquals(responseDto.getUsername(), actual.getUsername());
  }

  @Test
  @DisplayName("회원번호로 카테고리가 있는 블로그 조회")
  void getBlogListWithCategory() {
    GetBlogWithCategoryResponseDto responseDto = new GetBlogWithCategoryResponseDto(
            1L,
            "blogName",
            List.of(new GetCategoryInSettingsResponseDto(1L, "cname", false)));

    when(blogRepository.getBlogWithCategoryList(anyInt()))
            .thenReturn(List.of(responseDto));

    List<GetBlogWithCategoryResponseDto> actual = blogService.getBlogListWithCategory(1);

    assertFalse(actual.isEmpty());
    assertEquals(responseDto.getBlogName(), actual.getFirst().getBlogName());
    assertEquals(responseDto.getBlogNo(), actual.getFirst().getBlogNo());
    assertEquals(responseDto.getCategories().getFirst().getCategoryNo(),
            actual.getFirst().getCategories().getFirst().getCategoryNo());
  }

  @Test
  @DisplayName("내 블로그 정보 조회시")
  void getBlogForMy() {
    Member member = MemberDummy.dummy();
    GetBlogResponseDto blogResponseDto = new GetBlogResponseDto(
            "thumb", "origin",
            "blogUrl", "blogName",
            "name", member.getUsername(),
            "blogBio", 0L);

    when(blogRepository.getBlogByUrl(anyString()))
            .thenReturn(Optional.of(blogResponseDto));
    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));

    GetBlogResponseDto result = blogService.getBlogForMy(member.getMemberNo(), "url");

    assertEquals(blogResponseDto.getBlogName(), result.getBlogName());
  }

  @Test
  @DisplayName("다른 유저가 블로그 정보 조회시")
  void getBlogForMyFailed() {
    Member member = MemberDummy.dummy();
    GetBlogResponseDto blogResponseDto = new GetBlogResponseDto(
            "thumb", "origin",
            "blogUrl", "blogName",
            "name", "kjqwbgkqjwbg",
            "blogBio", 0L);

    when(blogRepository.getBlogByUrl(anyString()))
            .thenReturn(Optional.of(blogResponseDto));
    when(memberRepository.findById(member.getMemberNo())).thenReturn(Optional.of(member));

    assertThrows(AuthorizationException.class, () -> blogService.getBlogForMy(1, "url"));
  }
}