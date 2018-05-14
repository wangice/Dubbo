package com.cootf.dubbo.web.util;

import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;

/**
 * @author:ice
 * @Date: 2018/5/9 19:58
 */
public class MiscUtil {

  /**
   * 将json字符串转化为Map
   */
  public static void json2Map(Map<String, String> map, JSONObject object) {
    Set set = object.keySet();
    for (Object o : set) {
      String key = (String) o;
      String s = object.getString(key);
      if (s.indexOf("{") == 0 && s.lastIndexOf("}") == s.length() - 1) {//对象.
        JSONObject oo = MiscUtil.fromObject(s);
        if (oo == null) {
          map.put(key, s);
        } else {
          MiscUtil.json2Map(map, oo);
        }
      } else {
        map.put(key, s);
      }
    }
  }

  /**
   * 将json字符串转化为JSONObject,异常返回null
   */
  public static JSONObject fromObject(String jsonStr) {
    try {
      JSONObject object = JSONObject.fromObject(jsonStr);
      return object;
    } catch (Exception e) {
      return null;
    }
  }
}
