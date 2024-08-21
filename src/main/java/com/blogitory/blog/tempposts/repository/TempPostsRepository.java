package com.blogitory.blog.tempposts.repository;


import com.blogitory.blog.tempposts.entity.TempPosts;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Temp posts repository.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
public interface TempPostsRepository extends JpaRepository<TempPosts, UUID>,
        TempPostsRepositoryCustom {
  List<TempPosts> findAllByMemberMemberNoOrderByCreatedAtDesc(Integer memberNo);
}
