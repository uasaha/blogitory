package com.blogitory.blog.commons.advice;

import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.security.users.UserDetailsImpl;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global controller advice.
 *
 * @author woonseok
 * @Date 2024-07-18
 * @since 1.0
 **/
@ControllerAdvice
public class GlobalControllerAdvice {
  public static final String DEFAULT_ERROR_VIEW = "index/error/error";

  /**
   * Handle 404.
   *
   * @param model model
   * @return error page
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = {NoHandlerFoundException.class})
  public String handleNoHandlerFoundException(Model model) {
    addErrorAttributes(model, HttpStatus.NOT_FOUND.value());

    return DEFAULT_ERROR_VIEW;
  }

  protected static void addErrorAttributes(Model model, Integer status) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()
            && authentication.getDetails() instanceof UserDetailsImpl userDetails) {
      GetMemberPersistInfoDto infoDto =
              new GetMemberPersistInfoDto(
                      userDetails.getIdName(), userDetails.getName(), userDetails.getPfp());

      if (Objects.isNull(infoDto.getThumb()) || infoDto.getThumb().isEmpty()) {
        model.addAttribute("thumbIsNull", true);
      }

      model.addAttribute("members", infoDto);
    }

    model.addAttribute("errorCode", status);
  }
}
