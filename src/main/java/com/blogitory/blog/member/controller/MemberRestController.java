package com.blogitory.blog.member.controller;

import com.blogitory.blog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Rest api Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemberRestController {
  private final MemberService memberService;
}
