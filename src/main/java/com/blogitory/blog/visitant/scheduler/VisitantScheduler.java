package com.blogitory.blog.visitant.scheduler;

import com.blogitory.blog.visitant.service.VisitantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Visitant scheduler.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class VisitantScheduler {

  private final VisitantService visitantService;

  /**
   * Save visitant to DB. every 1 hour.
   */
  @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
  public void saveVisitant() {
    log.debug("Saving visitant start.");
    try {
      visitantService.persistence();
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      log.debug("Saving viewers end.");
    }
  }

  /**
   * Save visitant to DB and delete every day in redis.
   */
  @Scheduled(cron = "0 55 23 * * *", zone = "Asia/Seoul")
  public void saveAndDeleteVisitant() {
    log.debug("Saving and removing visitant start.");
    try {
      visitantService.saveAndDelete();
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      log.debug("Saving and removing visitant end.");
    }
  }
}
