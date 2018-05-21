package com.cootf.dubbo.component.ignite.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Ignite 基本配置项
 *
 * @author Charles
 * @email amwfhv@yeah.net
 * @create at 2017/11/22 22:38
 **/
@Configuration
public class IgniteProperties {

  /**
   * 系统缓存名称
   */
  @Value("${ignite.systemCacheName:SystemConfigs}")
  private String sysCacheName;
  /**
   * 节点名称
   */
  @Value("${ignite.instanceName:Ignite_Server_Node}")
  private String instanceName;
  /**
   * 是否加载数据到缓存
   */
  @Value("${ignite.loadData:false}")
  private boolean loadData;
  /**
   * 是否是客户端节点 默认false
   */
  @Value("${ignite.clientMode:false}")
  private boolean clientMode;

  /************************************ 缓存名称配置  ***************************************/
  /**
   * SimCard缓存名称
   */
  @Value("${ignite.cache.Person:Person}")
  private String personCacheName;

  /********************************** 缓存名称配置 End *************************************/
  public String getSysCacheName() {
    return sysCacheName;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public boolean isLoadData() {
    return loadData;
  }

  public boolean isClientMode() {
    return clientMode;
  }

  public String getPersonCacheName() {
    return personCacheName;
  }
}
