package com.cootf.dubbo.provider.configuration;

import com.cootf.dubbo.component.ignite.configuration.IgniteProperties;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PreDestroy;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * @author:ice
 * @Date: 2018/5/11 14:26
 */
@Configuration
public class IgniteAutoConfiguration implements ApplicationListener {

  private static final Logger logger = LoggerFactory.getLogger(IgniteAutoConfiguration.class);

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private IgniteProperties igniteProperties;

  private ReentrantLock reentrantLock = new ReentrantLock();

  private Ignite ignite;

  /**
   * 能监听到任意事件,说明Spring容器已启动成功,此时获取容器中Ignite配置,启动Ignite节点
   */
  @Override
  public void onApplicationEvent(ApplicationEvent applicationEvent) {

    if (applicationEvent.getClass() == ApplicationReadyEvent.class) {
      logger.info("监听到Spring ApplicationReadyEvent事件");
      reentrantLock.tryLock();//防止多个事件造成执行多遍
      try {
        if (ignite == null) {
          IgniteConfiguration igniteConfiguration = applicationContext
              .getBean(IgniteConfiguration.class);
          igniteConfiguration.setIgniteInstanceName(igniteProperties.getInstanceName());
          igniteConfiguration.setClientMode(igniteProperties.isClientMode());

          this.ignite = Ignition.start(igniteConfiguration);
        } else {
          logger.debug("Ignite节点已经启动,无需启动");
        }
      } catch (Exception e) {
        logger.warn("ignite 启动异常 {}", e);
      } finally {
        reentrantLock.unlock();
      }
    }
  }

  @PreDestroy
  public void destroy() {
    if (ignite != null) {
      ignite.close();
      ignite = null;
    }
  }

  /**
   * 由于Spring容器启动完毕后，才启动的Ignite节点,所有Ignite类型Bean直接注入均为空,所以在此类中提供方法给外部获取Ignite实例
   *
   * @return ignite实例
   */
  public Ignite getIgnite() {
    return ignite;
  }
}
