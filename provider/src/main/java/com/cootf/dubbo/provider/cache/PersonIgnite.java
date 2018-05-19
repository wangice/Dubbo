package com.cootf.dubbo.provider.cache;

import com.cootf.dubbo.persistence.common.IgniteCacheStrategy;
import com.cootf.dubbo.persistence.entities.Person;
import java.util.List;

/**
 * @author:ice
 * @Date: 2018/5/11 14:18
 */
public interface PersonIgnite extends IgniteCacheStrategy<Person> {

  List<Person> findOneByKey(String id);

  String savePerson(Person person);
}
