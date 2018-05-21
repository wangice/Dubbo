package com.cootf.dubbo.component.ignite.store;

import com.cootf.dubbo.component.ignite.repository.PersonRepository;
import com.cootf.dubbo.entities.Person;
import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 当ignite调用put get remove方法是会调用write load delete方法 在调用这些时，配置文件一定配置了通读通写
 *
 * @author:ice
 * @Date: 2018/5/15 14:46
 */
@Component("cacheDbPersonStore")
public class CacheDbPersonStore extends CacheStoreAdapter<String, Person> {

  @Autowired
  private PersonRepository personRepository;

  @Override
  public Person load(String s) throws CacheLoaderException {
    return personRepository.findById(s);
  }

  @Override
  public void write(Entry<? extends String, ? extends Person> entry) throws CacheWriterException {
    personRepository.savePerson(entry.getValue());
  }

  @Override
  public void delete(Object o) throws CacheWriterException {
    String id = ObjectUtils.toString(o);
    personRepository.remove(id);
  }

}
