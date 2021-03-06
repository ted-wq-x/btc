package com.go2going.okcoin.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.go2going.ApplicationStartUp;
import com.go2going.okcoin.interfaceApi.Subject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by BlueT on 2017/7/3.
 */
@Component
public class BuissnesWebSocketServiceImpl implements WebSocketService {
  private Logger LOGGER = Logger.getLogger(BuissnesWebSocketServiceImpl.class);

  @Resource
  private ApplicationStartUp applicationStartUp;

  @Override
  public void onReceive(String msg) {
    LOGGER.info("WebSocket Client received message: " + msg);
    JSONObject objects;
    try {
      objects = JSON.parseObject(msg);
    } catch (Exception e) {
      String substring = msg.substring(1, msg.length() - 1);
      objects = JSON.parseObject(substring);
    }

    Object data1 = objects.get("data");
    if (data1 instanceof JSONArray) {
      JSONArray data = objects.getJSONArray("data");
      if (data == null) {
        return;
      }
      String channel = objects.getString("channel");
      Class apiClass = applicationStartUp.getApiClass(channel);
      if (apiClass != null) {
        try {
          Subject o = (Subject) apiClass.newInstance();
          o.handler(data.toJSONString());
        } catch (InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    } else if (data1 instanceof JSONObject) {
      LOGGER.debug("is not data and ignore:" + data1.toString());
    }
  }
}