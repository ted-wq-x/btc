package com.go2going.common;

/**
 * 自定义异常
 * Created by wangq on 2017/7/12.
 */
public class Go2goingException extends RuntimeException  {

  public Go2goingException() {
  }

  public Go2goingException(String message) {
    super(message);
  }

  public Go2goingException(String message, Throwable cause) {
    super(message, cause);
  }

  public Go2goingException(Throwable cause) {
    super(cause);
  }

  public Go2goingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
