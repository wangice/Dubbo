package com.cootf.dubbo.web.core;

import com.cootf.dubbo.misc.Misc;
import com.cootf.dubbo.web.core.Rsp.RspErr;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.async.DeferredResult;

public class DubboTrans {

  /**
   * spring异步返回句柄.
   */
  private DeferredResult<String> def;
  /**
   * 响应.
   */
  private Rsp rsp;

  private DubboReq req;

  public DubboTrans(HttpServletRequest req, DeferredResult<String> def) {
    this.def = def;
    this.req = new DubboReq(req);
  }

  /**
   * 获取参数中的值
   */
  public String getParStr(String par) {
    return Misc.trim(this.req.getPars().get(par));
  }

  /**
   * 获取参数中的值，并转化为Integer
   */
  public Integer getParInteger(String par) {
    return Misc.forceInt0(Misc.trim(this.req.getPars().get(par)));
  }

  public final DeferredResult<String> end(RspErr err) {
    return this.end(err, null);
  }

  public final DeferredResult<String> end(RspErr err, Object o) {
    return this.endImmediate(err, o)/* 事务立即结束. */;
  }

  public final DeferredResult<String> end(Object o) {
    return this.end(RspErr.ERR_NONE, o);
  }

  public final DeferredResult<String> endNoLog(RspErr err) {
    this.rsp = new Rsp();
    this.rsp.setErr(err.getNum());
    this.rsp.setDesc(err.getDesc());
    this.def.setResult(Misc.obj2json(rsp));
    return this.def;
  }

  /**
   * 事务结束.
   */
  public final DeferredResult<String> flush() {
    String r = Misc.obj2json(this.rsp);
    this.def.setResult(r);
    return def;
  }

  /**
   * 事务立即结束.
   */
  private final DeferredResult<String> endImmediate(RspErr err, Object o) {
    this.rsp = new Rsp();
    this.rsp.setErr(err.getNum());
    this.rsp.setDesc(err.getDesc());
    if (o != null) {
      this.rsp.setDat(o);
    }
    return this.flush();
  }

  public DeferredResult<String> getDef() {
    return def;
  }

  public void setDef(DeferredResult<String> def) {
    this.def = def;
  }

  public Rsp getRsp() {
    return rsp;
  }

  public void setRsp(Rsp rsp) {
    this.rsp = rsp;
  }
}
