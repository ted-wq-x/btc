package com.go2going.bitfinex;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：  btc<br>
 * 类名称：  VertStartCore<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2017/10/10 0010 9:30
 */
@Component
public class VertStartCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertStartCore.class);
    @Resource
    private VertClient vertClient=new VertClient();


    public static void main(String[] args) {
        VertStartCore vertStartCore = new VertStartCore();
        vertStartCore.start();
    }
    public void start(){
        Vertx.vertx().deployVerticle(vertClient,result -> {
            if (result.failed()) {
                LOGGER.info("Deployment failed!----------------");
            }
        });


    }
}
