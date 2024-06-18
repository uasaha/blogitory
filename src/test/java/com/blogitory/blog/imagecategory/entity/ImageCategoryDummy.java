package com.blogitory.blog.imagecategory.entity;

/**
 * ImageCategory dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class ImageCategoryDummy {
  /**
   * Dummy image category.
   *
   * @return the image category
   */
  public static ImageCategory dummy() {
    return new ImageCategory(1, "imageCategory");
  }

}