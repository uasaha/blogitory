package com.blogitory.blog.storage.exception;

/**
 * An exception failed getting origin file name.
 *
 * @author woonseok
 * @since 1.0
 **/
public class NoOriginalFileNameException extends RuntimeException {
  private static final String MESSAGE = "Not has original file name";
  
  public NoOriginalFileNameException() {
    super(MESSAGE);
  }
}
