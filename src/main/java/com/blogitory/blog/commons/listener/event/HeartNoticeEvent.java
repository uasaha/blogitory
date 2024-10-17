package com.blogitory.blog.commons.listener.event;

import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.member.entity.Member;

/**
 * Heart notice event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record HeartNoticeEvent(Heart heart, Member heartTo) {
}
