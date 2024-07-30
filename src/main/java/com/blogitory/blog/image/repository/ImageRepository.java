package com.blogitory.blog.image.repository;

import com.blogitory.blog.image.entity.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Image Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface ImageRepository extends JpaRepository<Image, Long> {
  Optional<Image> findByUrl(String url);
}
