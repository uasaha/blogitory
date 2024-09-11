package com.blogitory.blog.viewer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.viewer.dto.ViewerInfoDto;
import com.blogitory.blog.viewer.repository.ViewerRepository;
import com.blogitory.blog.viewer.service.impl.ViewerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * ViewerServiceTest.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
class ViewerServiceTest {
  PostsRepository postsRepository;
  ViewerRepository viewerRepository;
  ViewerService viewerService;
  ObjectMapper objectMapper;
  RedisTemplate<String, Object> redisTemplate;

  @BeforeEach
  void setUp() {
    postsRepository = mock(PostsRepository.class);
    objectMapper = mock(ObjectMapper.class);
    redisTemplate = mock(RedisTemplate.class);
    viewerRepository = mock(ViewerRepository.class);

    viewerService = new ViewerServiceImpl(postsRepository, viewerRepository, objectMapper, redisTemplate);
  }

  @Test
  void viewPosts() {
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(null);
    doNothing().when(hashOperations).put(anyString(), anyString(), anyString());

    viewerService.viewPosts("@posts/posts/posts", 1, "127.0.0.1");

    verify(hashOperations, times(1)).put(any(), any(), any());
  }

  @Test
  void getViewersCount() {
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(any(), anyString())).thenReturn(null);
    when(viewerRepository.getCountByPostsUrl(anyString())).thenReturn(null);

    int actual = viewerService.getViewersCount("@posts/posts/posts");

    assertEquals(0, actual);
  }

  @Test
  void persistence() throws JsonProcessingException {
    String infoString = "{ \"memberNo\" : 1, \"ip\" : \"127.0.0.1\" }";
    ViewerInfoDto infoDto = new ViewerInfoDto(1, "127.0.0.1");
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.keys(any())).thenReturn(Set.of("posts"));
    when(hashOperations.get(anyString(), anyString())).thenReturn(infoString);
    when(objectMapper.readValue(anyString(), (Class<Object>) any())).thenReturn(Set.of(infoDto));
    when(hashOperations.delete(any(), anyString())).thenReturn(1L);
    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));
    when(viewerRepository.findByPostsUrlAndDate(anyString(), any())).thenReturn(Optional.empty());

    viewerService.persistence();

    verify(viewerRepository, times(1)).save(any());
  }
}