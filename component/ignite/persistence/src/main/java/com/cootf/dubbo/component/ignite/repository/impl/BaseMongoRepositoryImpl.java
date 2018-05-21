package com.cootf.dubbo.component.ignite.repository.impl;

import com.cootf.dubbo.component.ignite.configuration.MongoConfiguration;
import com.cootf.dubbo.component.ignite.repository.BaseMongoRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author:ice
 * @Date: 2018/5/15 14:59
 */
public abstract class BaseMongoRepositoryImpl<T> implements BaseMongoRepository<T> {

  protected static final String ID_FILED = "_id";
  protected static final String MONGODB_$_GT = "$gt";
  protected static final String MONGODB_$_SET = "$set";
  //
  protected int pageSize = 1000;
  //
  protected MongoDatabase mongoDatabase = null;
  protected MongoCollection<Document> mongoCollection = null;
  @Autowired
  private MongoConfiguration mongoConfiguration;
  @Autowired
  private MongoClient mongoClient;

  protected abstract String getDataBaseName();

  protected abstract String getCollectionName();

  public abstract void remove(String key);

  public abstract Document transEntityToDoc(T entity);

  public abstract T transDocToEntity(Document doc);


  @Override
  public int getPageSize() {
    return mongoConfiguration.getPageSize();
  }

  @Override
  public List<T> findAll() {
    FindIterable<Document> documents = getMongoCollection().find();
    List<T> datas = transIterableToList(documents);
    return datas;
  }

  @Override
  public long countAll() {
    return getMongoCollection().count();
  }

  /**
   * 单个更新
   */
  protected long updateOne(Bson match, Document update) {
    UpdateOptions options = new UpdateOptions();
    options.upsert(true);
    return getMongoCollection()
        .updateOne(match, new Document(BaseMongoRepositoryImpl.MONGODB_$_SET, update), options)
        .getModifiedCount();
  }

  protected void removeData(Document doc) {
    if (doc != null) {
      getMongoCollection().deleteOne(doc);
    }
  }

  /**
   * 查询单个
   */
  protected T findOne(Bson match) {
    Document doc = getMongoCollection().find(match).first();
    return transDocToEntity(doc);
  }

  /**
   * 根据ID升序排序,并分页检索
   */
  protected List<T> getListBySortQuery(String startId, int pageSize) {
    BasicDBObject field = null;
    if (!StringUtils.isEmpty(startId)) {
      BasicDBObject query = new BasicDBObject();
      query.put(MONGODB_$_GT, new ObjectId(startId));
      field = new BasicDBObject();
      field.put(ID_FILED, query);
    }
    //排序属性集合
    BasicDBObject sort = new BasicDBObject();
    sort.put(ID_FILED, 1);
    FindIterable<Document> docs = null;
    if (!StringUtils.isEmpty(startId)) {
      docs = getMongoCollection().find(field).sort(sort).limit(pageSize);
    } else {
      docs = getMongoCollection().find().sort(sort).limit(pageSize);
    }
    return transIterableToList(docs);
  }

  private List<T> transIterableToList(FindIterable<Document> documents) {
    List<T> data = new ArrayList<>();
    for (Document doc : documents) {
      T item = transDocToEntity(doc);
      if (item != null) {
        data.add(item);
      }
    }
    return data;
  }

  protected MongoCollection<Document> getMongoCollection() {
    if (mongoCollection == null) {
      if (mongoDatabase == null) {
        mongoDatabase = mongoClient.getDatabase(getDataBaseName());
      }
      mongoCollection = mongoDatabase.getCollection(getCollectionName());
    }
    return mongoCollection;
  }
}
