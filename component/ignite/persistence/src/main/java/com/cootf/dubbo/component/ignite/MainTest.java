package com.cootf.dubbo.component.ignite;

import com.cootf.dubbo.entities.Person;
import java.util.Collection;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.internal.IgnitionEx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author:ice
 * @Date: 2018/5/11 15:37
 */
public class MainTest {

  public static void main(String[] args) {
    System.setProperty("spring.profiles.active", "dev");
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
        "ignite-config-dev.xml");
    String[] names = applicationContext.getBeanDefinitionNames();
    for (String beanName : names) {
      System.out.println(applicationContext.getBean(beanName).getClass());
    }

    try {
      Ignite ignite = IgnitionEx.start("ignite-config-dev.xml");
      Collection<String> cacheNames = ignite.cacheNames();
      if (cacheNames != null && cacheNames.size() > 0) {
        for (String key : cacheNames) {
          ignite.cache(key).loadCache(null, key);
        }
      }
      Person person = new Person();
      person.setId("1");
      person.setName("ice");
      person.setCity_id(1L);
      ignite.getOrCreateCache("Person").put(1L, person);
      FieldsQueryCursor<List<?>> cursor = ignite.getOrCreateCache("Person")
          .query(new SqlFieldsQuery("select id,name from person where id = 1"));
      for (List<?> row : cursor) {
        System.out.println("id=" + row.get(0));
        System.out.println("personName=" + row.get(1));
      }
    } catch (IgniteCheckedException e) {
      e.printStackTrace();
    }
  }
}
