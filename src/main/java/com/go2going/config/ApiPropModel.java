package com.go2going.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 订阅的api模型
 * Created by BlueT on 2017/7/4.
 */
@Component
@Validated
@ConfigurationProperties()
public class ApiPropModel {
  @NotNull
  private String[] channels;
  @NotNull
  private Map<String, Double> remind;

  public Map<String, Double> getRemind() {
    return remind;
  }

  public void setRemind(Map<String, Double> remind) {
    this.remind = remind;
  }

  public String[] getChannels() {
    return channels;
  }

  public void setChannels(String[] channels) {
    this.channels = channels;
  }
}
