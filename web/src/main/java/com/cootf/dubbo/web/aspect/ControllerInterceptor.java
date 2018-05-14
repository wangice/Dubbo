package com.cootf.dubbo.web.aspect;

import com.cootf.dubbo.misc.Misc;
import com.cootf.dubbo.web.core.Rsp;
import com.cootf.dubbo.web.core.Rsp.RspErr;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/5/8 19:43
 */
@Aspect
@Component
public class ControllerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

  @Pointcut("@annotation(com.cootf.dubbo.component.annotation.ServletInitLog)")
  public void controllerMethodPointcut() {
  }

  @Around("controllerMethodPointcut()")
  public Object Interceptor(ProceedingJoinPoint pjp) throws Exception {
    logger.info("拦截");
    Object result = null;
    Object[] args = pjp.getArgs();
    HttpServletRequest request = (HttpServletRequest) args[0];
    HttpServletResponse response = (HttpServletResponse) args[1];
    try {
      result = pjp.proceed(args);
    } catch (Throwable throwable) {
      if (logger.isDebugEnabled()) {
        logger.debug("{}", Misc.trace(throwable));
      }
      return new Rsp(RspErr.ERR_SYSTEM_EXCEPTION);
    }

    return result;
  }
}
