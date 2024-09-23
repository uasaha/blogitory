package com.blogitory.blog.visitant.service.impl;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.visitant.dto.GetVisitantCountResponseDto;
import com.blogitory.blog.visitant.dto.VisitantInfoDto;
import com.blogitory.blog.visitant.entity.Visitant;
import com.blogitory.blog.visitant.repository.VisitantRepository;
import com.blogitory.blog.visitant.service.VisitantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * Implementation of Visitant service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VisitantServiceImpl implements VisitantService {
  private final VisitantRepository visitantRepository;
  private final BlogRepository blogRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  private static final String VISITANT_KEY = "bv";
  private static final String TODAY_KEY = "today";
  private static final String TOTAL_KEY = "total";

  /**
   * {@inheritDoc}
   */
  @Override
  public void viewBlogs(String blogUrl, Integer memberNo, String ip) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Set<VisitantInfoDto> visitants = getVisitants(blogUrl);

    VisitantInfoDto infoDto = new VisitantInfoDto(memberNo, ip);
    visitants.add(infoDto);

    try {
      String value = objectMapper.writeValueAsString(visitants);
      hashOperations.put(VISITANT_KEY, blogUrl, value);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Map<String, Integer> getVisitantCount(String blogUrl) {
    Set<VisitantInfoDto> visitants = getVisitants(blogUrl);

    Integer totalCount = visitantRepository.getCountByBlogUrl(blogUrl);
    Integer todayCount = visitantRepository.getCountByBlogUrlAndDate(blogUrl, LocalDate.now());

    if (Objects.isNull(totalCount)) {
      totalCount = 0;
    }

    if (Objects.isNull(todayCount)) {
      todayCount = 0;
    }

    Integer total = Math.addExact(totalCount, visitants.size());
    Integer today = Math.addExact(todayCount, visitants.size());

    return Map.of(TODAY_KEY, today, TOTAL_KEY, total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persistence() throws JsonProcessingException {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Set<String> keys = hashOperations.keys(VISITANT_KEY);

    for (String key : keys) {
      String visitantString = hashOperations.get(VISITANT_KEY, key);
      Set<VisitantInfoDto> visitants = objectMapper.readValue(visitantString, Set.class);

      Blog blog = blogRepository.findBlogByUrlName(key)
              .orElse(null);

      if (Objects.nonNull(blog)) {
        Visitant visitant = visitantRepository
                .findByBlogUrlAndDate(blog.getUrlName(), LocalDate.now())
                .orElse(new Visitant(blog, 0));

        visitant.sync(visitants.size());

        visitantRepository.save(visitant);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveAndDelete() throws JsonProcessingException {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Set<String> keys = hashOperations.keys(VISITANT_KEY);

    for (String key : keys) {
      String visitantString = hashOperations.get(VISITANT_KEY, key);
      Set<VisitantInfoDto> visitants = objectMapper.readValue(visitantString, Set.class);

      Blog blog = blogRepository.findBlogByUrlName(key)
              .orElse(null);

      if (Objects.nonNull(blog)) {
        Visitant visitant = visitantRepository
                .findByBlogUrlAndDate(blog.getUrlName(), LocalDate.now())
                .orElse(new Visitant(blog, 0));

        hashOperations.delete(VISITANT_KEY, key);

        visitant.sync(visitants.size());

        visitantRepository.save(visitant);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public List<GetVisitantCountResponseDto> getVisitantMonthlyCount(
          Integer memberNo, String blogUrl) {
    Blog blog = blogRepository.findBlogByUrlName(blogUrl)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    LocalDate today = LocalDate.now();
    LocalDate start = today.minusDays(30L);

    List<GetVisitantCountResponseDto> visitantCountList =
            visitantRepository.getCountsByBlogUrl(blogUrl, start, today);

    List<GetVisitantCountResponseDto> visitantCountSet = new ArrayList<>();

    for (LocalDate now = start; now.isBefore(today); now = now.plusDays(1)) {
      LocalDate nowDate = now;
      Optional<GetVisitantCountResponseDto> countOptional = visitantCountList
              .stream().filter(c -> nowDate.isEqual(c.getDate())).findFirst();

      visitantCountSet.add(countOptional.orElse(new GetVisitantCountResponseDto(now, 0)));
    }

    return visitantCountSet;
  }

  private Set<VisitantInfoDto> getVisitants(String blogUrl) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String visitantString = hashOperations.get(VISITANT_KEY, blogUrl);
    Set<VisitantInfoDto> visitants = null;

    try {
      visitants = objectMapper.readValue(visitantString, Set.class);
    } catch (IllegalArgumentException | JsonProcessingException e) {
      log.error(e.getMessage());
    }

    return Objects.isNull(visitants) ? new HashSet<>() : visitants;
  }
}
