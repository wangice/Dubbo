package com.ice;

import com.cootf.dubbo.misc.Misc;

/**
 * @author:ice
 * @Date: 2018/5/9 10:01
 */
public class GsonTest {


  public static void main(String[] args) {
    User user = new User();
    user.setName("ice");
    String s = Misc.gson.toJson(user);
    System.out.println(s);
  }

  public static class User {

    private String user;

    private String name;

    public String getUser() {
      return user;
    }

    public void setUser(String user) {
      this.user = user;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
