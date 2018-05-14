package com.cootf.dubbo.persistence.entities;

/**
 * @author:ice
 * @Date: 2018/5/11 12:31
 */
public class Person {

  private Long id;

  private String name;

  private Long city_id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
