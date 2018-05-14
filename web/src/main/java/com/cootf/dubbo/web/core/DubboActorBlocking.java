package com.cootf.dubbo.web.core;

import com.cootf.dubbo.web.actor.ActorBlocking;
import org.springframework.context.annotation.Configuration;

/**
 * @author:ice
 * @Date: 2018/5/9 10:14
 */
@Configuration
public class DubboActorBlocking extends ActorBlocking {

  /**
   * 开启16个线程去执行任务.
   */
  public DubboActorBlocking() {
    super(16);
  }

}
