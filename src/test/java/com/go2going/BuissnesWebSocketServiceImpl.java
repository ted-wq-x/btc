package com.go2going;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * Created by BlueT on 2017/7/3.
 */
public class BuissnesWebSocketServiceImpl implements WebSocketService{
    private Logger log = Logger.getLogger(BuissnesWebSocketServiceImpl.class);
    @Override
    public void onReceive(String msg){
        log.info("WebSocket Client received message: " + msg);
        String substring = msg.substring(1, msg.length() - 1);
        JSONObject objects = JSON.parseObject(substring);
        String channel = (String) objects.get("channel");

        if ("".equals(channel)) {

        }
    }
}