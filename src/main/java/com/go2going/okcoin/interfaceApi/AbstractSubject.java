package com.go2going.okcoin.interfaceApi;

import com.go2going.okcoin.websocket.WebSocketBase;

/**
 * Created by BlueT on 2017/7/4.
 */
public abstract class AbstractSubject implements Subject {

    /**
     * api
     */
    protected static String sbj;

    @Override
    public void subscription(WebSocketBase webSocketBase) {
        webSocketBase.addChannel(sbj);
    }

    @Override
    public void unsubscribe(WebSocketBase webSocketBase) {
        webSocketBase.removeChannel(sbj);
    }
}
