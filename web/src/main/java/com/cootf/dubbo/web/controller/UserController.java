package com.cootf.dubbo.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cootf.dubbo.entities.Person;
import com.cootf.dubbo.misc.Misc;
import com.cootf.dubbo.service.UserService;
import com.cootf.dubbo.web.core.DubboTrans;
import com.cootf.dubbo.web.core.Rsp.RspErr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/5/9 11:07
 */
@Component
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Reference(version = "2.0.0")
  private UserService userService;

  public void login(DubboTrans trans) {
    try {
      String name = trans.getParStr("name");
      if (name == null) {
        trans.end(RspErr.ERR_MISSING_PARAMS);
        return;
      }
      logger.info("user({}) request login", name);
      Person person = userService.login(name);
      if (person == null) {
        trans.end(RspErr.ERR_NOT_FOUND_USER);
        return;
      }
      trans.end(person);
      logger.info("user({}) login success,response content: {}", name, Misc.obj2json(person));
    } catch (Exception e) {
      logger.info("exception: {}", Misc.trace(e));
    }
  }


  /**
   * 保存失败.
   */
  public void save(DubboTrans trans) {
    try {
      String name = trans.getParStr("name");
      Integer cityId = trans.getParInteger("cityId");
      if (name == null && cityId == null) {
        trans.end(RspErr.ERR_MISSING_PARAMS);
        return;
      }
      Person person = new Person();
      person.setName(name);
      person.setCity_id((long) cityId);
      boolean success = userService.save(person);
      if (success) {/* 保存. */
        trans.end(RspErr.ERR_NONE);
        return;
      }
      trans.end(RspErr.ERR_FAIL);
    } catch (Exception e) {
      logger.info("exception:{}", Misc.trace(e));
    }
  }

}
