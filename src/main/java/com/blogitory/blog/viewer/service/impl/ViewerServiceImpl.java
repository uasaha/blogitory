package com.blogitory.blog.viewer.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.viewer.dto.GetViewerCountResponseDto;
import com.blogitory.blog.viewer.dto.ViewerInfoDto;
import com.blogitory.blog.viewer.entity.Viewer;
import com.blogitory.blog.viewer.repository.ViewerRepository;
import com.blogitory.blog.viewer.service.ViewerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
    Set<ViewerInfoDto> viewers = getViewers(postsUrl);

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
  public Integer getViewersCount(Integer memberNo, String postsUrl) {
    Posts posts = postsRepository.findByUrl(postsUrl)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    if (!posts.getCategory().getBlog().getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    Set<ViewerInfoDto> viewers = getViewers(postsUrl);

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

      Posts posts = postsRepository.findByUrl(key)
              .orElse(null);

      if (Objects.nonNull(posts)) {
        Viewer viewer = viewerRepository.findByPostsUrlAndDate(posts.getUrl(), LocalDate.now())
                .orElse(new Viewer(posts, 0));

        viewer.addCount(viewers.size());

        viewerRepository.save(viewer);
        hashOperations.delete(VIEWER_KEY, key);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public List<GetViewerCountResponseDto> getViewerMonthlyCount(Integer memberNo, String postsUrl) {
    Posts posts = postsRepository.findByUrl(postsUrl)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    if (!memberNo.equals(posts.getCategory().getBlog().getMember().getMemberNo())) {
      throw new AuthorizationException();
    }

    LocalDate today = LocalDate.now();
    LocalDate start = today.minusDays(30L);

    List<GetViewerCountResponseDto> viewerCountList =
            viewerRepository.getCountsByPostUrl(postsUrl, start, today);

    List<GetViewerCountResponseDto> result = new ArrayList<>();

    for (LocalDate now = start; now.isBefore(today); now = now.plusDays(1)) {
      LocalDate nowDate = now;
      Optional<GetViewerCountResponseDto> countOptional = viewerCountList
              .stream().filter(c -> nowDate.isEqual(c.getDate())).findFirst();

      result.add(countOptional.orElse(new GetViewerCountResponseDto(now, 0)));
    }

    return result;
  }

  private Set<ViewerInfoDto> getViewers(String postsUrl) {
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

    return Objects.isNull(viewers) ? new HashSet<>() : viewers;
  }
}