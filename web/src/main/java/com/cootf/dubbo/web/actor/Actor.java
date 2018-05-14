package com.cootf.dubbo.web.actor;

import com.cootf.dubbo.misc.Misc;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * actor抽象基类.
 *
 * @Author: ice
 * @time 2017年5月12日 下午2:09:35
 */
public abstract class Actor {

  private static final Logger logger = LoggerFactory.getLogger(Actor.class);

  public enum ActorType {
    /**
     * 进程内Actor, 附着在某个Tworker上.
     */
    ITC,
    /**
     * 拥有自己独立线程或线程池的actor, 主要用于IO阻塞操作, 如数据库查询.
     */
    BLOCKING;
  }

  /**
   * Actor类型.
   */
  public ActorType type;
  /**
   * Actor名称.
   */
  public String name;

  public Actor(ActorType type) {
    this.type = type;
    this.name = this.getClass().getSimpleName();
  }

  public void future(Consumer<Void> c) {
    if (this.type.ordinal() == ActorType.BLOCKING.ordinal()) /* 直接入队. */ {
      ((ActorBlocking) this).push(c);
      return;
    }
    Misc.exeConsumer(c, null);
  }
}
