package com.blogitory.blog.commons.listener.event;

import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.member.entity.Member;

/**
 * Comment notice event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record CommentNoticeEvent(String name, Comment comment, Member commentTo) {
}
