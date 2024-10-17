package com.blogitory.blog.commons.listener.event;

import com.blogitory.blog.member.entity.Member;

/**
 * Follow notice event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record FollowNoticeEvent(Member followFrom, Member followTo) {
}
