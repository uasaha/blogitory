package com.blogitory.blog.jwt.service;

import com.blogitory.blog.member.dto.MemberLoginResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

/**
 * Service of JWT.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface JwtService {
  /**
   * Issue JWT Token.
   *
   * @param responseDto infos of login member
   * @return new Access Token
   */
  String issue(String uuid, MemberLoginResponseDto responseDto);

  /**
   * ReIssue JWT Token.
   *
   * @param uuid the identifier of login member
   * @return new Access Token
   */
  Map<String, String> reIssue(String uuid) throws JsonProcessingException;
}
