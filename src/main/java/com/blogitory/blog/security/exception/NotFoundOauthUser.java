package com.blogitory.blog.security.exception;

import lombok.Getter;

/**
 * Not founded Oauth user.
 *
 * @author woonseok
 * @Date 2024-08-23
 * @since 1.0
 **/
@Getter
public class NotFoundOauthUser extends RuntimeException {
  private static final String MESSAGE = "Not found oauth user";

  private final String provider;
  private final String id;
  private final String name;
  private final String thumb;

  /**
   * Constructor.
   *
   * @param provider oauth provider
   * @param id       oauth id
   * @param name     oauth name
   * @param thumb    oauth thumb
   */
  public NotFoundOauthUser(String provider, String id, String name, String thumb) {
    super(MESSAGE);
    this.provider = provider;
    this.id = id;
    this.name = name;
    this.thumb = thumb;
  }
}
