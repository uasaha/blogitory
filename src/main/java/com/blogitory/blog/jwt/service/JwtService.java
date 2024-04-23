package com.blogitory.blog.jwt.service;

import com.blogitory.blog.member.dto.MemberLoginResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

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
   * @param uuid the identifier of login member
   * @param responseDto infos of login member
   * @param roles roles of login member
   * @return new Access Token
   */
  String issue(String uuid, MemberLoginResponseDto responseDto, List<String> roles);

  /**
   * ReIssue JWT Token.
   *
   * @param uuid the identifier of login member
   * @return new Access Token
   */
  String reIssue(String uuid) throws JsonProcessingException;
}
