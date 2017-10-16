package com.go2going.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 错误码配置
 * Created by wangq on 2017/7/12.
 */
@Component
@Validated
@ConfigurationProperties
public class ExceptionProp {

  @NotNull
  private Map<String, String> errorCode;

//  @Deprecated
  public Map<String, String> getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Map<String, String> errorCode) {
    this.errorCode = errorCode;
  }

  public String getCodeMsg(String code) {
    return errorCode.get(code);
  }
}
