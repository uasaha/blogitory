package com.blogitory.blog.commons.listener.event;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;
import java.util.List;

/**
 * New posts notice event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record NewPostsNoticeEvent(Posts posts, List<Member> targets) {
}
