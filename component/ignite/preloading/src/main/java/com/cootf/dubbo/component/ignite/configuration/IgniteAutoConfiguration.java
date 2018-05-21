package com.cootf.dubbo.component.ignite.configuration;


import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PreDestroy;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

/**
 * Ignite 配置
 *
 * @author Charles
 * @email amwfhv@yeah.net
 * @create at 2017/11/18 21:25
 **/
@Configuration
public class IgniteAutoConfiguration implements ApplicationListener {

  private static final Logger logger = LoggerFactory.getLogger(IgniteAutoConfiguration.class);
  /**
   * 数据加载状态(0-未加载；1-加载中；2-已加载;)
   */
  private static final int STATUS_READY = 0;
  private static final int STATUS_PROCEEDING = 1;
  private static final int STATUS_FINISHED = 2;

  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;
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
      reentrantLock.tryLock(); //防止多个事件造成执行多遍
      try {
        if (this.ignite == null) {
          logger.info("开始启动Ignite实例");
          IgniteConfiguration igniteConfiguration = applicationContext
              .getBean(IgniteConfiguration.class);
          igniteConfiguration.setIgniteInstanceName(igniteProperties.getInstanceName());
          igniteConfiguration.setClientMode(igniteProperties.isClientMode());
//          igniteConfiguration.setGridLogger(new Slf4jLogger(logger));
          this.ignite = Ignition.start(igniteConfiguration);

          if (igniteProperties.isLoadData()) {
            Collection<String> cacheNames = ignite.cacheNames();
            if (!CollectionUtils.isEmpty(cacheNames)) {
              loadDataToCache(cacheNames);
            }
          }
        } else {
          logger.info("Ignite节点已经启动,无需启动");
        }
      } catch (Exception e) {
        logger.error("启动Ignite异常 {}", e);
      } finally {
        reentrantLock.unlock();
      }
    }
  }

  /**
   * 功能：加载MongoDB中数据到Ignite缓存.
   */
  private void loadDataToCache(Collection<String> cacheNameList) {
    logger.info("开始加载db数据到Ignite");
    String loadFlag = null;
    for (String key : cacheNameList) {
      if (igniteProperties.getSysCacheName() != null) {
        //如果缓存名称是系统变量配置，则跳过，不读取数据库.
        if (StringUtils.equalsIgnoreCase(igniteProperties.getSysCacheName(), key)) {
          continue;
        }
        //如果当前数据缓存已经加载，则不再加载.
        loadFlag = ObjectUtils
            .toString(ignite.getOrCreateCache(igniteProperties.getSysCacheName()).get(key));
      }
      if (StringUtils.isNotBlank(loadFlag) && Integer.valueOf(loadFlag).intValue() > STATUS_READY) {
        continue;
      }
      threadPoolTaskExecutor.submit(() -> {
        try {
          ignite.getOrCreateCache(igniteProperties.getSysCacheName()).put(key, STATUS_PROCEEDING);
          //缓存名字作为第一个参数传入.
          ignite.cache(key).loadCache(null, key);
          //标记当前数据已加载到缓存.
          if (igniteProperties.getSysCacheName() != null) {
            ignite.getOrCreateCache(igniteProperties.getSysCacheName()).put(key, STATUS_FINISHED);
          }
          Thread.sleep(2000);
        } catch (Exception e) {
          logger.warn("缓存数据异步加载异常 {}", e);
        }
      });
    }
  }

  @PreDestroy
  public void destroy() {
    if (this.ignite != null) {
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
