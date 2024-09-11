package com.blogitory.blog.visitant.repository;

import com.blogitory.blog.visitant.entity.Visitant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Visitant JPA repository.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
public interface VisitantRepository extends JpaRepository<Visitant, Long>, VisitantRepositoryCustom {
}
