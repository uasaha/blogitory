package com.blogitory.blog.comment.repository;

import com.blogitory.blog.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
