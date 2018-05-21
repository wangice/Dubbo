package com.cootf.dubbo.component.ignite.store;

import javax.cache.configuration.Factory;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Ignite StoreFactory ignite.xml管理,每个Store均会生成一个Factory
 *
 * @author Admin
 */
public class CacheStoreFactory<K, V> implements Factory<CacheStoreAdapter<K, V>>,
    ApplicationContextAware {

  private String storeName;
  private ApplicationContext applicationContext;

  @Override
  public CacheStoreAdapter<K, V> create() {
    CacheStoreAdapter result = (CacheStoreAdapter) this.applicationContext.getBean(storeName);
    return result;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
