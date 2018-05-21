package com.cootf.dubbo.component.ignite.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author:ice
 * @Date: 2018/5/15 15:02
 */
@Configuration
public class MongoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MongoConfiguration.class);

  private MongoClient mongoClient;

  @Value("${mongo.addresses}")
  private String mongoAddresses;
  @Value("${mongo.user}")
  private String mongoUser;
  @Value("${mongo.password}")
  private String mongoPassword;
  @Value("${mongo.database}")
  private String mongoDataBase;
  @Value("${mongo.pageSize:1000}")
  private int pageSize;

  @Bean
  public MongoClient mongoClient() {
    log.info("读取到MongoDB连接信息 : addresses = {}", mongoAddresses);
    if (!StringUtils.isEmpty(mongoAddresses)) {
      String[] addresses = mongoAddresses.split(",");
      List<ServerAddress> serverAddressList = new ArrayList<>();
      for (String address : addresses) {
        String[] tempArray = address.split(":");
        serverAddressList.add(new ServerAddress(tempArray[0], Integer.parseInt(tempArray[1])));
      }
      MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
      if (!StringUtils.isEmpty(mongoUser) && !StringUtils.isEmpty(mongoPassword)) {
        MongoCredential credential = MongoCredential
            .createCredential(mongoUser, mongoDataBase, mongoPassword.toCharArray());
        mongoClient = new MongoClient(serverAddressList, Arrays.asList(credential),
            mongoClientOptions);
      } else {
        mongoClient = new MongoClient(serverAddressList, mongoClientOptions);
      }
      log.info("mongodb 连接初始化完毕");
    }
    return mongoClient;
  }

  @PreDestroy
  public void destroy() {
    if (mongoClient != null) {
      mongoClient.close();
      mongoClient = null;
    }
  }

  public int getPageSize() {
    return pageSize;
  }
}
