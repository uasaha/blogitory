package com.blogitory.blog.commons.listener.event;

import com.blogitory.blog.posts.entity.Posts;

/**
 * New posts notice event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record NewPostsNoticeEvent(Posts posts) {
}
