package com.cootf.dubbo.provider;

import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author:ice
 * @Date: 2018/5/4 20:03
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
@ImportResource("classpath*:ignite-config-${spring.profiles.active}.xml")
public class DubboDataApplication {

  public static final AtomicBoolean RUN = new AtomicBoolean(true);

  public static void main(String[] args) {
    SpringApplication.run(DubboDataApplication.class, args);
    synchronized (DubboDataApplication.class) {
      while (RUN.get()) {
        try {
          DubboDataApplication.class.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
