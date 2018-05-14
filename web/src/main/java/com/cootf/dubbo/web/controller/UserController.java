package com.cootf.dubbo.web.controller;

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

  public void login(DubboTrans trans) {
    logger.info("登录");
    trans.end(RspErr.ERR_NONE);
  }
}
