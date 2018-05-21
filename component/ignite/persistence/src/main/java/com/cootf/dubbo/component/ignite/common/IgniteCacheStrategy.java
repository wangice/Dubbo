package com.cootf.dubbo.component.ignite.common;

import java.util.List;
import java.util.Map;
import org.apache.ignite.Ignite;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.SqlQuery;

/**
 * Created by mjg on 2017/6/7.
 */
public interface IgniteCacheStrategy<T> {

  Ignite getIgnite();

  String getCacheName();

  long getCacheId(String seqName);

  long getBatchCacheId(String seqName, int batchSize);

  /**
   * 功能：异步存储缓存信息.
   *
   * @param key String
   * @param data T
   * @return Boolean
   */
  boolean putAsync(String key, T data);

  /**
   * 功能：同步存储缓存信息.
   *
   * @param key String
   * @param data T
   * @return Boolean
   */
  boolean put(String key, T data);

  /**
   * 功能：同步存储指定过期时间的缓存信息.
   */
  boolean putWithExpires(String key, T data, int expiresMinutes);

  /**
   * 功能：存储并置数据.
   *
   * @param key 并置数据的key值.
   * @param data 并置数据的value值.
   */
  void putAffinity(AffinityKey<String> key, T data);

  /**
   * 功能：插入全部数据.
   */
  void putAll(Map<String, T> dataMap);

  /**
   * 取出用户传入缓存组对应的键的值
   *
   * @param key String
   * @return T
   */
  T getAsync(String key);

  /**
   * 取出用户传入缓存组对应的键的值
   *
   * @param key String
   * @return T
   */
  T get(String key);

  T getAffinity(AffinityKey<String> key);

  /**
   * 移除用户传入缓存组对应的键的对象
   *
   * @return Boolean
   */
  boolean removeAsync(String key);

  /**
   * 移除用户传入缓存组对应的键的对象
   *
   * @return Boolean
   */
  boolean remove(String key);

  boolean removeAffinity(AffinityKey<String> key);

  /**
   * 功能：分页获取数据集合列表.
   */
  List<T> getListByQuery(SqlQuery sqlQuery);

  /**
   * 功能：分页获取数据属性集合列表.
   */
  List<List<?>> getListByFieldQuery(IgniteSqlQuery igniteSqlQuery);

  /**
   * 功能：快速地将大量的数据流注入Ignite.
   *
   * @param overWriteFlag 是否允许覆写内存中已经存在的数据(true-是；false-否).
   */
  boolean pullData(boolean overWriteFlag, Map<String, Object> dataMap);

  /**
   * 功能：获取指定查询记录数.
   */
  long executeDMLSql(IgniteSqlQuery igniteSqlQuery);

}
