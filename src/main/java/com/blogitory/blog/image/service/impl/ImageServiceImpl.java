package com.blogitory.blog.image.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image Service's implementation.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ObjectStorageService objectStorageService;
  private final ImageRepository imageRepository;
  private final ImageCategoryRepository imageCategoryRepository;
  private final MemberRepository memberRepository;

  private static final String THUMBNAIL = "mem_thumb";

  /**
   * {@inheritDoc}
   */
  @Override
  public ThumbnailUpdateResponseDto uploadThumbnail(Integer memberNo, MultipartFile file) {

    ImageCategory imageCategory = imageCategoryRepository.findByName(THUMBNAIL)
            .orElseThrow(() -> new NotFoundException(ImageCategory.class));

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(
            THUMBNAIL, file);

    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Image image = Image.builder()
            .imageCategory(imageCategory)
            .member(member)
            .url(responseDto.getUrl())
            .originName(responseDto.getOriginName())
            .saveName(responseDto.getSavedName())
            .extension(responseDto.getExtension())
            .savePath(responseDto.getSavePath())
            .build();

    image = imageRepository.save(image);
    member.updateThumbnail(image.getUrl());

    return new ThumbnailUpdateResponseDto(image.getUrl(), image.getOriginName());
  }
}
