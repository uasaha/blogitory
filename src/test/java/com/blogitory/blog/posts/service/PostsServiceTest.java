package com.blogitory.blog.posts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.exception.InvalidPostsUrlException;
import com.blogitory.blog.posts.exception.PostsJsonConvertException;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.repository.PostsTagRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.repository.TagRepository;
import com.blogitory.blog.tempposts.entity.TempPosts;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Posts service test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
class PostsServiceTest {
  MemberRepository memberRepository;
  PostsRepository postsRepository;
  TempPostsRepository tempPostsRepository;
  CategoryRepository categoryRepository;
  TagRepository tagRepository;
  PostsTagRepository postsTagRepository;
  RedisTemplate<String, Object> redisTemplate;
  ObjectMapper objectMapper;
  PostsService postsService;

  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    postsRepository = mock(PostsRepository.class);
    tempPostsRepository = mock(TempPostsRepository.class);
    categoryRepository = mock(CategoryRepository.class);
    tagRepository = mock(TagRepository.class);
    postsTagRepository = mock(PostsTagRepository.class);
    redisTemplate = mock(RedisTemplate.class);
    objectMapper = mock(ObjectMapper.class);

    postsService = new PostsServiceImpl(
            memberRepository,
            postsRepository,
            tempPostsRepository,
            categoryRepository,
            tagRepository,
            postsTagRepository,
            redisTemplate,
            objectMapper);
  }

  @Test
  @DisplayName("임시 게시물 ID 발급")
  void getTempPostsId() throws Exception {
    Member member = MemberDummy.dummy();

    UUID uuid = UUID.randomUUID();
    TempPosts tempPosts = new TempPosts(uuid, member);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(tempPostsRepository.save(any())).thenReturn(tempPosts);
    when(objectMapper.writeValueAsString(any())).thenReturn("dto");
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    doNothing().when(hashOperations).put(anyString(), anyString(), any());

    String actual = postsService.getTempPostsId(member.getMemberNo());

    assertEquals(uuid.toString(), actual);
  }

  @Test
  @DisplayName("임시 게시물 ID 발급 실패")
  void getTempPostsIdFailed() throws Exception {
    Member member = MemberDummy.dummy();

    UUID uuid = UUID.randomUUID();
    TempPosts tempPosts = new TempPosts(uuid, member);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(tempPostsRepository.save(any())).thenReturn(tempPosts);
    when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    doNothing().when(hashOperations).put(anyString(), anyString(), any());

    assertThrows(PostsJsonConvertException.class,
            () -> postsService.getTempPostsId(1));
  }

  @Test
  @DisplayName("임시게시물 로드 성공")
  void loadTempPosts() throws Exception {
    String saveData = "dto";
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(saveData);
    when(objectMapper.readValue(saveData, SaveTempPostsDto.class))
            .thenReturn(postsDto);

    SaveTempPostsDto actual = postsService.loadTempPosts("tpid", 1);

    assertEquals(postsDto.getMemberNo(), actual.getMemberNo());
    assertEquals(postsDto.getTitle(), actual.getTitle());
    assertEquals(postsDto.getUrl(), actual.getUrl());
    assertEquals(postsDto.getSummary(), actual.getSummary());
  }

  @Test
  @DisplayName("임시게시물 로드 실패 - JSON 변환")
  void loadTempPostsFailedConvertJson() throws Exception {
    String saveData = "dto";
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(saveData);
    when(objectMapper.readValue(saveData, SaveTempPostsDto.class))
            .thenThrow(JsonProcessingException.class);

    assertThrows(PostsJsonConvertException.class, () -> postsService.loadTempPosts("tpid", 1));
  }

  @Test
  @DisplayName("임시게시물 로드 실패 - 다른 유저")
  void loadTempPostsFailedAuthorization() throws Exception {
    String saveData = "dto";
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(saveData);
    when(objectMapper.readValue(saveData, SaveTempPostsDto.class))
            .thenReturn(postsDto);

    assertThrows(AuthorizationException.class, () -> postsService.loadTempPosts("tpid", 2));
  }


  @Test
  @DisplayName("임시게시물 저장")
  void saveTempPosts() throws Exception {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    TempPosts tempPosts = new TempPosts(uuid, member);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());
    String saved = "saved";

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    when(objectMapper.writeValueAsString(any(SaveTempPostsDto.class))).thenReturn(saved);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    doNothing().when(hashOperations).put(anyString(), anyString(), any());

    postsService.saveTempPosts(uuid.toString(), postsDto, 1);

    assertEquals(1, postsDto.getMemberNo());
  }

  @Test
  @DisplayName("임시게시물 저장 실패 - JSON")
  void saveTempPostsFailed() throws Exception {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    TempPosts tempPosts = new TempPosts(uuid, member);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());

    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    when(objectMapper.writeValueAsString(any(SaveTempPostsDto.class))).thenThrow(JsonProcessingException.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    doNothing().when(hashOperations).put(anyString(), anyString(), any());

    String uuidString = uuid.toString();

    assertThrows(PostsJsonConvertException.class,
            () -> postsService.saveTempPosts(uuidString, postsDto, 1));
  }

  @Test
  @DisplayName("게시글 저장 성공")
  void createPosts() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of("tag"));
    Posts posts = PostsDummy.dummy(category);
    Tag tag = new Tag(1L, "tag", false);
    PostsTag postsTag = new PostsTag(1L, tag, posts, blog);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
    when(postsRepository.existsByUrl(anyString())).thenReturn(false);
    when(postsRepository.save(any(Posts.class))).thenReturn(posts);
    when(tagRepository.findByName(anyString()))
            .thenReturn(Optional.of(tag));
    when(tagRepository.save(any(Tag.class))).thenReturn(tag);
    when(postsTagRepository.save(any(PostsTag.class))).thenReturn(postsTag);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    doNothing().when(tempPostsRepository).delete(any());

    CreatePostsResponseDto responseDto =
            postsService.createPosts(uuid.toString(), 1, postsDto);

    assertEquals("posts_url", responseDto.getPostsUrl());
  }

  @Test
  @DisplayName("게시물 저장 실패 - 다른 유저")
  void createPostsFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

    assertThrows(AuthorizationException.class, () -> postsService.createPosts("up", 0, postsDto));
  }

  @Test
  @DisplayName("게시글 저장 성공 - url 중복")
  void createPostsDuplicateUrl() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of("tag"));
    Posts posts = PostsDummy.dummy(category);

    Tag tag = new Tag(1L, "tag", false);
    PostsTag postsTag = new PostsTag(1L, tag, posts, blog);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
    when(postsRepository.existsByUrl(anyString())).thenReturn(true);
    when(postsRepository.save(any(Posts.class))).thenReturn(posts);
    when(tagRepository.findByName(anyString()))
            .thenReturn(Optional.of(tag));
    when(tagRepository.save(any(Tag.class))).thenReturn(tag);
    when(postsTagRepository.save(any(PostsTag.class))).thenReturn(postsTag);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    doNothing().when(tempPostsRepository).delete(any());

    CreatePostsResponseDto responseDto =
            postsService.createPosts(uuid.toString(), 1, postsDto);

    assertTrue(responseDto.getPostsUrl().startsWith("posts_url"));
  }

  @Test
  @DisplayName("게시글 저장 성공 - url 미지정")
  void createPostsSuccessNullUrl() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "",
            "summary",
            "thumb",
            "details",
            List.of("tag"));
    Posts posts = PostsDummy.dummy(category);
    Tag tag = new Tag(1L, "tag", false);
    PostsTag postsTag = new PostsTag(1L, tag, posts, blog);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
    when(postsRepository.existsByUrl(anyString())).thenReturn(false);
    when(postsRepository.save(any(Posts.class))).thenReturn(posts);
    when(tagRepository.findByName(anyString()))
            .thenReturn(Optional.of(tag));
    when(tagRepository.save(any(Tag.class))).thenReturn(tag);
    when(postsTagRepository.save(any(PostsTag.class))).thenReturn(postsTag);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));
    doNothing().when(tempPostsRepository).delete(any());

    CreatePostsResponseDto responseDto =
            postsService.createPosts(uuid.toString(), 1, postsDto);

    assertEquals("posts_url", responseDto.getPostsUrl());
  }

  @Test
  @DisplayName("게시글 저장 실패 - url 패턴 미일치")
  void createPostsFailInvalidUrl() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "!!@#!@%@",
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

    String uuidString = uuid.toString();

    assertThrows(InvalidPostsUrlException.class, () -> postsService.createPosts(uuidString, 1, postsDto));
  }

  @Test
  @DisplayName("임시 게시물 삭제 성공")
  void deleteTempPosts() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
    when(tempPostsRepository.findById(any())).thenReturn(Optional.of(tempPosts));

    postsService.deleteTempPosts(1, uuid.toString());

    verify(tempPostsRepository).delete(any());
  }

  @Test
  @DisplayName("임시 게시물 삭제 실패")
  void deleteTempPostsFailed() {
    UUID uuid = UUID.randomUUID();
    Member member = MemberDummy.dummy();
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    TempPosts tempPosts = new TempPosts(uuid, member);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
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
}