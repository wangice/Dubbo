package com.cootf.dubbo.web.core;

import com.cootf.dubbo.misc.Net;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @author:ice
 * @Date: 2018/5/9 10:46
 */
public class DubboReq {

  private HttpServletRequest request;

  private Map<String, String> pars = new HashMap<>();

  public DubboReq(HttpServletRequest request) {
    this.request = request;
    this.parsePars();

  }

  /**
   * 将路径转化为Map对象
   */
  private void parsePars() {
    try {
      String pars[] = this.request.getQueryString().split("&");
      if (pars == null || pars.length < 1) {
        return;
      }
      for (int i = 0; i < pars.length; ++i) {
        String kv[] = pars[i].split("=");
        if (kv == null || kv.length < 1) {
          continue;
        }
        this.pars.put(kv[0], kv.length == 1 ? "" : Net.urlDecode(kv[1]));
      }
    } catch (Exception e) {
      return;
    }
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  public Map<String, String> getPars() {
    return pars;
  }

  public void setPars(Map<String, String> pars) {
    this.pars = pars;
  }
}
