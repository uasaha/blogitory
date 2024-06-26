package com.blogitory.blog.commons.interceptor;

import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
  private final MemberService memberService;

  @Override
  public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                         @NonNull Object handler, ModelAndView mav) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()
            && authentication.getDetails() instanceof UserDetailsImpl userDetails) {
      String thumbnail = memberService.getThumbnailByNo(userDetails.getUserNo());

      MemberPersistInfoDto infoDto =
              new MemberPersistInfoDto(userDetails.getIdName(), userDetails.getName(), thumbnail);

      if (Objects.isNull(infoDto.getThumb()) || infoDto.getThumb().isEmpty()) {
        mav.addObject("thumbIsNull", true);
      }

      mav.addObject("members", infoDto);
    }
  }
}
