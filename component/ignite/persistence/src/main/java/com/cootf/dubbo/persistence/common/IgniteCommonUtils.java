package com.cootf.dubbo.persistence.common;

import org.apache.ignite.cache.affinity.AffinityKey;

/**
 * 功能：Ignite公共逻辑方法.
 * Created by mjg on 2017/12/7.
 */
public class IgniteCommonUtils {

  private transient AffinityKey<String> key;

  public static IgniteCommonUtils getInstance() {
    return new IgniteCommonUtils();
  }

  /**
   * 功能：新建关联key值.
   */
  public AffinityKey<String> key(String sourceKey, String targetKey) {
    key = new AffinityKey<>(sourceKey, targetKey);
    return key;
  }
}
