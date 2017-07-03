package com.go2going.InterfaceApi;

import com.go2going.WebSocketBase;

/**
 * 对api接口的订阅
 * Created by BlueT on 2017/7/3.
 */
public interface Subject {
    void subscription(WebSocketBase webSocketBase);

    void unsubscribe(WebSocketBase webSocketBase);

    void handler(String json);
}
