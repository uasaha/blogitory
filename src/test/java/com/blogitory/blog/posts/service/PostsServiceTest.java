package com.blogitory.blog.posts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.dto.request.ModifyPostsRequestDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetFeedPostsPagesResponseDto;
import com.blogitory.blog.posts.dto.response.GetFeedPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostActivityResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostManageResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.exception.InvalidPostsUrlException;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.posts.service.impl.PostsServiceImpl;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.repository.PostsTagRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.repository.TagRepository;
import com.blogitory.blog.tempposts.entity.TempPosts;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Posts service test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
class PostsServiceTest {
  ApplicationEventPublisher eventPublisher;
  MemberRepository memberRepository;
  PostsRepository postsRepository;
  TempPostsRepository tempPostsRepository;
  CategoryRepository categoryRepository;
  TagRepository tagRepository;
  PostsTagRepository postsTagRepository;
  PostsService postsService;

  @BeforeEach
  void setUp() {
    eventPublisher = mock(ApplicationEventPublisher.class);
    memberRepository = mock(MemberRepository.class);
    postsRepository = mock(PostsRepository.class);
    tempPostsRepository = mock(TempPostsRepository.class);
    categoryRepository = mock(CategoryRepository.class);
    tagRepository = mock(TagRepository.class);
    postsTagRepository = mock(PostsTagRepository.class);

    postsService = new PostsServiceImpl(
            eventPublisher,
            memberRepository,
            postsRepository,
            tempPostsRepository,
            categoryRepository,
            tagRepository,
            postsTagRepository);
  }

  @Test
  @DisplayName("임시 게시물 ID 발급")
  void getTempPostsId() {
    Member member = MemberDummy.dummy();

    UUID uuid = UUID.randomUUID();
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(tempPostsRepository.save(any())).thenReturn(tempPosts);

    String actual = postsService.getTempPostsId(member.getMemberNo());

    assertEquals(uuid.toString(), actual);
  }

  @Test
  @DisplayName("임시 게시물 ID 발급 실패")
  void getTempPostsIdFailed() {
    Member member = MemberDummy.dummy();

    UUID uuid = UUID.randomUUID();
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(memberRepository.findById(any())).thenReturn(Optional.empty());
    when(tempPostsRepository.save(any())).thenReturn(tempPosts);

    assertThrows(NotFoundException.class,
            () -> postsService.getTempPostsId(1));
  }

  @Test
  @DisplayName("임시게시물 로드 성공")
  void loadTempPosts() {
    UUID uuid = UUID.randomUUID();
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    Member member = MemberDummy.dummy();
    TempPosts tp = new TempPosts(uuid, member);
    tp.updateTempPosts(tpDto);

    when(tempPostsRepository.findById(uuid)).thenReturn(Optional.of(tp));

    SaveTempPostsDto actual = postsService.loadTempPosts(uuid.toString(), member.getMemberNo());

    assertEquals(tpDto.getTitle(), actual.getTitle());
    assertEquals(tpDto.getSummary(), actual.getSummary());
  }

  @Test
  @DisplayName("임시게시물 로드 실패 - 다른 유저")
  void loadTempPostsFailedAuthorization() {
    UUID uuid = UUID.randomUUID();

    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    Member member = MemberDummy.dummy();
    TempPosts tp = new TempPosts(uuid, member);
    tp.updateTempPosts(tpDto);

    when(tempPostsRepository.findById(uuid)).thenReturn(Optional.of(tp));

    String uuidStr = uuid.toString();

    assertThrows(AuthorizationException.class, () -> postsService.loadTempPosts(uuidStr, 2));
  }


