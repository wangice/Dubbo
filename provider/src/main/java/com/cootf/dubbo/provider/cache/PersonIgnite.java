package com.cootf.dubbo.provider.cache;

import com.cootf.dubbo.component.ignite.common.IgniteCacheStrategy;
import com.cootf.dubbo.entities.Person;
import java.util.List;

/**
 * @author:ice
 * @Date: 2018/5/11 14:18
 */
public interface PersonIgnite extends IgniteCacheStrategy<Person> {

  List<Person> findOneByKey(String id);

  boolean savePerson(Person person);

  Person findPersonByName(String name);
}
