package com.blogitory.blog.tempposts.service.impl;

import static com.blogitory.blog.posts.service.impl.PostsServiceImpl.POST_KEY;

import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.exception.PostsJsonConvertException;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import com.blogitory.blog.tempposts.service.TempPostsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Temp posts service implementation.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class TempPostsServiceImpl implements TempPostsService {
  private final TempPostsRepository tempPostsRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GetTempPostsResponseDto> getTempPostsListByMemberNo(Integer memberNo) {
    List<GetTempPostsResponseDto> responseDtoList =  tempPostsRepository
            .getTempPostsByMemberNo(memberNo);
    HashOperations<String, String, Object> operations = redisTemplate.opsForHash();

    responseDtoList.forEach(tempPost -> {
      String tp = (String) operations.get(POST_KEY, tempPost.getTempPostsId());

      String title = "";

      try {
        if (tp != null) {
          title = objectMapper.readValue(tp, SaveTempPostsDto.class).getTitle();
        }
      } catch (JsonProcessingException e) {
        throw new PostsJsonConvertException();
      }

      tempPost.setTempPostsTitle(title);
    });

    return responseDtoList;
  }
}
