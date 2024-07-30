package com.blogitory.blog.commons.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Querydsl config for use JPAQueryFactory.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@Configuration
public class QuerydslConfig {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * new JPAQueryFactory bean.
   *
   * @return JPAQueryFactory
   */
  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
  }
}
