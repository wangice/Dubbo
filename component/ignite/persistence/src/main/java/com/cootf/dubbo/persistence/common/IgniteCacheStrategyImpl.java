package com.cootf.dubbo.persistence.common;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.processors.cache.CacheEntryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能：Ignite缓存基础服务. Created by mjg on 2017/6/7.
 */
public abstract class IgniteCacheStrategyImpl<T> implements IgniteCacheStrategy<T> {

  protected static final String ID = "id";
  private static final Logger logger = LoggerFactory.getLogger(IgniteCacheStrategyImpl.class);

  @Override
  public abstract String getCacheName();

  @Override
  public abstract Ignite getIgnite();

  @Override
  public boolean putAsync(String key, T data) {
    getIgnite().getOrCreateCache(this.getCacheName()).putAsync(key, data);
    return true;
  }

  @Override
  public boolean put(String key, T data) {
    getIgnite().getOrCreateCache(this.getCacheName()).put(key, data);
    return true;
  }

  @Override
  public boolean putWithExpires(String key, T data, int expiresMinutes) {
    getIgniteCacheWithExpiry(expiresMinutes).put(key, data);
    return true;
  }

  @Override
  public void putAffinity(AffinityKey<String> key, T data) {
    getIgnite().getOrCreateCache(this.getCacheName()).put(key, data);
  }

  @Override
  public void putAll(Map<String, T> dataMap) {
    getIgnite().getOrCreateCache(this.getCacheName()).putAll(dataMap);
  }

  @Override
  public T getAsync(String key) {
    return (T) getIgnite().getOrCreateCache(this.getCacheName()).getAsync(key).get();
  }

  @Override
  public T get(String key) {
    return (T) getIgnite().getOrCreateCache(this.getCacheName()).get(key);
  }

  @Override
  public T getAffinity(AffinityKey<String> key) {
    return (T) getIgnite().getOrCreateCache(this.getCacheName()).getAsync(key).get();
  }

  @Override
  public boolean removeAsync(String key) {
    getIgnite().getOrCreateCache(this.getCacheName()).removeAsync(key);
    return true;
  }

  @Override
  public boolean remove(String key) {
    getIgnite().getOrCreateCache(this.getCacheName()).remove(key);
    return true;
  }

  @Override
  public boolean removeAffinity(AffinityKey<String> key) {
    getIgnite().getOrCreateCache(this.getCacheName()).remove(key);
    return true;
  }

  /**
   * 功能：查询获取对象集合列表.
   *
   * @param sqlQuery SqlQuery类型.
   */
  @Override
  public List<T> getListByQuery(SqlQuery sqlQuery) {
    List<T> result = new ArrayList<>();
    try {
      logger.debug("getListByQuery({},{})", sqlQuery.getSql(), sqlQuery.getArgs());
      QueryCursor<CacheEntryImpl<String, T>> cursor = getIgnite()
          .getOrCreateCache(this.getCacheName()).query(sqlQuery);
      for (CacheEntryImpl<String, T> cacheEntry : cursor) {
        result.add(cacheEntry.getValue());
      }
    } catch (Exception e) {
      logger.warn("getListByQuery Exception e={}", e);
    }
    return result;
  }

  /**
   * 功能：查询获取对象集合列表.
   *
   * @param igniteSqlQuery SqlQuery类型.
   */
  @Override
  public List<List<?>> getListByFieldQuery(IgniteSqlQuery igniteSqlQuery) {
    SqlFieldsQuery sqlFieldsQuery = new SqlFieldsQuery(igniteSqlQuery.getSql().toString());
    //兼容无参数过滤的检索(即检索全部数据).
    if (igniteSqlQuery.getArgs() != null && igniteSqlQuery.getArgs().size() > 0) {
      sqlFieldsQuery.setArgs(igniteSqlQuery.getArgs().toArray());
    }
    try {
      logger.debug("getListByFieldQuery({},{})", sqlFieldsQuery.getSql(),
          sqlFieldsQuery.getArgs());
      QueryCursor<List<?>> cursor = getIgnite().getOrCreateCache(this.getCacheName())
          .query(sqlFieldsQuery);
      List<List<?>> res = cursor.getAll();
      return res;
    } catch (Exception e) {
      logger.warn("getListByFieldQuery Exception e={}", e);
    }
    return null;
  }

