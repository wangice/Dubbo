package com.cootf.dubbo.component.ignite.repository;

import java.util.List;
import org.bson.Document;

/**
 * @author:ice
 * @Date: 2018/5/15 14:50
 */
public interface BaseMongoRepository<T> {

  void remove(String id);

  int getPageSize();

  List<T> getListByQuery(String startId, int pageSize);

  List<T> findAll();

  long countAll();

  /**
   * 功能：转换数据对象为文档.
   */
  Document transEntityToDoc(T data);

  /**
   * 功能：转换文档为数据对象.
   */
  T transDocToEntity(Document doc);
}
