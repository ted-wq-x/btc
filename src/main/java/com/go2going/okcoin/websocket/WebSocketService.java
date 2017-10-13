package com.go2going.okcoin.websocket;

/**
 * Created by BlueT on 2017/7/3.
 */
public interface WebSocketService {
    /**
     * @param msg json数据
     */
    void onReceive(String msg)  ;
}