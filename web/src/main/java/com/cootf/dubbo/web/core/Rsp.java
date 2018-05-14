package com.cootf.dubbo.web.core;

import java.io.Serializable;

public class Rsp {

  public enum RspErr implements Serializable {

    ERR_NONE(0x0000, "SUCCESS"), /* 成功. */

    ERR_FAIL(0x0001, "失败"),/* 失败. */

    ERR_TIMEOUT(0x0002, "超时"),/* 超时. */

    ERR_SYSTEM_EXCEPTION(0x0003, "服务器异常"),/* 异常. */

    ERR_NOT_FOUND_ACTION(0x0010, "没有找到该方法"),/* 没有找到该方法. */
    //
    ERR_END(0xFFFF, "结束");

    private int num = 0;

    private String desc;

    private RspErr(int num, String desc) {
      this.num = num;
      this.desc = desc;
    }

    public int getNum() {
      return num;
    }

    public void setNum(int num) {
      this.num = num;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }

  private int err;

  private String desc;

  private Object dat;

  public Rsp() {

  }

  public Rsp(RspErr rspErr) {
    this.err = rspErr.getNum();
    this.desc = rspErr.getDesc();
  }

  public int getErr() {
    return err;
  }

  public void setErr(int err) {
    this.err = err;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Object getDat() {
    return dat;
  }

  public void setDat(Object dat) {
    this.dat = dat;
  }
}
