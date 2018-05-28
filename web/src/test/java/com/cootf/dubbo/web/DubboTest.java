package com.cootf.dubbo.web;

import com.cootf.dubbo.misc.ODateu;
import java.util.Date;
import org.junit.Test;

/**
 * @author:ice
 * @Date: 2018/5/22 10:27
 */
public class DubboTest {

  @Test
  public void test1() {
    Double d = new Double(12);
    String s = Double.toHexString(12);
    System.out.println(ODateu.parseDateyyyy_MM_dd(new Date(1527091200L * 1000)));
  }
}
