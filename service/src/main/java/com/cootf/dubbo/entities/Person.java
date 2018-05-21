package com.cootf.dubbo.entities;

import java.io.Serializable;

/**
 * @author:ice
 * @Date: 2018/5/11 12:31
 */
public class Person implements Serializable {

  private static final long serialVersionUID = 391859729968255137L;
  private String id;

  private String name;

  private Long city_id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getCity_id() {
    return city_id;
  }

  public void setCity_id(Long city_id) {
    this.city_id = city_id;
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", city_id=" + city_id +
        '}';
  }
}