  @Override
  public boolean pullData(boolean overWriteFlag, Map<String, Object> dataMap) {
    IgniteDataStreamer igniteDataStreamer = getIgnite()
        .dataStreamer(getIgnite().getOrCreateCache(this.getCacheName()).getName());
    if (dataMap != null && dataMap.size() > 0) {
      //允许覆写内存中已经有的数据.
      igniteDataStreamer.allowOverwrite(overWriteFlag);
      for (String key : dataMap.keySet()) {
        igniteDataStreamer.addData(key, dataMap.get(key));
      }
    }
    return true;
  }

  @Override
  public long executeDMLSql(IgniteSqlQuery igniteSqlQuery) {
    SqlFieldsQuery sqlFieldsQuery = new SqlFieldsQuery(igniteSqlQuery.getSql().toString());
    //兼容无参数过滤的检索(即检索全部数据).
    if (igniteSqlQuery.getArgs() != null && igniteSqlQuery.getArgs().size() > 0) {
      sqlFieldsQuery.setArgs(igniteSqlQuery.getArgs().toArray());
    }
    try {
      logger.debug("executeDMLSql({},{})", sqlFieldsQuery.getSql(),
          sqlFieldsQuery.getArgs());
      QueryCursor<List<?>> cursor = getIgnite().getOrCreateCache(this.getCacheName())
          .query(sqlFieldsQuery);
      List<List<?>> resultRows = cursor.getAll();
      return getDMLExecutionResult(resultRows);
    } catch (Exception e) {
      logger.warn("executeDMLSql Exception e={}", e);
    }
    return 0;
  }

  /**
   * 功能：获取Ignite存储序列的唯一标示.
   */
  @Override
  public long getCacheId(String seqName) {
    return getBatchCacheId(seqName, 1);
  }

  @Override
  public long getBatchCacheId(String seqName, int batchSize) {
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    IgniteAtomicSequence seq = getIgnite()
        .atomicSequence(seqName + "_seq",
            Long.valueOf(format.format(new Date()) + "00000"), true);
    return seq.getAndAdd(batchSize);
  }

  protected IgniteCache<String, T> getIgniteCacheWithExpiry(int expiresMinutes) {
    CacheConfiguration cacheConfiguration = new CacheConfiguration(this.getCacheName());
    cacheConfiguration.setExpiryPolicyFactory(
        FactoryBuilder
            .factoryOf(new CreatedExpiryPolicy(new Duration(MINUTES, expiresMinutes))));
    return getIgnite().getOrCreateCache(cacheConfiguration);
  }

  private long getDMLExecutionResult(List<List<?>> resultRows) {
    List<?> row = null;
    if (resultRows != null && resultRows.size() > 0) {
      row = resultRows.get(0);
    }
    return row != null && row.get(0) != null ? Long.parseLong(row.get(0).toString()) : 0;
  }

  /**
   * 对象转字符串  为空直接返回null
   *
   * @param obj 对象
   * @return 字符串/null
   */
  protected String object2String(Object obj) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  /**
   * 对象转Boolean  为空直接返回null
   *
   * @param obj 对象
   * @return Boolean/null
   */
  protected Boolean object2Boolean(Object obj) {
    if (obj == null) {
      return null;
    }
    return (Boolean) obj;
  }

  /**
   * 对象转Double  为空直接返回null
   *
   * @param obj 对象
   * @return Double/null
   */
  protected Double object2Double(Object obj) {
    if (obj == null) {
      return null;
    }
    return (Double) obj;
  }

  /**
   * 对象转Double  为空直接返回null
   *
   * @param obj 对象
   * @return Double/null
   */
  protected Long object2Long(Object obj) {
    if (obj == null) {
      return null;
    }
    return (Long) obj;
  }

  /**
   * 对象转int 为空直接返回0
   *
   * @param obj 对象
   * @return 数字/0
   */
  protected int object2int(Object obj) {
    if (obj == null) {
      return 0;
    }
    try {
      return Integer.parseInt(obj.toString());
    } catch (NumberFormatException e) {
      e.printStackTrace();
      logger.warn("object2int({}) exception {}", obj, e);
      return 0;
    }
  }
}
