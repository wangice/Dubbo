package com.cootf.dubbo.web.controller;

import com.cootf.dubbo.component.annotation.ServletInitLog;
import com.cootf.dubbo.misc.Misc;
import com.cootf.dubbo.misc.Net;
import com.cootf.dubbo.web.core.DubboActorBlocking;
import com.cootf.dubbo.web.core.DubboTrans;
import com.cootf.dubbo.web.core.MsgCfg;
import com.cootf.dubbo.web.core.MsgCfg.CallBackStub;
import com.cootf.dubbo.web.core.Rsp.RspErr;
import com.cootf.dubbo.web.core.SpringContextUtils;
import com.cootf.dubbo.web.util.MiscUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author:ice
 * @Date: 2018/5/8 20:08
 */
@Controller
public class AppController {

  private static final Logger logger = LoggerFactory.getLogger(AppController.class);

  @Autowired
  private DubboActorBlocking actorBlocking;

  @Autowired
  private MsgCfg msgCfg;

  @ServletInitLog
  @ResponseBody
  @RequestMapping(value = "service", method = RequestMethod.GET)
  public DeferredResult<String> service(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    DeferredResult<String> result = new DeferredResult<>();

    String action = request.getParameter("action");
    //
    CallBackStub callBackStub = msgCfg.getMsgs().get(action);
    if (callBackStub == null) {
      return new DubboTrans(request, result).endNoLog(RspErr.ERR_NOT_FOUND_ACTION);
    }
    this.afterCallBack(callBackStub, request, result);
    return result;
  }

  @ServletInitLog
  @ResponseBody
  @RequestMapping(value = "service", method = RequestMethod.POST)
  public DeferredResult<String> servicePost(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    DeferredResult<String> result = new DeferredResult<>();

    ServletInputStream inputStream = request.getInputStream();//获取实体中类容
//    String read = IOUtils.read(new BufferedReader(new InputStreamReader(inputStream)));
//    System.out.println(read);
    String contentType = request.getContentType();
    if (contentType.equals("application/json")) {//json格式,通过JSONOBJECT将其转化为Map
      String s = Net.convertStreamToString(inputStream);//获取流之后，流就会被关闭,可以进行封装
      Map<String, String> map = new HashMap<>();
      MiscUtil.json2Map(map, JSONObject.fromObject(s));
      System.out.println("字符串：" + Misc.obj2json(map));
    } else if (contentType.equals("form-data") || contentType.equals("x-www-form-urlencoded")) {
      Map<String, String[]> parameterMap = request.getParameterMap();
      System.out.println("map:" + Misc.obj2json(parameterMap));
    } else {//直接从连接中去字段.

    }
    //
//    CallBackStub callBackStub = msgCfg.getMsgs().get(action);
//    if (callBackStub == null) {
//      return new DubboTrans(request, result).endNoLog(RspErr.ERR_NOT_FOUND_ACTION);
//    }
//    this.afterCallBack(callBackStub, request, result);
    return result;
  }

  private void afterCallBack(CallBackStub callBackStub, HttpServletRequest request,
      DeferredResult<String> result) {
    actorBlocking.future(v -> {
      DubboTrans trans = new DubboTrans(request, result);//开启事务.
      try {
        String simpleName = callBackStub.getCls().getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        Object bean = SpringContextUtils.getBean(simpleName);
        callBackStub.getMth().invoke(bean, trans);//执行某个方法
      } catch (IllegalAccessException e) {
        trans.endNoLog(RspErr.ERR_SYSTEM_EXCEPTION);
        if (logger.isErrorEnabled()) {
          logger.error("exception: {}", Misc.trace(e));
        }
      } catch (InvocationTargetException e) {
        trans.endNoLog(RspErr.ERR_SYSTEM_EXCEPTION);
        if (logger.isErrorEnabled()) {
          logger.error("exception: {}", Misc.trace(e));
        }
      }
    });
  }
}
