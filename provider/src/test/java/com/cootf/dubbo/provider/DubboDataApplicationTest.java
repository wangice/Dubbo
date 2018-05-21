package com.cootf.dubbo.provider;

import com.cootf.dubbo.entities.Person;
import com.cootf.dubbo.provider.cache.PersonIgnite;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:ice
 * @Date: 2018/5/11 15:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@ImportResource("classpath*:ignite-config-dev.xml")
public class DubboDataApplicationTest {


  @Autowired
  private PersonIgnite personIgnite;

  @Before
  public void setUp() {

  }

  @Test
  public void test() {
    List<Person> persons = personIgnite.findOneByKey(1 + "");
    if (persons.isEmpty()) {
      System.out.println("为空");
    } else {
      System.out.println("数据:" + persons.toString());
    }
  }

}
