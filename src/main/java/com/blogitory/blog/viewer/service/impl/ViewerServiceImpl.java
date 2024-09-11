package com.blogitory.blog.viewer.service.impl;

import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.viewer.dto.ViewerInfoDto;
import com.blogitory.blog.viewer.entity.Viewer;
import com.blogitory.blog.viewer.repository.ViewerRepository;
import com.blogitory.blog.viewer.service.ViewerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Viewer service.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ViewerServiceImpl implements ViewerService {
  private final PostsRepository postsRepository;
  private final ViewerRepository viewerRepository;
  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;
  private static final String VIEWER_KEY = "pv";

  /**
   * {@inheritDoc}
   */
  @Override
  public void viewPosts(String postsUrl, Integer memberNo, String ip) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

    String viewersString = hashOperations.get(VIEWER_KEY, postsUrl);
    Set<ViewerInfoDto> viewers = null;

    try {
      viewers = objectMapper.readValue(viewersString, Set.class);
    } catch (IllegalArgumentException e) {
      viewers = new HashSet<>();
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }

    if (Objects.isNull(viewers)) {
      viewers = new HashSet<>();
    }

    ViewerInfoDto viewerInfoDto = new ViewerInfoDto(memberNo, ip);
    viewers.add(viewerInfoDto);

    try {
      String value = objectMapper.writeValueAsString(viewers);
      hashOperations.put(VIEWER_KEY, postsUrl, value);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Integer getViewersCount(String postsUrl) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

    String viewersString = hashOperations.get(VIEWER_KEY, postsUrl);
    Set<ViewerInfoDto> viewers = null;

    try {
      viewers = objectMapper.readValue(viewersString, Set.class);
    } catch (IllegalArgumentException e) {
      viewers = new HashSet<>();
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }

    if (Objects.isNull(viewers)) {
      viewers = new HashSet<>();
    }

    Integer view = viewerRepository.getCountByPostsUrl(postsUrl);

    if (view == null) {
      view = 0;
    }

    return Math.addExact(view, viewers.size());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persistence() throws JsonProcessingException {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Set<String> keys = hashOperations.keys(VIEWER_KEY);

    for (String key : keys) {
      String viewersString = hashOperations.get(VIEWER_KEY, key);
      Set<ViewerInfoDto> viewers = objectMapper.readValue(viewersString, Set.class);
      hashOperations.delete(VIEWER_KEY, key);

      Posts posts = postsRepository.findByUrl(key)
              .orElseThrow();

      Viewer viewer = viewerRepository.findByPostsUrlAndDate(posts.getUrl(), LocalDate.now())
              .orElse(new Viewer(posts, 0));

      viewer.addCount(viewers.size());

      viewerRepository.save(viewer);
    }
  }
}
