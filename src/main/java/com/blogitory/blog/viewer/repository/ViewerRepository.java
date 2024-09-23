package com.blogitory.blog.viewer.repository;

import com.blogitory.blog.viewer.entity.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Viewer JPA repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface ViewerRepository extends JpaRepository<Viewer, Long>, ViewerRepositoryCustom {
}
