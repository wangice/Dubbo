package com.cootf.dubbo.service;

import com.cootf.dubbo.entities.Person;

/**
 * @author:ice
 * @Date: 2018/5/4 20:00
 */
public interface UserService {

  /**
   * 用户登录
   */
  Person login(String user);

  /**
   * 保存用户
   */
  boolean save(Person person);
}
