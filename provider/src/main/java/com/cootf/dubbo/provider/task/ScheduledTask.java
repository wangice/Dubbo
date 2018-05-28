package com.cootf.dubbo.provider.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/5/26 09:44
 */
@Component
public class ScheduledTask {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

  @Async
  @Scheduled(cron = "0/5 * * * * *")
  public void scheduled() {
    log.info("=====>>>>>使用cron  {}", System.currentTimeMillis());
  }

  @Async
  @Scheduled(fixedRate = 5000)
  public void scheduled1() {
    log.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
  }

  @Async
  @Scheduled(fixedDelay = 5000)
  public void scheduled2() {
    log.info("=====>>>>>fixedDelay{}", System.currentTimeMillis());
  }
}
