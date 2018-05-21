package com.cootf.dubbo.provider.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.cootf.dubbo.misc.Misc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ice
 * @Date: 2018/5/19 17:23
 */
@Activate(group = Constants.PROVIDER, value = "ipFilter")
public class AuthorityFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(AuthorityFilter.class);


  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    log.info("remote url: {}", Misc.obj2json(RpcContext.getContext().getUrl()));
    return invoker.invoke(invocation);
  }
}
