package com.blogitory.blog.image.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.entity.ImageDummy;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.entity.ImageCategoryDummy;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.service.ObjectStorageService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Image Service Test.
 *
 * @author woonseok
 * @since 1.0
 */
class ImageServiceTest {
  /**
   * The Object storage service.
   */
  ObjectStorageService objectStorageService;
  /**
   * The Image repository.
   */
  ImageRepository imageRepository;
  /**
   * The Image category repository.
   */
  ImageCategoryRepository imageCategoryRepository;
  /**
   * The Member repository.
   */
  MemberRepository memberRepository;

  /**
   * The Image service.
   */
  ImageService imageService;

  BlogRepository blogRepository;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    objectStorageService = Mockito.mock(ObjectStorageService.class);
    imageRepository = Mockito.mock(ImageRepository.class);
    imageCategoryRepository = Mockito.mock(ImageCategoryRepository.class);
    memberRepository = Mockito.mock(MemberRepository.class);
    blogRepository = Mockito.mock(BlogRepository.class);

    imageService = new ImageServiceImpl(objectStorageService,
            imageRepository,
            imageCategoryRepository,
            memberRepository,
            blogRepository);
  }

  /**
   * Upload thumbnail.
   */
  @Test
  @DisplayName("프로필 썸네일 업데이트 성공")
  void uploadThumbnail() {
    ImageCategory imageCategory = new ImageCategory(1, "thumb");
    MockMultipartFile file = new MockMultipartFile("name", "content".getBytes(StandardCharsets.UTF_8));
    FileUploadResponseDto responseDto =
            FileUploadResponseDto.builder()
                    .url("url")
                    .originName("originName")
                    .savedName("savedName")
                    .extension("extension")
                    .savePath("savedPath")
                    .build();
    Member member = MemberDummy.dummy();
    Image image = Image.builder()
            .imageCategory(imageCategory)
            .member(member)
            .url(responseDto.getUrl())
            .originName(responseDto.getOriginName())
            .saveName(responseDto.getSavedName())
            .savePath(responseDto.getSavePath())
            .build();

    when(imageCategoryRepository.findByName(any())).thenReturn(Optional.of(imageCategory));
    when(objectStorageService.uploadFile(any(), any())).thenReturn(responseDto);
    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(imageRepository.save(any())).thenReturn(image);

    ThumbnailUpdateResponseDto result = imageService.uploadThumbnail(1, file);

    verify(imageCategoryRepository, times(1)).findByName(any());
    verify(objectStorageService, times(1)).uploadFile(any(), any());
    verify(memberRepository, times(1)).findById(any());
    verify(imageRepository, times(1)).save(any());

    assertThat(result.getOriginName()).isEqualTo(image.getOriginName());
    assertThat(result.getUrl()).isEqualTo(image.getUrl());
  }

  @Test
  @DisplayName("프로필 사진 제거 성공")
  void removeThumbnail() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    imageService.removeThumbnail(member.getMemberNo());

    Assertions.assertNull(member.getProfileThumb());
  }

  @Test
  @DisplayName("프로필 사진 제거 실패 - 없는 멤버")
  void removeThumbnailFailed() {
    when(memberRepository.findById(any())).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> imageService.removeThumbnail(1));
  }

  @Test
  @DisplayName("블로그 프로필 사진 업데이트 성공")
  void updateBlogThumbnail() {
    ImageCategory imageCategory = new ImageCategory(1, "thumb");
    MockMultipartFile file = new MockMultipartFile("name", "content".getBytes(StandardCharsets.UTF_8));
    FileUploadResponseDto responseDto =
            FileUploadResponseDto.builder()
                    .url("url")
                    .originName("originName")
                    .savedName("savedName")
                    .extension("extension")
                    .savePath("savedPath")
                    .build();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Image image = Image.builder()
            .imageCategory(imageCategory)
            .member(member)
            .url(responseDto.getUrl())
            .originName(responseDto.getOriginName())
            .saveName(responseDto.getSavedName())
            .savePath(responseDto.getSavePath())
            .build();

    ReflectionTestUtils.setField(blog, "background", null);

    when(imageCategoryRepository.findByName(any())).thenReturn(Optional.of(imageCategory));
    when(objectStorageService.uploadFile(any(), any())).thenReturn(responseDto);
    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(imageRepository.save(any())).thenReturn(image);
    when(blogRepository.findBlogByUrlName(any())).thenReturn(Optional.of(blog));

    ThumbnailUpdateResponseDto result = imageService.updateBlogThumbnail(1, blog.getUrlName(), file);

    verify(imageCategoryRepository, times(1)).findByName(any());
    verify(objectStorageService, times(1)).uploadFile(any(), any());
    verify(memberRepository, times(1)).findById(any());
    verify(imageRepository, times(1)).save(any());

    assertThat(result.getOriginName()).isEqualTo(image.getOriginName());
    assertThat(result.getUrl()).isEqualTo(image.getUrl());
  }

  @Test
  @DisplayName("블로그 프로필 사진 업데이트 성공")
  void updateBlogThumbnailChanged() {
    ImageCategory imageCategory = new ImageCategory(1, "thumb");
    MockMultipartFile file = new MockMultipartFile("name", "content".getBytes(StandardCharsets.UTF_8));
    FileUploadResponseDto responseDto =
            FileUploadResponseDto.builder()
                    .url("url")
                    .originName("originName")
                    .savedName("savedName")
                    .extension("extension")
                    .savePath("savedPath")
                    .build();
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Image image = Image.builder()
            .imageCategory(imageCategory)
            .member(member)
            .url(responseDto.getUrl())
            .originName(responseDto.getOriginName())
            .saveName(responseDto.getSavedName())
            .savePath(responseDto.getSavePath())
            .build();

    imageService = Mockito.spy(imageService);

    when(imageCategoryRepository.findByName(any())).thenReturn(Optional.of(imageCategory));
    when(objectStorageService.uploadFile(any(), any())).thenReturn(responseDto);
    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(imageRepository.save(any())).thenReturn(image);
    when(blogRepository.findBlogByUrlName(any())).thenReturn(Optional.of(blog));
    doNothing().when(imageService).removeBlogThumbnail(anyInt(), anyString());

    ThumbnailUpdateResponseDto result = imageService.updateBlogThumbnail(1, blog.getUrlName(), file);

    verify(imageCategoryRepository, times(1)).findByName(any());
    verify(objectStorageService, times(1)).uploadFile(any(), any());
    verify(memberRepository, times(1)).findById(any());
    verify(imageRepository, times(1)).save(any());

    assertThat(result.getOriginName()).isEqualTo(image.getOriginName());
    assertThat(result.getUrl()).isEqualTo(image.getUrl());
  }

  @Test
  @DisplayName("블로그 프로필 사진 삭제 성공")
  void removeBlogThumbnailSuccess() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    ImageCategory category = ImageCategoryDummy.dummy();
    Image image = ImageDummy.blogDummy(category, blog);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(blogRepository.findBlogByUrlName(any())).thenReturn(Optional.of(blog));
    when(imageRepository.findByUrl(any())).thenReturn(Optional.of(image));
    doNothing().when(objectStorageService).deleteFile(any(), any());
    doNothing().when(imageRepository).delete(any());

    imageService.removeBlogThumbnail(member.getMemberNo(), blog.getUrlName());

    assertNull(blog.getBackground());
  }

  @Test
  @DisplayName("블로그 프로필 사진 삭제 실패 - 회원 미일치")
  void removeBlogThumbnailFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    ImageCategory category = ImageCategoryDummy.dummy();
    Image image = ImageDummy.blogDummy(category, blog);
    Member member2 = MemberDummy.dummy();
    ReflectionTestUtils.setField(member2, "memberNo", 5);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member2));
    when(blogRepository.findBlogByUrlName(any())).thenReturn(Optional.of(blog));
    when(imageRepository.findByUrl(any())).thenReturn(Optional.of(image));
    doNothing().when(objectStorageService).deleteFile(any(), any());
    doNothing().when(imageRepository).delete(any());

    assertThrows(AuthenticationException.class,
            () -> imageService.removeBlogThumbnail(5, "blogUrl"));
  }
}