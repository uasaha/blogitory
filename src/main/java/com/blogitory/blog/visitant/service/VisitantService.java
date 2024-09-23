package com.blogitory.blog.visitant.service;

import com.blogitory.blog.visitant.dto.GetVisitantCountResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

/**
 * Visitant service.
 *
 * @author woonseok
 * @Date 2024-09-10
 * @since 1.0
 **/
public interface VisitantService {
  /**
   * Viewing blog pages.
   *
   * @param blogUrl  blog url
   * @param memberNo member no
   * @param ip       client ip
   */
  void viewBlogs(String blogUrl, Integer memberNo, String ip);

  /**
   * Get visitant total & today count of blog.
   *
   * @param blogUrl blog url
   * @return count(total, today)
   */
  Map<String, Integer> getVisitantCount(String blogUrl);

  /**
   * Save visitant to DB.
   *
   * @throws JsonProcessingException object mapper exception
   */
  void persistence() throws JsonProcessingException;

  /**
   * Save visitant to DB and delete in redis.
   *
   * @throws JsonProcessingException object mapper exception
   */
  void saveAndDelete() throws JsonProcessingException;

  /**
   * Get visitants monthly count.
   *
   * @param memberNo member no
   * @param blogUrl  blog url
   * @return visitants count
   */
  List<GetVisitantCountResponseDto> getVisitantMonthlyCount(Integer memberNo, String blogUrl);
}
