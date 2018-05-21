package com.cootf.dubbo.component.ignite;

import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ImportResource("classpath*:ignite-config-${spring.profiles.active}.xml")
public class IgniteDataPreLoadingApplication {

  private static AtomicBoolean RUNNING = new AtomicBoolean(true);

  public static void main(String[] args) {
    SpringApplication.run(IgniteDataPreLoadingApplication.class, args);
    synchronized (IgniteDataPreLoadingApplication.class) {
      while (RUNNING.get()) {
        try {
          IgniteDataPreLoadingApplication.class.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
