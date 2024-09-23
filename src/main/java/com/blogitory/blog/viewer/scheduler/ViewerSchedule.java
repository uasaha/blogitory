package com.blogitory.blog.viewer.scheduler;

import com.blogitory.blog.viewer.service.ViewerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Viewer scheduler.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class ViewerSchedule {
  private final ViewerService viewerService;

  /**
   * Save viewers, from redis to DB.
   */
  @Scheduled(fixedRate = 1200000, initialDelay = 1200000)
  public void saveViewers() {
    log.debug("Saving viewers start.");
    try {
      viewerService.persistence();
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      log.debug("Saving viewers end.");
    }
  }
}
