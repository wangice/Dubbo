package com.cootf.dubbo.component.ignite.repository;

import com.cootf.dubbo.entities.Person;

/**
 * @author:ice
 * @Date: 2018/5/15 14:48
 */
public interface PersonRepository extends BaseMongoRepository<Person> {

  Person findById(String key);

  void savePerson(Person person);
}
