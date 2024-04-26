package com.blogitory.blog.image.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;

/**
 * Image dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class ImageDummy {
  public static Image thumbnailDummy(ImageCategory imageCategory, Member member) {
    return new Image(
            1L,
            imageCategory,
            member,
            null,
            null,
            "imageUrl",
            "originName",
            "saveName",
            "extension",
            "savePath");
  }

  public static Image postsDummy(ImageCategory imageCategory, Posts posts) {
    return new Image(
            1L,
            imageCategory,
            null,
            posts,
            null,
            "imageUrl",
            "originName",
            "saveName",
            "extension",
            "savePath");
  }

  public static Image blogDummy(ImageCategory imageCategory, Blog blog) {
    return new Image(
            1L,
            imageCategory,
            null,
            null,
            blog,
            "imageUrl",
            "originName",
            "saveName",
            "extension",
            "savePath");
  }
}