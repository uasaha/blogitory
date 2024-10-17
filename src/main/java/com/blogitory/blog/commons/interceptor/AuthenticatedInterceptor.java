package com.blogitory.blog.commons.interceptor;

import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Interceptor for adding User's info in Model.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
public class AuthenticatedInterceptor implements HandlerInterceptor {
  private final BlogService blogService;
  private final NoticeService noticeService;

  @Override
  public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                         @NonNull Object handler, ModelAndView mav) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()
            && authentication.getDetails() instanceof UserDetailsImpl userDetails) {
      GetMemberPersistInfoDto infoDto =
              new GetMemberPersistInfoDto(userDetails.getIdName(),
                      userDetails.getName(),
                      userDetails.getPfp());

      if (Objects.isNull(infoDto.getThumb()) || infoDto.getThumb().isEmpty()) {
        mav.addObject("thumbIsNull", true);
      }

      mav.addObject("members", infoDto);

      List<GetBlogInHeaderResponseDto> blogs =
              blogService.blogListForHeader(userDetails.getIdName());
      mav.addObject("headerBlogs", blogs);
      mav.addObject("readNotice", noticeService.hasNonReadNotice(userDetails.getUserNo()));
    }
  }
}
