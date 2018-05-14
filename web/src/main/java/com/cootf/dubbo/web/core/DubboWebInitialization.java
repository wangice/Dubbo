package com.cootf.dubbo.web.core;

/**
 * @author:ice
 * @Date: 2018/5/9 10:18
 */
public class DubboWebInitialization {

  private DubboActorBlocking actorBlocking;

  public void init() {
    actorBlocking = new DubboActorBlocking();/* 启动线程. */
  }


  public DubboActorBlocking getActorBlocking() {
    return actorBlocking;
  }

  public void setActorBlocking(DubboActorBlocking actorBlocking) {
    this.actorBlocking = actorBlocking;
  }
}
