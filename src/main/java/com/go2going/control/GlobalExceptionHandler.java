package com.go2going.control;

import com.go2going.common.ExceptionProp;
import com.go2going.common.Go2goingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 全局异常处理
 * Created by wangq on 2017/7/12.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Resource
  private ExceptionProp exceptionProp;

  @ExceptionHandler(value = Go2goingException.class)
  public ModelAndView tipException(Exception e) {
    LOGGER.error("find exception:e={}", e.getMessage());
    LOGGER.trace(e.getMessage(), e);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("comm/error_404");
    String codeMsg = exceptionProp.getErrorCode().get(e.getMessage());
    if (codeMsg != null) {
      modelAndView.addObject("exception", codeMsg);
    } else {
      modelAndView.addObject("exception",e.getMessage());
    }

    return modelAndView;
  }

  @ExceptionHandler(value = Exception.class)
  public ModelAndView exception(Exception e) {
    LOGGER.error("find exception:e={}", e.getMessage());
    LOGGER.trace(e.getMessage(),e);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("comm/error_500");
    modelAndView.addObject("exception", e.getMessage());
    return modelAndView;
  }
}
