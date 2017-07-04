package com.go2going.interfaceApi;

import com.go2going.websocket.WebSocketBase;
import org.apache.log4j.Logger;

/**
 * 订阅BTC成交记录
 * Created by BlueT on 2017/7/3.
 */
@ApiAnnotation(api="ok_sub_spotcny_btc_trades")
public class TradeBTCRecord extends AbstractSubject{
    private static final Logger LOGGER = Logger.getLogger(TradeBTCRecord.class);

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
        System.out.println("wq="+json);
    }
}
