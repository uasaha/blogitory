package com.blogitory.blog.posts.service.impl;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.dto.request.ModifyPostsRequestDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.exception.InvalidPostsUrlException;
import com.blogitory.blog.posts.exception.PostsJsonConvertException;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.posts.service.PostsService;
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
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PostsService.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {
  private final MemberRepository memberRepository;
  private final PostsRepository postsRepository;
  private final TempPostsRepository tempPostsRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;
  private final PostsTagRepository postsTagRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  public static final String POST_KEY = "temp_post";
  private static final String URL_REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+$";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTempPostsId(Integer memberNo) {
    UUID uuid = UUID.randomUUID();

    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    List<TempPosts> tempPostsList =
            tempPostsRepository.findAllByMemberMemberNoOrderByCreatedAtDesc(memberNo);

    if (tempPostsList.size() >= 10) {
      deleteTempPosts(memberNo, tempPostsList.getLast().getTempPostsId().toString());
    }

    TempPosts tempPosts = new TempPosts(uuid, member);

    tempPosts = tempPostsRepository.save(tempPosts);

    SaveTempPostsDto saveDto = new SaveTempPostsDto();
    saveDto.setMemberNo(memberNo);

    String saved;

    try {
      saved = objectMapper.writeValueAsString(saveDto);
    } catch (JsonProcessingException e) {
      throw new PostsJsonConvertException();
    }

    redisTemplate.opsForHash().put(POST_KEY, uuid.toString(), saved);

    return tempPosts.getTempPostsId().toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SaveTempPostsDto loadTempPosts(String id, Integer memberNo) {
    String saveData =
            (String) redisTemplate.opsForHash().get(POST_KEY, id);

    SaveTempPostsDto saveDto;

    try {
      saveDto = objectMapper.readValue(saveData, SaveTempPostsDto.class);
    } catch (JsonProcessingException e) {
      throw new PostsJsonConvertException();
    }

    if (!saveDto.getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    return saveDto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveTempPosts(String id, SaveTempPostsDto saveDto, Integer memberNo) {
    TempPosts tempPosts = tempPostsRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException(TempPosts.class));

    tempPosts.updateCreateAt();
    saveDto.setMemberNo(memberNo);

    String saved;

    try {
      saved = objectMapper.writeValueAsString(saveDto);
    } catch (JsonProcessingException e) {
      throw new PostsJsonConvertException();
    }

    redisTemplate.opsForHash().put(POST_KEY, id, saved);


  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CreatePostsResponseDto createPosts(String tp, Integer memberNo, SaveTempPostsDto saveDto) {
    Category category = categoryRepository.findById(saveDto.getCategoryNo())
            .orElseThrow(() -> new NotFoundException(Category.class));

    Blog blog = category.getBlog();

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    String url = makeUrl(blog, saveDto);

    if (isDuplicateUrl(url)) {
      String uuid = UUID.randomUUID().toString();
      String added = uuid.split("-")[3];
      url = url + added;
    }

    Posts posts = Posts.builder()
            .category(category)
            .url(url)
            .subject(saveDto.getTitle())
            .summary(saveDto.getSummary())
            .thumbnail(saveDto.getThumbnailUrl())
            .detail(saveDto.getDetails())
            .build();

    posts = postsRepository.save(posts);

    connectTags(blog, posts, saveDto.getTags());

    deleteTempPosts(memberNo, tp);

    return new CreatePostsResponseDto(posts.getUrl());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTempPosts(Integer memberNo, String tp) {
    redisTemplate.opsForHash().delete(POST_KEY, tp);
    TempPosts tempPosts = tempPostsRepository.findById(UUID.fromString(tp))
            .orElseThrow(() -> new NotFoundException(TempPosts.class));

    if (!tempPosts.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    tempPostsRepository.delete(tempPosts);
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public GetPostResponseDto getPostByUrl(String postUrl) {
    GetPostResponseDto responseDto = postsRepository.getPostByPostUrl(postUrl)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    List<GetTagResponseDto> tags = tagRepository.getTagListByPost(postUrl);
    responseDto.setTags(tags);

    return responseDto;
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public GetPostForModifyResponseDto getPostForModifyByUrl(Integer memberNo, String postUrl) {
    GetPostForModifyResponseDto responseDto =
            postsRepository.getPostForModifyByUrl(memberNo, postUrl)
                    .orElseThrow(() -> new NotFoundException(Posts.class));

    responseDto.tags(tagRepository.getTagListByPost(postUrl));

    return responseDto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void modifyPosts(Integer memberNo, String postKey, ModifyPostsRequestDto requestDto) {
    Posts posts = postsRepository.findByUrl(postKey)
            .orElseThrow(() -> new NotFoundException(Posts.class));
    Category category = posts.getCategory();
    Blog blog = category.getBlog();

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    posts.modify(requestDto.getTitle(),
            requestDto.getSummary(),
            requestDto.getContent(),
            requestDto.getThumb());

    List<PostsTag> postsTagList = postsTagRepository.findByPostsNo(posts.getPostsNo());

    postsTagRepository.deleteAll(postsTagList);

    connectTags(blog, posts, requestDto.getTags());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deletePosts(Integer memberNo, String postKey) {
    Posts posts = postsRepository.findByUrl(postKey)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    Category category = posts.getCategory();
    Blog blog = category.getBlog();

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    posts.delete();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetRecentPostResponseDto> getRecentPost(Pageable pageable) {
    Page<GetRecentPostResponseDto> result = postsRepository.getRecentPosts(pageable);

    return new Pages<>(result.getContent(),
            pageable.getPageNumber(),
            result.hasPrevious(),
            result.hasNext(),
            result.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public List<GetRecentPostResponseDto> getRecentPostByUsername(String username) {
    Pageable pageable = PageRequest.of(0, 4);

    return postsRepository.getRecentPostByUsername(pageable, username);
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetRecentPostResponseDto> getRecentPostByBlog(Pageable pageable, String blogUrl) {
    Page<GetRecentPostResponseDto> result = postsRepository.getRecentPostByBlog(pageable, blogUrl);

    return new Pages<>(result.getContent(),
            pageable.getPageNumber(),
            result.hasPrevious(),
            result.hasNext(),
            result.getTotalElements());
  }

  @Override
  public List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl) {
    return postsRepository.getPopularPostsByBlog(blogUrl);
  }

  /**
   * Connect tag to blog.
   *
   * @param blog  blog
   * @param posts posts
   * @param tags  tags
   */
  private void connectTags(Blog blog, Posts posts, List<String> tags) {
    for (String tagName : tags) {
      Tag tag = tagRepository.findByName(tagName)
              .orElse(new Tag(tagName));

      tag = tagRepository.save(tag);

      PostsTag postsTag = new PostsTag(null, tag, posts, blog);

      postsTagRepository.save(postsTag);
    }
  }

  /**
   * make posts url.
   */
  private String makeUrl(Blog blog, SaveTempPostsDto saveDto) {
    String url = blog.getUrlName() + "/" + saveDto.getUrl();

    if (saveDto.getUrl() == null || saveDto.getUrl().isEmpty()) {
      String urlTitle = saveDto.getTitle().replace(" ", "-");
      url = blog.getUrlName() + "/" + urlTitle;
    } else {
      if (!Pattern.matches(URL_REGEX, saveDto.getUrl())) {
        throw new InvalidPostsUrlException();
      }
    }

    return url;
  }

  /**
   * is url duplicated.
   */
  private boolean isDuplicateUrl(String url) {
    return postsRepository.existsByUrl(url);
  }
}
