package com.go2going.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.go2going.ApplicationStartUp;
import com.go2going.interfaceApi.Subject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by BlueT on 2017/7/3.
 */
@Component
public class BuissnesWebSocketServiceImpl implements WebSocketService {
    private Logger log = Logger.getLogger(BuissnesWebSocketServiceImpl.class);

    @Resource
    private ApplicationStartUp applicationStartUp;
    @Override
    public void onReceive(String msg){
        log.info("WebSocket Client received message: " + msg);
        String substring = msg.substring(1, msg.length() - 1);
        JSONObject objects = JSON.parseObject(substring);
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
    }
}