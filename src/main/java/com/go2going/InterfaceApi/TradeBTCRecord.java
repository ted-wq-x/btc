package com.go2going.InterfaceApi;

import com.go2going.WebSocketBase;
import org.apache.log4j.Logger;

/**
 * 订阅BTC成交记录
 * Created by BlueT on 2017/7/3.
 */
public class TradeBTCRecord implements Subject{
    private static final Logger LOGGER = Logger.getLogger(TradeBTCRecord.class);
    private  final String sbj="ok_sub_spotcny_btc_trades";

    @Override
    public void subscription(WebSocketBase webSocketBase) {
        webSocketBase.addChannel(sbj);
    }

    @Override
    public void unsubscribe(WebSocketBase webSocketBase) {
        webSocketBase.removeChannel(sbj);
    }

    @Override
    public void handler(String json) {

    }
}
