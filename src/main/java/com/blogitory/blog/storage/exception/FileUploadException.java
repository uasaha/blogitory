package com.blogitory.blog.storage.exception;

/**
 * An exception failed file uploading.
 *
 * @author woonseok
 * @since 1.0
 **/
public class FileUploadException extends RuntimeException {
  private static final String MESSAGE = "업로드에 실패하였습니다.";

  public FileUploadException() {
    super(MESSAGE);
  }
}
