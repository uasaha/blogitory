package com.blogitory.blog.storage.service;

import com.blogitory.blog.storage.dto.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for Object storage.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface ObjectStorageService {
  /**
   * Upload new file.
   *
   * @param type file category
   * @param file file
   * @return uploaded file info
   */
  FileUploadResponseDto uploadFile(String type, MultipartFile file);

  /**
   * Delete file.
   *
   * @param type     file type
   * @param filename file name
   */
  void deleteFile(String type, String filename);
}
