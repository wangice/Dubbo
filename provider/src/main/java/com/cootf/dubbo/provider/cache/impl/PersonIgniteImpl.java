package com.cootf.dubbo.provider.cache.impl;

import com.cootf.dubbo.persistence.common.IgniteCacheStrategyImpl;
import com.cootf.dubbo.persistence.configuration.IgniteProperties;
import com.cootf.dubbo.persistence.entities.Person;
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
  public String savePerson(Person person) {
    long sequece = super.getCacheId(Person.class.getSimpleName() + "_seq");
    String id = sequece + "";
    person.setId(id);
    super.put(id, person);
    return id;
  }
}
