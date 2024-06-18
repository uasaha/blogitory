package com.blogitory.blog.image.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.service.ObjectStorageService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

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

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    objectStorageService = Mockito.mock(ObjectStorageService.class);
    imageRepository = Mockito.mock(ImageRepository.class);
    imageCategoryRepository = Mockito.mock(ImageCategoryRepository.class);
    memberRepository = Mockito.mock(MemberRepository.class);

    imageService = new ImageServiceImpl(objectStorageService,
            imageRepository,
            imageCategoryRepository,
            memberRepository);
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
}