  @Test
  @DisplayName("임시게시물 저장")
  void saveTempPosts() {
    UUID uuid = UUID.randomUUID();

    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    Member member = MemberDummy.dummy();
    TempPosts tp = new TempPosts(uuid, member);
    tp.updateTempPosts(tpDto);

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tp));

    postsService.saveTempPosts(uuid.toString(), tpDto, 1);

    assertEquals(1, tp.getMember().getMemberNo());
  }

  @Test
  @DisplayName("게시글 저장 성공")
  void createPosts() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    Posts posts = PostsDummy.dummy(category);
    Tag tag = new Tag(1L, "tag", false);
    PostsTag postsTag = new PostsTag(1L, tag, posts, blog);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
    when(postsRepository.existsByUrl(anyString())).thenReturn(false);
    when(postsRepository.save(any(Posts.class))).thenReturn(posts);
    when(tagRepository.findByName(anyString()))
            .thenReturn(Optional.of(tag));
    when(tagRepository.save(any(Tag.class))).thenReturn(tag);
    when(postsTagRepository.save(any(PostsTag.class))).thenReturn(postsTag);
    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    doNothing().when(tempPostsRepository).delete(any());

    CreatePostsResponseDto responseDto =
            postsService.createPosts(uuid.toString(), 1, tpDto);

    assertEquals("posts_url", responseDto.getPostsUrl());
  }

  @Test
  @DisplayName("게시물 저장 실패 - 다른 유저")
  void createPostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

    assertThrows(AuthorizationException.class, () -> postsService.createPosts("up", 0, tpDto));
  }

  @Test
  @DisplayName("게시글 저장 실패 - url 패턴 미일치")
  void createPostsFailInvalidUrl() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url!@#",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));
    TempPosts tempPosts = new TempPosts(uuid, member);
    tempPosts.updateTempPosts(tpDto);

    Posts posts = PostsDummy.dummy(category);

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
    when(postsRepository.save(any(Posts.class))).thenReturn(posts);

    String uuidString = uuid.toString();

    assertThrows(InvalidPostsUrlException.class, () -> postsService.createPosts(uuidString, 1, tpDto));
  }

  @Test
  @DisplayName("임시 게시물 삭제 성공")
  void deleteTempPosts() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));

    postsService.deleteTempPosts(1, uuid.toString());

    verify(tempPostsRepository).delete(any());
  }

  @Test
  @DisplayName("임시 게시물 삭제 실패")
  void deleteTempPostsFailed() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    String uuidString = uuid.toString();
    assertThrows(AuthorizationException.class, () -> postsService.deleteTempPosts(2, uuidString));
  }


  @Test
  @DisplayName("URL로 조회 성공")
  void getPostByUrl() {
    GetPostResponseDto responseDto = new GetPostResponseDto(
            "username",
            "memberName",
            "blogName",
            "blogUrl",
            1L,
            "categoryName",
            "postUrl",
            "subject",
            "postsThumb",
            "summary",
            "detail",
            10,
            LocalDateTime.now(),
            null);
    List<GetTagResponseDto> tagResponseDto = List.of(new GetTagResponseDto("tag"));

    when(postsRepository.getPostByPostUrl(anyString()))
            .thenReturn(Optional.of(responseDto));
    when(tagRepository.getTagListByPost(anyString())).thenReturn(tagResponseDto);

    GetPostResponseDto actual = postsService.getPostByUrl("url");

    assertEquals(responseDto.getBlogName(), actual.getBlogName());
    assertEquals(tagResponseDto.getFirst().getTagName(),
            actual.getTags().getFirst().getTagName());
  }

  @Test
  @DisplayName("Posts Builder 테스트")
  void postsBuilder() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = new Posts(category, "url", "subject", "summary", "thumb", "detail");

    assertEquals(posts.getCategory().getCategoryNo(), category.getCategoryNo());
  }

  @Test
  @DisplayName("글 수정페이지 정보 조회")
  void getPostForModifyByUrl() {
    GetPostForModifyResponseDto responseDto = new GetPostForModifyResponseDto();
    ReflectionTestUtils.setField(responseDto, "blogName", "blog");
    ReflectionTestUtils.setField(responseDto, "categoryName", "categoryName");
    ReflectionTestUtils.setField(responseDto, "title", "title");
    ReflectionTestUtils.setField(responseDto, "postUrl", "postUrl");
    ReflectionTestUtils.setField(responseDto, "thumbnailUrl", "thumbnailUrl");
    ReflectionTestUtils.setField(responseDto, "summary", "summary");
    ReflectionTestUtils.setField(responseDto, "detail", "detail");

    when(postsRepository.getPostForModifyByUrl(anyInt(), anyString()))
            .thenReturn(Optional.of(responseDto));

    when(tagRepository.getTagListByPost(anyString())).thenReturn(List.of(new GetTagResponseDto("tag")));

    GetPostForModifyResponseDto result = postsService.getPostForModifyByUrl(1, "postUrl");

    assertEquals(responseDto.getBlogName(), result.getBlogName());
    assertEquals(responseDto.getCategoryName(), result.getCategoryName());
    assertEquals(responseDto.getTitle(), result.getTitle());
    assertEquals(responseDto.getPostUrl(), result.getPostUrl());
    assertEquals(responseDto.getThumbnailUrl(), result.getThumbnailUrl());
    assertEquals(responseDto.getSummary(), result.getSummary());
    assertEquals(responseDto.getDetail(), result.getDetail());
    assertEquals(responseDto.getTags().getFirst(), result.getTags().getFirst());
  }

  @Test
  @DisplayName("글 수정 성공")
  void modifyPosts() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    List<PostsTag> postsTagList = List.of();
    ModifyPostsRequestDto requestDto = new ModifyPostsRequestDto();
    ReflectionTestUtils.setField(requestDto, "title", "title");
    ReflectionTestUtils.setField(requestDto, "summary", "summary");
    ReflectionTestUtils.setField(requestDto, "content", "content");
    ReflectionTestUtils.setField(requestDto, "thumb", "thumb");
    ReflectionTestUtils.setField(requestDto, "tags", List.of("tag"));

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));
    when(postsTagRepository.findByPostsNo(anyLong())).thenReturn(postsTagList);
    doNothing().when(postsTagRepository).delete(any());

    postsService.modifyPosts(1, "postKey", requestDto);

    verify(postsTagRepository, times(1)).deleteAll(any());

    assertEquals("title", posts.getSubject());
    assertEquals("summary", posts.getSummary());
  }

  @Test
  @DisplayName("글 수정 실패 - 다른 유저")
  void modifyPostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    List<PostsTag> postsTagList = List.of();
    ModifyPostsRequestDto requestDto = new ModifyPostsRequestDto();
    ReflectionTestUtils.setField(requestDto, "title", "title");
    ReflectionTestUtils.setField(requestDto, "summary", "summary");
    ReflectionTestUtils.setField(requestDto, "content", "content");
    ReflectionTestUtils.setField(requestDto, "thumb", "thumb");
    ReflectionTestUtils.setField(requestDto, "tags", List.of("tag"));
    ReflectionTestUtils.setField(member, "memberNo", 2);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));
    when(postsTagRepository.findByPostsNo(anyLong())).thenReturn(postsTagList);
    doNothing().when(postsTagRepository).delete(any());

    assertThrows(AuthorizationException.class,
            () -> postsService.modifyPosts(1, "postKey", requestDto));
  }

  @Test
  @DisplayName("글 삭제")
  void deletePosts() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    postsService.deletePosts(1, "postKey");

    assertTrue(posts.isDeleted());
  }

  @Test
  @DisplayName("글 삭제 실패 - 다른 유저")
  void deletePostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    ReflectionTestUtils.setField(member, "memberNo", 2);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    assertThrows(AuthorizationException.class,
            () -> postsService.deletePosts(1, "postKey"));
  }

  @Test
  @DisplayName("최근 글 조회")
  void getRecentPost() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    Pageable pageable = PageRequest.of(0, 10);
    Page<GetRecentPostResponseDto> page = new PageImpl<>(List.of(response), pageable, 1L);

    when(postsRepository.getRecentPosts(any())).thenReturn(page);

    Pages<GetRecentPostResponseDto> result = postsService.getRecentPost(pageable);
    GetRecentPostResponseDto resultDto = result.body().getFirst();

    assertEquals(resultDto.getTitle(), response.getTitle());
    assertEquals(resultDto.getSummary(), response.getSummary());
    assertFalse(result.hasNext());
    assertFalse(result.hasPrevious());
    assertEquals(1L, result.total());
  }

  @Test
  @DisplayName("회원 최근 글 조회")
  void getRecentPostByUsername() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    List<GetRecentPostResponseDto> list = List.of(response);

    when(postsRepository.getRecentPostByUsername(any(), anyString())).thenReturn(list);

    List<GetRecentPostResponseDto> result = postsService.getRecentPostByUsername("username");
    GetRecentPostResponseDto resultDto = result.getFirst();

    assertEquals(resultDto.getTitle(), response.getTitle());
    assertEquals(resultDto.getSummary(), response.getSummary());
  }

  @Test
  @DisplayName("블로그 최근 글 조회")
  void getRecentPostByBlog() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    List<GetRecentPostResponseDto> list = List.of(response);
    Pageable pageable = PageRequest.of(0, 4);

    Page<GetRecentPostResponseDto> page = new PageImpl<>(list, pageable, 1L);

    when(postsRepository.getRecentPostByBlog(any(), anyString())).thenReturn(page);

    List<GetRecentPostResponseDto> result = postsService.getRecentPostByBlog(pageable, "blogUrl").body();
    GetRecentPostResponseDto resultDto = result.getFirst();

    assertEquals(resultDto.getTitle(), response.getTitle());
    assertEquals(resultDto.getSummary(), response.getSummary());
  }

  @Test
  @DisplayName("카테고리 최근 글 조회")
  void getRecentPostByCategory() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    List<GetRecentPostResponseDto> list = List.of(response);
    Pageable pageable = PageRequest.of(0, 4);

    Page<GetRecentPostResponseDto> page = new PageImpl<>(list, pageable, 1L);

    when(postsRepository.getRecentPostByCategory(any(), anyString(), anyString())).thenReturn(page);

    List<GetRecentPostResponseDto> result = postsService.getRecentPostByCategory(pageable, "blogUrl", "category").body();
    GetRecentPostResponseDto resultDto = result.getFirst();

    assertEquals(resultDto.getTitle(), response.getTitle());
    assertEquals(resultDto.getSummary(), response.getSummary());
  }

  @Test
  @DisplayName("태그 최근 글 조회")
  void getRecentPostByTag() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    List<GetRecentPostResponseDto> list = List.of(response);
    Pageable pageable = PageRequest.of(0, 4);

    Page<GetRecentPostResponseDto> page = new PageImpl<>(list, pageable, 1L);

    when(postsRepository.getRecentPostsByTag(any(), anyString(), anyString())).thenReturn(page);

    List<GetRecentPostResponseDto> result = postsService.getRecentPostByTag(pageable, "blogUrl", "tag").body();
    GetRecentPostResponseDto resultDto = result.getFirst();

    assertEquals(resultDto.getTitle(), response.getTitle());
    assertEquals(resultDto.getSummary(), response.getSummary());
  }

  @Test
  @DisplayName("인기 글 조회")
  void getPopularPostsByBlog() {
    GetPopularPostResponseDto response = new GetPopularPostResponseDto(
            "url", "thumb", "title", "summary", 0L, 0L);

    when(postsRepository.getPopularPostsByBlog(anyString())).thenReturn(List.of(response));

    List<GetPopularPostResponseDto> list = postsService.getPopularPostsByBlog("blogUrl");

    assertFalse(list.isEmpty());

    GetPopularPostResponseDto result = list.getFirst();

    assertEquals(result.getTitle(), response.getTitle());
    assertEquals(result.getSummary(), response.getSummary());
  }

  @Test
  @DisplayName("게시물 비공개")
  void closePosts() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    postsService.closePosts(member.getMemberNo(), posts.getUrl());

    assertFalse(posts.isOpen());
  }

  @Test
  @DisplayName("게시물 비공개 실패")
  void closePostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    String url = posts.getUrl();

    assertThrows(AuthorizationException.class,
            () -> postsService.closePosts(3, url));
  }

  @Test
  @DisplayName("게시물 공개")
  void openPosts() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    postsService.openPosts(member.getMemberNo(), posts.getUrl());

    assertTrue(posts.isOpen());
  }

  @Test
  @DisplayName("게시물 공개 실패")
  void openPostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));

    String url = posts.getUrl();

    assertThrows(AuthorizationException.class,
            () -> postsService.openPosts(3, url));
  }

  @Test
  @DisplayName("작성한 전체 게시글 조회")
  void getPostsByMemberNo() {
    GetPostManageResponseDto responseDto =
            new GetPostManageResponseDto("blog",
                    "categpru",
                    "post",
                    "title",
                    "thumb",
                    LocalDateTime.now(),
                    true);
    Pageable pageable = PageRequest.of(0, 4);

    when(postsRepository.getPostsByMemberNo(any(), anyInt()))
            .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

    Pages<GetPostManageResponseDto> result = postsService.getPostsByMemberNo(pageable, 1);

    assertEquals(1L, result.total());

    GetPostManageResponseDto actual = result.body().getFirst();

    assertEquals(responseDto.getPostTitle(), actual.getPostTitle());
  }

  @Test
  @DisplayName("좋아요 표시한 게시글 조회")
  void getPostsByHearts() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    List<GetRecentPostResponseDto> list = List.of(response);
    Pageable pageable = PageRequest.of(0, 4);

    Page<GetRecentPostResponseDto> page = new PageImpl<>(list, pageable, 1L);

    when(postsRepository.getPostsByHearts(anyInt(), any()))
            .thenReturn(page);

    Pages<GetRecentPostResponseDto> result = postsService.getPostsByHearts(1, pageable);

    assertEquals(1L, page.getTotalElements());

    GetRecentPostResponseDto actual = result.body().getFirst();

    assertEquals(response.getTitle(), actual.getTitle());
  }

  @Test
  @DisplayName("최근 활동 조회")
  void getPostActivity() {
    LocalDate now = LocalDate.now();
    GetPostActivityResponseDto responseDto =
            new GetPostActivityResponseDto(now, 1L);

    when(postsRepository.getPostActivity(anyString(), any(), any()))
            .thenReturn(List.of(responseDto));

    Map<String, List<GetPostActivityResponseDto>> activities =
            postsService.getPostActivity("username");

    assertFalse(activities.isEmpty());

    GetPostActivityResponseDto actual = activities.get(now.getDayOfWeek().name()).getLast();
    assertEquals(responseDto.getDate(),actual.getDate());
    assertEquals(responseDto.getCount(), actual.getCount());
  }

  @Test
  @DisplayName("최근 활동 조회 - 시작일이 일요일 아닌 경우")
  void getPostActivityNotSunday() {
    LocalDate now = LocalDate.now();
    GetPostActivityResponseDto responseDto =
            new GetPostActivityResponseDto(now, 1L);

    when(postsRepository.getPostActivity(anyString(), any(), any()))
            .thenReturn(List.of(responseDto));

    Map<String, List<GetPostActivityResponseDto>> activities =
            postsService.getPostActivity("username");

    assertFalse(activities.isEmpty());

    GetPostActivityResponseDto actual = activities.get(now.getDayOfWeek().name()).getLast();
    assertEquals(responseDto.getDate(),actual.getDate());
    assertEquals(responseDto.getCount(), actual.getCount());

    GetPostActivityResponseDto sundays = activities.get(DayOfWeek.SUNDAY.name()).getFirst();
    assertEquals(DayOfWeek.SUNDAY, sundays.getDate().getDayOfWeek());
  }

  @Test
  @DisplayName("게시글 검색")
  void searchPosts() {
    GetRecentPostResponseDto response = new GetRecentPostResponseDto(
            "blogUrl", "blogName", "username",
            "blogPfp", "postUrl", "title",
            "summary", "thumb", LocalDateTime.now(),
            0L, 0L);
    Pageable pageable = PageRequest.of(0, 4);
    String word = "word";

    when(postsRepository.searchPosts(any(), anyString()))
            .thenReturn(new PageImpl<>(List.of(response)));

    Pages<GetRecentPostResponseDto> result = postsService.searchPosts(pageable, word);

    List<GetRecentPostResponseDto> resultList = result.body();

    assertEquals(1L, resultList.size());

    GetRecentPostResponseDto actual = resultList.getFirst();

    assertEquals(response.getTitle(), actual.getTitle());
  }

  @Test
  @DisplayName("피드 조회")
  void feed() {
    GetFeedPostsResponseDto expect = new GetFeedPostsResponseDto(
            "username",
            "title",
            LocalDateTime.now(),
            null,
            "details",
            "postUrl",
            "blogUrl",
            "blogName",
            "blogThumb");
    Page<GetFeedPostsResponseDto> expectPage = new PageImpl<>(List.of(expect));
    Pageable pageable = PageRequest.of(0, 4);

    when(postsRepository.getFeedStartPostsNoByMemberNo(anyInt())).thenReturn(1L);
    when(postsRepository.getFeedPostsByMemberNo(anyInt(), any(), any())).thenReturn(expectPage);

    GetFeedPostsPagesResponseDto actual = postsService.feed(1, null, pageable);

    assertEquals(1L, actual.getStart());
    assertEquals(expect.getUsername(), actual.getPages().body().getFirst().getUsername());
  }
}