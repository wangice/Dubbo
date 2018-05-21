package com.cootf.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cootf.dubbo.entities.Person;
import com.cootf.dubbo.provider.cache.PersonIgnite;
import com.cootf.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author:ice
 * @Date: 2018/5/4 20:06
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private PersonIgnite personIgnite;

  @Override
  public Person login(String user) {
    return personIgnite.findPersonByName(user);
  }

  @Override
  public boolean save(Person person) {
    return personIgnite.savePerson(person);
  }
}
