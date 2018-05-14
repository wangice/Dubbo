package com.cootf.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cootf.dubbo.service.UserService;

/**
 * @author:ice
 * @Date: 2018/5/4 20:06
 */
@Service
public class UserServiceImpl implements UserService {

  @Override
  public String login(String user) {
    System.out.println("是");
    return "收拾";
  }
}
