package com.blogitory.blog.commons.advice;

import static com.blogitory.blog.commons.advice.GlobalControllerAdvice.DEFAULT_ERROR_VIEW;
import static com.blogitory.blog.commons.advice.GlobalControllerAdvice.addErrorAttributes;

import com.blogitory.blog.blog.controller.BlogController;
import com.blogitory.blog.category.exception.DuplicateCategoryException;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.main.IndexController;
import com.blogitory.blog.member.controller.MemberController;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.storage.exception.FileUploadException;
import com.blogitory.blog.storage.exception.NoOriginalFileNameException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Controller advice for controller.
 *
 * @author woonseok
 * @Date 2024-07-17
 * @since 1.0
 **/
@ControllerAdvice(assignableTypes = {BlogController.class,
                                     IndexController.class,
                                     MemberController.class})
@RequiredArgsConstructor
public class PageControllerAdvice {

  /**
   * Handle 401.
   *
   * @param response response
   * @throws IOException exception
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(value = {AuthenticationException.class})
  public void handleAuthentication(HttpServletResponse response) throws IOException {
    response.sendRedirect("/");
  }

  /**
   * Handle 400.
   *
   * @param model model
   * @return error page
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {DuplicateCategoryException.class,
                             MemberEmailAlreadyUsedException.class,
                             FileUploadException.class,
                             NoOriginalFileNameException.class})
  public String handleBadRequest(Model model) {
    addErrorAttributes(model, HttpStatus.BAD_REQUEST.value());

    return DEFAULT_ERROR_VIEW;
  }

  /**
   * Handle 404.
   *
   * @param model model
   * @return error page
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = {NoHandlerFoundException.class, NotFoundException.class})
  public String handleNotFound(Model model) {
    addErrorAttributes(model, HttpStatus.NOT_FOUND.value());

    return DEFAULT_ERROR_VIEW;
  }

  /**
   * Handle 403.
   *
   * @param model model
   * @return error page
   */
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(value = {AuthorizationException.class})
  public String handleForbidden(Model model) {
    addErrorAttributes(model, HttpStatus.FORBIDDEN.value());

    return DEFAULT_ERROR_VIEW;
  }

  /**
   * Handle 500.
   *
   * @param model model
   * @return error page
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = {Exception.class})
  public String handleServerError(Model model) {
    addErrorAttributes(model, HttpStatus.INTERNAL_SERVER_ERROR.value());

    return DEFAULT_ERROR_VIEW;
  }
}
