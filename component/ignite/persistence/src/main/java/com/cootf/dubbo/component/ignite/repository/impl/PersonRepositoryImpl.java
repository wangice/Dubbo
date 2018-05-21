package com.cootf.dubbo.component.ignite.repository.impl;

import com.cootf.dubbo.component.contents.MongodbContent;
import com.cootf.dubbo.component.ignite.repository.PersonRepository;
import com.cootf.dubbo.entities.Person;
import com.mongodb.BasicDBObject;
import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.bson.Document;
import org.springframework.stereotype.Repository;

/**
 * @author:ice
 * @Date: 2018/5/15 16:20
 */
@Repository
public class PersonRepositoryImpl extends BaseMongoRepositoryImpl<Person> implements
    PersonRepository {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String CITY_ID = "city_id";

  @Override
  protected String getDataBaseName() {
    return MongodbContent.DB_DUBBO;
  }

  @Override
  protected String getCollectionName() {
    return MongodbContent.TB_PERSON;
  }

  @Override
  public void remove(String key) {
    Document document = new Document(ID_FILED, key);
    getMongoCollection().deleteOne(document);
  }

  @Override
  public List<Person> getListByQuery(String startId, int pageSize) {
    return super.getListBySortQuery(startId, pageSize);
  }

  @Override
  public Document transEntityToDoc(Person entity) {
    if (entity == null) {
      return null;
    }
    Document document = new Document();
    document.put(ID, entity.getId());
    document.put(NAME, entity.getName());
    document.put(CITY_ID, entity.getCity_id());
    return document;
  }

  @Override
  public Person transDocToEntity(Document doc) {
    if (doc == null || doc.size() == 0) {
      return null;
    }
    Person person = new Person();
    person.setId(ObjectUtils.toString(doc.get(ID_FILED)));
    person.setName(doc.getString(NAME));
    person.setCity_id(doc.getLong(CITY_ID));
    return person;
  }

  @Override
  public Person findById(String key) {
    return super.findOne(new BasicDBObject(ID_FILED, key));
  }

  @Override
  public void savePerson(Person person) {
    Document document = transEntityToDoc(person);
    String name = person.getName();
    super.updateOne(new BasicDBObject(NAME, name), document);
  }
}
