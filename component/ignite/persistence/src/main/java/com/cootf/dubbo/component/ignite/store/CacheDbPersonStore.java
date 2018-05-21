package com.cootf.dubbo.component.ignite.store;

import com.cootf.dubbo.component.ignite.repository.PersonRepository;
import com.cootf.dubbo.entities.Person;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 当ignite调用put get remove方法是会调用write load delete方法 在调用这些时，配置文件一定配置了通读通写
 *
 * @author:ice
 * @Date: 2018/5/15 14:46
 */
@Component("cacheDbPersonStore")
public class CacheDbPersonStore extends CacheStoreAdapter<String, Person> {

  private static final Logger log = LoggerFactory.getLogger(CacheDbPersonStore.class);

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

  @Override
  public void loadCache(IgniteBiInClosure<String, Person> clo, Object... args) {
    long totalSize = personRepository.countAll();
    int counts = 0;

    long start = System.currentTimeMillis();
    if (totalSize < 1) {
      return;
    }
    int listSize = 0;
    int pageSize = personRepository.getPageSize();
    String startId = null;
    int startIndex = 0;
    do {
      Map<String, Person> tempMap = new HashMap<>();
      List<Person> persons = personRepository.getListByQuery(startId, pageSize);
      if (persons != null && persons.size() > 0) {
        listSize += persons.size();
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < persons.size(); i++) {
          Person person = persons.get(i);
          if (!StringUtils.isEmpty(person.getId())) {
            clo.apply(person.getId(), person);
            if (tempMap.containsKey(person.getId())) {
              log.warn("WARN: Repeat Person data is loading. first= {}, repeat= {}",
                  tempMap.get(person.getId()).toString(), person.toString());
            } else {
              counts++;
              tempMap.put(person.getId(), person);
            }
          } else {
            log.warn("WARN: loading a person is failed. person= {}", person.toString());
          }
          startId = persons.get(i).getId();
        }
        long end2 = System.currentTimeMillis();
        log.info(
            "Loading Person Data into cache start= {},size={},in {} ms,currentLoadTotalSize= {}",
            startIndex, persons.size(), (end2 - start2), counts);
        startIndex += persons.size();
      } else {
        break;
      }
    } while (listSize < totalSize);
  }
}
