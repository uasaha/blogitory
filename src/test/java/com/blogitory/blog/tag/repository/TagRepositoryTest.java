package com.blogitory.blog.tag.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.entity.TagDummy;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Tag repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class TagRepositoryTest {

  /**
   * The Tag repository.
   */
  @Autowired
  TagRepository tagRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    entityManager.createNativeQuery("ALTER TABLE `tag` ALTER COLUMN `tag_no` RESTART")
            .executeUpdate();
  }

  /**
   * Tag save.
   */
  @Test
  @DisplayName("태그 저장")
  void tagSave() {
    Tag tag = TagDummy.dummy();
    Tag actual = tagRepository.save(tag);

    assertAll(
            () -> assertEquals(tag.getTagNo(), actual.getTagNo()),
            () -> assertEquals(tag.getName(), actual.getName()),
            () -> assertEquals(tag.isDeleted(), actual.isDeleted())
    );
  }
}