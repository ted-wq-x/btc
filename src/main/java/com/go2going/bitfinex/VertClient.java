package com.go2going.bitfinex;

import com.alibaba.fastjson.JSONObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.*;
import io.vertx.core.json.Json;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：  btc<br>
 * 类名称：  VertClient<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2017/10/10 0010 9:01
 */
@Component
public class VertClient extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertClient.class);

    @Value("${bitfinex}")
    private String url;

    @Value("${bitfinexApiKey}")
    private String apiKey="3YtPxt3pkFJ1b6wDNoysYzugUDbXC8EMksUO3FotQwc";

    @Value("${bitfinexApiSecret}")
    private String apiSecret="hJYtMGOJKuhshnFROzt6o4AlnZugL189N06tn551aw8";

    private WebSocket webSocket;

//    {"apiKey":"3YtPxt3pkFJ1b6wDNoysYzugUDbXC8EMksUO3FotQwc","event":"auth","authPayload":"AUTH1507861663542000","authSig":"6f2adf8b326b269b8924174cc6d00f049a960086095cfde69bf065a76193a84979cd002266110207b5ee024b9cf6d449"}

    @Override
    public void start(){

        HttpClientOptions options = new HttpClientOptions().
                setDefaultPort(443).
                setProxyOptions(new ProxyOptions().setType(ProxyType.SOCKS5).setHost("127.0.0.1") .setPort(1080)).
                setLogActivity(true).
                setTrustAll(true).
                setSsl(true).
                setDefaultHost("api.bitfinex.com");
        HttpClient httpClient = vertx.createHttpClient(options);

        httpClient.websocket("/ws/2", webSocket -> {

            LOGGER.info("bitfinex websocket has Connected!");
            webSocket.frameHandler(event -> {
                System.out.println("受到数据"+event.textData());
            });
            webSocket.exceptionHandler(event -> LOGGER.error(event.getMessage()));
            webSocket.handler(buffer -> {

                LOGGER.info("连接正常" + new String(buffer.getBytes()));

            });

            webSocket.endHandler(event -> {
                LOGGER.info("End webSocket connection!");
            });

            this.webSocket = webSocket;
        });
    }


    public void initChannel(){

    }

    /**
     * 生成认证参数
     * @return
     */
    private String  getinitParam(){
        long authNonce  = new Date().getTime()*1000;
        String authPayload = "AUTH" + authNonce;

        String authSig = crypto(authPayload);
        Map<String, Object> para = new HashMap<>();
        para.put("apiKey", apiKey);
        para.put("event", "auth");
        para.put("authPayload", authPayload);
        para.put("authSig", authSig);

        JSONObject jsonObject = new JSONObject(para);
        return jsonObject.toJSONString();
    }

    /**
     * HMAC-sha384 signature
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private String crypto(String authPayload) {

        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA384");
        Mac mac = null;
        try {
            // 实例化Mac
            mac = Mac.getInstance(secretKey.getAlgorithm());
            //初始化mac
            mac.init(secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        //执行消息摘要
        assert mac != null;
        byte[] digest = mac.doFinal(authPayload.getBytes());

        return new HexBinaryAdapter().marshal(digest);
    }
}
