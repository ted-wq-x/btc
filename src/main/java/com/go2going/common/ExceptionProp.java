package com.go2going.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 错误码配置
 * Created by wangq on 2017/7/12.
 */
@Component
@ConfigurationProperties()
public class ExceptionProp {
  private Map<String, String> errorCode;

  public Map<String, String> getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Map<String, String> errorCode) {
    this.errorCode = errorCode;
  }
}
