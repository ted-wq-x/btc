package com.go2going.bitfinex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.go2going.common.ExceptionProp;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.SpringContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

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
    private static final String host = "api.bitfinex.com";
    private static final String requestUrl = "/ws/2";

    @Value("${bitfinexApiKey}")
    private String apiKey;

    @Value("${bitfinexApiSecret}")
    private String apiSecret;

    @Resource
    private ExceptionProp exceptionProp;

    private WebSocket webSocket;

    private WorkerExecutor myCore;

    private TradeRecordDao recordDao;



    @Override
    public void stop() throws Exception {
        if (webSocket != null) {
            webSocket.close();
        }
    }

    @Override
    public void start() {
        recordDao = SpringContext.getBean(TradeRecordDao.class);
        //TODO 检查启动参数
        HttpClientOptions options = new HttpClientOptions().
                setDefaultPort(443).
                setProxyOptions(new ProxyOptions().setType(ProxyType.SOCKS5).setHost("127.0.0.1").setPort(1080)).
                setTrustAll(true).
                setSsl(true).
                setDefaultHost(host);

        myCore = vertx.createSharedWorkerExecutor("MyCore");

        HttpClient httpClient = vertx.createHttpClient(options);

        createWebSocket(httpClient);


    }

    private void createWebSocket(HttpClient httpClient) {
        httpClient.websocket(requestUrl, webSocket -> {

            LOGGER.info("bitfinex websocket has Connected!");
            webSocket.frameHandler(frame -> this.handler(frame.textData()));

            webSocket.handler(event -> LOGGER.info(new String(event.getBytes())));

            webSocket.exceptionHandler(event -> LOGGER.error(event.getMessage()));

            webSocket.endHandler(event ->{
                LOGGER.info("End webSocket connection!");
                //
                createWebSocket(httpClient);
            });

            this.webSocket = webSocket;
            //初始化订阅
            initChannel();
        });
    }


    public void initChannel() {
        //测试ping
//        webSocket.writeTextMessage(SendReqBo.getPingCmd());
        webSocket.writeTextMessage(SendReqBo.getTrades(SendReqBo.Pairs.BTCUSD));
    }

    private BlockingDeque<TradeRecordVo> queue = new LinkedBlockingDeque<>(30);

    /**
     * 数据结构不同得分开处理
     * @param s
     */
    private void handler(String s) {
        //检查数据异常
        JSONObject jsonObject = null;
        try {
            jsonObject =JSONObject.parseObject(s) ;
        } catch (JSONException e) {
        }

        if (jsonObject == null) {
            //日常数据

            myCore.executeBlocking(future -> {
                //处理消息
                List<TradeRecordVo> data = getData(s);
//                queue.addAll(data);
//                if (queue.size() >= 20) {
//                    TradeRecordDao recordDao = SpringContext.getBean(TradeRecordDao.class);
//                    List<TradeRecordVo> temp = new ArrayList<>();
//                    queue.drainTo(temp);
//                    recordDao.save(temp);
//                    LOGGER.info(Thread.currentThread()+"插入数据："+temp.size()+",队列剩余"+queue.size());
//                }
                if(!data.isEmpty()){
                    recordDao.save(data);
                }
                future.complete();
            }, false, event -> {
                //获取消息的处理结果
                if (event.failed()) {
                    LOGGER.error("处理消息失败" + event.cause());
                }
            });
        } else {
            // 处理事件
            String code = jsonObject.getString("code");

            if (code != null) {
                String codeMsg = exceptionProp.getCodeMsg(code);
                LOGGER.error(codeMsg);
                return;
            }
            String event = jsonObject.getString("event");
            switch (event) {
                default:
                    LOGGER.info("未处理的event:" + event);
                    break;
                case "subscribed":
                    String channel = jsonObject.getString("channel");
                    String channelId = jsonObject.getString("channelId");
                    String pair = jsonObject.getString("pair");
                    SendReqBo.Pairs pairs = SendReqBo.Pairs.valueOf(pair);
                    //TODO 就一个处理类型
                    if (channel.equals(SendReqBo.Channel.TRADES.getName())) {
                    }
                    break;
                case "info":
                    break;
            }
        }


    }

    /**
     * 生成认证参数
     *
     * @return
     */
    private String getinitParam() {
        long authNonce = new Date().getTime() * 1000;
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
     *
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


    /**
     * 数组index=0为channelId
     * @param string
     * @return
     */
    public List<TradeRecordVo> getData(String string) {
        List<TradeRecordVo> list = new ArrayList<>();
        JSONArray objects = JSON.parseArray(string);

        Integer channelId = objects.getInteger(0);

        int size = objects.size();

        if (size == 3) {
            //包含tu和td的待处理数据,目前两种情况的数据是一样的，所以只处理一种
            if(objects.getString(1).equals("tu")){
                JSONArray o1 = objects.getJSONArray(2);
                list.add(getTradeRecordVo(o1));
            }
        } else if (size==2) {
            Object o = objects.get(1);
            if (o == "tb") {
                //心跳
            } else if (o instanceof JSONArray) {
                //大量的待处理数据
                JSONArray os = (JSONArray) o;

                for (int i = 0; i < os.size(); i++) {
                    JSONArray o1 = os.getJSONArray(i);
                    TradeRecordVo tradeRecordVo = getTradeRecordVo(o1);
                    list.add(tradeRecordVo);
                }
            }
        }
        return list;
    }

    private TradeRecordVo getTradeRecordVo(JSONArray o1) {
        TradeRecordVo tradeRecordVo = new TradeRecordVo();
        Long id = o1.getLong(0);
        Long timeStamp = o1.getLong(1);
        Float tradeNum = o1.getFloat(2);
        Float tradePrice = o1.getFloat(3);
        tradeRecordVo.setId(id);
        tradeRecordVo.setTradeTime(new Date(timeStamp));
        tradeRecordVo.setTradeNum(tradeNum);

        if (tradeNum > 0) {
            tradeRecordVo.setTradeType("bid");
        } else {
            tradeRecordVo.setTradeType("ask");
        }

        tradeRecordVo.setGoodsCategory(GoodsCategory.BTC);
        tradeRecordVo.setPrice(tradePrice);
        return tradeRecordVo;
    }

}

