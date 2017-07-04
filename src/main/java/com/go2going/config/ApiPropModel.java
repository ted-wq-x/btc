package com.go2going.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 订阅的api模型
 * Created by BlueT on 2017/7/4.
 */
@Component
@ConfigurationProperties(prefix = "channels")
public class ApiPropModel {
    private String[] channels;

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }
}
