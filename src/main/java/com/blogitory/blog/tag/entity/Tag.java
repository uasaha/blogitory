package com.blogitory.blog.tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Tag entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "tag")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tag_no")
  private Long tagNo;

  @Column(name = "tag_name")
  private String name;

  @Column(name = "tag_deleted")
  private boolean deleted;
}
