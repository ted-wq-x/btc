package com.go2going;

import com.go2going.okcoin.websocket.WebSocketBase;
import com.go2going.okcoin.websocket.WebSocketService;

/**
 * Created by BlueT on 2017/7/3.
 */
public class WebSoketClient extends WebSocketBase {
    public WebSoketClient(String url,WebSocketService service){
        super(url,service);
    }
}