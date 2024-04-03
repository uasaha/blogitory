package com.blogitory.blog.commons.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity for JPA Auditing.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreatedAtEntity {
  @CreatedDate
  @Column(name = "create_at", updatable = false)
  private LocalDateTime createAt;
}
