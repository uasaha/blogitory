package com.blogitory.blog.image.service.impl;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import com.blogitory.blog.storage.service.ObjectStorageService;
import java.util.Objects;
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
  private final BlogRepository blogRepository;

  private static final String MEM_THUMBNAIL = "mem_thumb";
  private static final String BLOG_THUMB = "log_thumb";
  private static final String POST_THUMB = "post_thumb";
  private static final String POST_IMAGE = "post_image";

  /**
   * {@inheritDoc}
   */
  @Override
  public ThumbnailUpdateResponseDto uploadThumbnail(Integer memberNo, MultipartFile file) {
    ImageCategory imageCategory = imageCategoryRepository.findByName(MEM_THUMBNAIL)
            .orElseThrow(() -> new NotFoundException(ImageCategory.class));

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(
            MEM_THUMBNAIL, file);

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeThumbnail(Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateThumbnail(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ThumbnailUpdateResponseDto updateBlogThumbnail(Integer memberNo,
                                                        String blogUrl,
                                                        MultipartFile file) {
    ImageCategory category = imageCategoryRepository.findByName(BLOG_THUMB)
            .orElseThrow(() -> new NotFoundException(ImageCategory.class));
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
    Blog blog = blogRepository.findBlogByUrlName(blogUrl)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    checkingBlogMember(member, blog);

    if (Objects.nonNull(blog.getBackground())) {
      removeBlogThumbnail(memberNo, blogUrl);
    }

    FileUploadResponseDto responseDto = objectStorageService.uploadFile(
            BLOG_THUMB, file);

    Image image = Image.builder()
            .imageCategory(category)
            .member(member)
            .blog(blog)
            .url(responseDto.getUrl())
            .originName(responseDto.getOriginName())
            .saveName(responseDto.getSavedName())
            .extension(responseDto.getExtension())
            .savePath(responseDto.getSavePath())
            .build();

    image = imageRepository.save(image);
    blog.updateBackground(image.getUrl());

    return new ThumbnailUpdateResponseDto(image.getUrl(), image.getOriginName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeBlogThumbnail(Integer memberNo, String blogUrl) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
    Blog blog = blogRepository.findBlogByUrlName(blogUrl)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    checkingBlogMember(member, blog);

    Image image = imageRepository.findByUrl(blog.getBackground())
            .orElseThrow(() -> new NotFoundException(Image.class));

    objectStorageService.deleteFile(BLOG_THUMB, image.getSaveName());
    imageRepository.delete(image);

    blog.updateBackground(null);
  }

  private void checkingBlogMember(Member member, Blog blog) {
    if (!member.getMemberNo().equals(blog.getMember().getMemberNo())) {
      throw new AuthenticationException("Not Blog's member");
    }
  }
}
