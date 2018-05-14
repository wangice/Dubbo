package com.cootf.dubbo.web.core;

import com.cootf.dubbo.misc.Misc;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MsgCfg {

  private static final Logger logger = LoggerFactory.getLogger(MsgCfg.class);

  private Map<String, CallBackStub> msgs = new HashMap<>();

  public boolean init() {
    File file[] = new File(this.getClass().getClassLoader().getResource("msgs").getPath())
        .listFiles();
    for (int i = 0; i < file.length; ++i) {
      ArrayList<CallBackStub> arr = this.loadCbs(file[i].getPath());
      if (arr == null) {
        return false;
      }
      for (CallBackStub cbs : arr) {
        if (!this.addCbs(cbs)) {
          return false;
        }
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  public ArrayList<CallBackStub> loadCbs(String path) {
    try {
      ArrayList<CallBackStub> arr = new ArrayList<>();
      Document root = new SAXReader().read(path);
      Element e = root.getRootElement();
      List<Element> msgs = e.elements("msgs");
      for (int i = 0; i < msgs.size(); ++i) {
        Element tmp = msgs.get(i);
        Class<?> cls = Class.forName(tmp.attributeValue("class"));
        String clsName = cls.getName()
            .substring(cls.getName().lastIndexOf(".") + 1, cls.getName().length());
        clsName = clsName.substring(0, 1).toLowerCase() + clsName.substring(1, clsName.length());
        Object bean = SpringContextUtils.getBean(clsName);
        List<Element> msg = tmp.elements("msg");
        for (int k = 0; k < msg.size(); ++k) {
          Method mth = Misc
              .findMethodByName(bean.getClass(), msg.get(k).attributeValue("method"));
          boolean login = !("false".equals(msg.get(k).attributeValue("login")));
          if (mth == null) {
            return null;
          }
          System.out.println(mth + "," + cls.getName());
          arr.add(new CallBackStub(mth, cls, login));
        }
      }
      return arr.size() < 1 ? null : arr;
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("exception: {}", Misc.trace(e));
      }
      return null;
    }
  }

  /**
   * 尝试添加一个新的消息回调.
   */
  public boolean addCbs(CallBackStub cbs) {
    if (this.msgs.containsKey(cbs.getMth().getName())) {
      return false;
    }
    this.msgs.put(cbs.mth.getName(), cbs);
    return true;
  }

  public static class CallBackStub {

    private Method mth;//调用的方法.
    private Class<?> cls;//调用方法的类
    private boolean login;//是否登录.

    public CallBackStub(Method mth, Class<?> cls, boolean login) {
      this.mth = mth;
      this.cls = cls;
      this.login = login;
    }

    public Method getMth() {
      return mth;
    }

    public void setMth(Method mth) {
      this.mth = mth;
    }

    public Class<?> getCls() {
      return cls;
    }

    public void setCls(Class<?> cls) {
      this.cls = cls;
    }

    public boolean isLogin() {
      return login;
    }

    public void setLogin(boolean login) {
      this.login = login;
    }
  }


  public Map<String, CallBackStub> getMsgs() {
    return msgs;
  }

  public void setMsgs(Map<String, CallBackStub> msgs) {
    this.msgs = msgs;
  }
}
