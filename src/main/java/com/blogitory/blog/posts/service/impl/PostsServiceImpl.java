package com.blogitory.blog.posts.service.impl;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.commons.listener.event.NewPostsNoticeEvent;
import com.blogitory.blog.commons.utils.PostsUtils;
import com.blogitory.blog.member.entity.Member;
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
import com.blogitory.blog.posts.exception.InvalidPostsUrlException;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PostsService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {
  private final ApplicationEventPublisher eventPublisher;
  private final MemberRepository memberRepository;
  private final PostsRepository postsRepository;
  private final TempPostsRepository tempPostsRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;
  private final PostsTagRepository postsTagRepository;
  private static final String URL_REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+$";
  private static final String TITLE_URL_REGEX = "[^ㄱ-ㅎ가-힣a-zA-Z0-9\\s]";

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

    return tempPosts.getTempPostsId().toString();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public SaveTempPostsDto loadTempPosts(String id, Integer memberNo) {
    TempPosts tempPosts = tempPostsRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException(TempPosts.class));

    if (!tempPosts.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    List<String> tags = new ArrayList<>();

    if (Objects.nonNull(tempPosts.getTags())) {
      tags = Arrays.stream(tempPosts.getTags().split(",")).toList();

    }

    return new SaveTempPostsDto(
            tempPosts.getBlogNo(),
            null,
            tempPosts.getTitle(),
            tempPosts.getCategoryNo(),
            tempPosts.getSummary(),
            tempPosts.getThumbnailUrl(),
            tempPosts.getDetails(),
            tags);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveTempPosts(String id, SaveTempPostsDto saveDto, Integer memberNo) {
    TempPosts tempPosts = tempPostsRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException(TempPosts.class));

    if (!tempPosts.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    tempPosts.updateTempPosts(saveDto);
  }

  /**
   * {@inheritDoc}
   */
  @Caching(evict = {
      @CacheEvict(cacheNames = "recent-post", allEntries = true),
      @CacheEvict(cacheNames = "posts-activity", allEntries = true)
  })
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

    String summary = saveDto.getSummary().isEmpty()
            ? PostsUtils.extractSummary(saveDto.getDetails()).replace("\n", " ")
            : saveDto.getSummary();

    Posts posts = Posts.builder()
            .category(category)
            .url(url)
            .subject(saveDto.getTitle())
            .summary(summary)
            .thumbnail(saveDto.getThumbnailUrl())
            .detail(saveDto.getDetails())
            .build();

    posts = postsRepository.save(posts);

    eventPublisher.publishEvent(new NewPostsNoticeEvent(posts));

    connectTags(blog, posts, saveDto.getTags());

    deleteTempPosts(memberNo, tp);

    return new CreatePostsResponseDto(posts.getUrl());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTempPosts(Integer memberNo, String tp) {
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
  @Cacheable(cacheNames = "post", key = "#postUrl")
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
  @CacheEvict(cacheNames = "post", key = "#postKey")
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
  @Caching(evict = {
      @CacheEvict(cacheNames = "post", key = "#postKey"),
      @CacheEvict(cacheNames = "recent-post", allEntries = true),
      @CacheEvict(cacheNames = "posts-activity", allEntries = true)
  })
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

    List<PostsTag> postsTags = postsTagRepository.findByPostsNo(posts.getPostsNo());
    postsTagRepository.deleteAll(postsTags);
  }

  /**
   * {@inheritDoc}
   */
  @Cacheable(cacheNames = "recent-posts",
          key = "'page-' + #pageable.getPageNumber() + 'size-' + #pageable.getPageSize()")
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

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetRecentPostResponseDto> getRecentPostByCategory(Pageable pageable,
                                                                 String blogUrl,
                                                                 String categoryName) {
    Page<GetRecentPostResponseDto> result =
            postsRepository.getRecentPostByCategory(pageable, blogUrl, categoryName);

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
  public Pages<GetRecentPostResponseDto> getRecentPostByTag(Pageable pageable,
                                                                 String blogUrl,
                                                                 String tagName) {
    Page<GetRecentPostResponseDto> result =
            postsRepository.getRecentPostsByTag(pageable, blogUrl, tagName);

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
  public List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl) {
    return postsRepository.getPopularPostsByBlog(blogUrl);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closePosts(Integer memberNo, String postKey) {
    Posts posts = postsRepository.findByUrl(postKey)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    Category category = posts.getCategory();
    Blog blog = category.getBlog();

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    posts.close();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetPostManageResponseDto> getPostsByMemberNo(Pageable pageable, Integer memberNo) {
    Page<GetPostManageResponseDto> page = postsRepository.getPostsByMemberNo(pageable, memberNo);

    return new Pages<>(page.getContent(),
            pageable.getPageNumber(),
            page.hasPrevious(),
            page.hasNext(),
            page.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openPosts(Integer memberNo, String postKey) {
    Posts posts = postsRepository.findByUrl(postKey)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    Category category = posts.getCategory();
    Blog blog = category.getBlog();

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    posts.open();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetRecentPostResponseDto> getPostsByHearts(Integer memberNo, Pageable pageable) {
    Page<GetRecentPostResponseDto> result =
            postsRepository.getPostsByHearts(memberNo, pageable);

    return new Pages<>(result.getContent(),
            pageable.getPageNumber(),
            result.hasPrevious(),
            result.hasNext(),
            result.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Cacheable(cacheNames = "posts-activity", key = "#username")
  @Transactional(readOnly = true)
  @Override
  public Map<String, List<GetPostActivityResponseDto>> getPostActivity(String username) {
    int dayOfYear = 365;

    LocalDate today = LocalDate.now();

    if (today.isLeapYear()) {
      dayOfYear += 1;
    }

    LocalDate start = today.minusDays(dayOfYear);

    int minusDayValue = DayOfWeek.SUNDAY.equals(start.getDayOfWeek())
            ? 0 : start.getDayOfWeek().getValue();

    start = start.minusDays(minusDayValue);

    List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
            .limit(ChronoUnit.DAYS.between(start, today) + 1)
            .toList();

    List<GetPostActivityResponseDto> activities =
            postsRepository.getPostActivity(username, start, today);

    List<GetPostActivityResponseDto> result = new ArrayList<>();

    for (LocalDate date : dates) {
      GetPostActivityResponseDto activity = activities.stream()
              .filter(a -> a.getDate().equals(date)).findFirst().orElse(null);

      if (Objects.isNull(activity)) {
        result.add(new GetPostActivityResponseDto(date, 0L));
      } else {
        result.add(activity);
      }
    }

    return result.stream()
            .collect(Collectors.groupingBy(d -> d.getDate().getDayOfWeek().name()));
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetRecentPostResponseDto> searchPosts(Pageable pageable, String word) {
    Page<GetRecentPostResponseDto> result = postsRepository.searchPosts(pageable, word);

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
  public GetFeedPostsPagesResponseDto feed(Integer memberNo, Long start, Pageable pageable) {
    if (Objects.isNull(start) || start <= 0) {
      start = postsRepository.getFeedStartPostsNoByMemberNo(memberNo);
    }

    Page<GetFeedPostsResponseDto> result = postsRepository
            .getFeedPostsByMemberNo(memberNo, start, pageable);

    return new GetFeedPostsPagesResponseDto(start, new Pages<>(result.getContent(),
            pageable.getPageNumber(),
            result.hasPrevious(),
            result.hasNext(),
            result.getTotalElements()));
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
      String titleUrl = saveDto.getTitle().replaceAll(TITLE_URL_REGEX, " ");
      titleUrl = titleUrl.replaceAll("\\s{2,}", " ");
      titleUrl = titleUrl.trim();
      titleUrl = titleUrl.replace(" ", "-");

      if (titleUrl.isEmpty() || url.isBlank()) {
        throw new InvalidPostsUrlException();
      }

      url = blog.getUrlName() + "/" + titleUrl;
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
