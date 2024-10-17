package com.blogitory.blog.commons.listener.event;


/**
 * Sse send event.
 *
 * @author woonseok
 * @since 1.0
 **/
public record SseSendEvent(Long noticeNo, Integer target, Object body) {
}
