package com.cootf.dubbo.provider.cache.impl;

import com.cootf.dubbo.component.ignite.common.IgniteCacheStrategyImpl;
import com.cootf.dubbo.component.ignite.configuration.IgniteProperties;
import com.cootf.dubbo.entities.Person;
import com.cootf.dubbo.provider.cache.PersonIgnite;
import com.cootf.dubbo.provider.configuration.IgniteAutoConfiguration;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.cache.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author:ice
 * @Date: 2018/5/11 14:19
 */
@Repository
public class PersonIgniteImpl extends IgniteCacheStrategyImpl<Person> implements PersonIgnite {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String CITYID = "city_id";


  @Autowired
  private IgniteProperties igniteProperties;

  @Autowired
  private IgniteAutoConfiguration igniteAutoConfiguration;

  @Override
  public String getCacheName() {
    return igniteProperties.getPersonCacheName();
  }

  public Ignite getIgnite() {
    return igniteAutoConfiguration.getIgnite();
  }

  @Override
  public List<Person> findOneByKey(String id) {
    SqlQuery sqlQuery = new SqlQuery(Person.class, PersonIgniteImpl.ID + "=? ");
    sqlQuery.setAlias(id);
    return super.getListByQuery(sqlQuery);
  }

  @Override
  public boolean savePerson(Person person) {
    long sequece = super.getCacheId(Person.class.getSimpleName() + "_seq");
    String id = sequece + "";
    person.setId(id);
    return super.put(id, person);
  }

  @Override
  public Person findPersonByName(String name) {
    SqlQuery sqlQuery = new SqlQuery(Person.class, PersonIgniteImpl.NAME + "=? ");
    sqlQuery.setArgs(name);
    List<Person> persons = super.getListByQuery(sqlQuery);
    return persons.isEmpty() ? null : persons.get(0);
  }
}
