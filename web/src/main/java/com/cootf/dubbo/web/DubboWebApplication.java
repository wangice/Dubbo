package com.cootf.dubbo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author:ice
 * @Date: 2018/5/7 15:52
 */
@SpringBootApplication
public class DubboWebApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(DubboWebApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(DubboWebApplication.class);
  }
}
