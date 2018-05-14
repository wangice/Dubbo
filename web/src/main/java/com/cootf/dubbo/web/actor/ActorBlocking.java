package com.cootf.dubbo.web.actor;

import com.cootf.dubbo.misc.Misc;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拥有自己独立线程(一个或多个)的Actor, 用于有限的IO阻塞操作, 如数据库.
 *
 * @Author: ice
 * @time 2017年5月12日 下午9:24:23
 */
public abstract class ActorBlocking extends Actor {

  private static final Logger logger = LoggerFactory.getLogger(ActorBlocking.class);

  /**
   * 等待处理的Consumer.
   */
  private ConcurrentLinkedQueue<Consumer<Void>> cs = new ConcurrentLinkedQueue<>();
  /**
   * cs的size.
   */
  private final AtomicInteger size = new AtomicInteger(0);
  /**
   * 拥有的线程个数.
   */
  private int tc = 1;
  /**
   * 线程忙?
   */
  private volatile boolean busy = false;

  public ActorBlocking(int tc) {
    super(ActorType.BLOCKING);
    this.tc = tc < 1 ? 1 : tc;
    this.start();
  }

  /**
   * 添加一个Consumer.
   */
  public void push(Consumer<Void> c) {
    this.cs.add(c);
    this.size.incrementAndGet();
    synchronized (this) {
      this.notify();
    }
  }

  /**
   * 线程忙?
   */
  public boolean isBusy() {
    return this.busy;
  }

  /**
   * 队列尺寸.
   */
  public int size() {
    return this.size.get();
  }

  private void start() {
    ActorBlocking ab = this;
    ExecutorService es = Executors.newFixedThreadPool(this.tc);
    for (int i = 0; i < this.tc; ++i) {
      es.execute(() ->
      {
        logger.info("Actor-Blocking worker thread started successfully, name: {}, tid: {}", ab.name,
            Thread.currentThread().getId());
        while (true) {
          ab.run();
        }
      });
    }
  }

  private void run() {
    Consumer<Void> c = this.cs.poll();
    if (c == null) {
      synchronized (this) {
        try {
          this.wait();
        } catch (InterruptedException e) {
        }
      }
      c = this.cs.poll();
    }
    if (c != null) /* 抢占式. */ {
      this.size.decrementAndGet();
      this.busy = true;
      Misc.exeConsumer(c, null);
      this.busy = false;
    }
  }
}
