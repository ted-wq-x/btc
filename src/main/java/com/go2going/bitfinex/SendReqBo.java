package com.go2going.bitfinex;

import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotNull;

/**
 * 具体参数说明参考 https://docs.bitfinex.com/v1/reference
 * @author BlueT
 * 2017/10/13 23:30
 */
public class SendReqBo {


    enum Pairs {
        BTCUSD, LTCUSD, LTCBTC, ETHUSD, ETHBTC, ETCUSD, ETCBTC, BFXUSD, BFXBTC, RRTUSD, RRTBTC, ZECUSD, ZECBTC;
    }

    enum Channel {

        BOOK("book"),

        TRADES("trades"),

        //提供访问图表蜡烛信息的方法
        CANDLES("candles"),

        //股票行情是市场状况的高级概览。它显示您当前的最佳出价和要求以及最后的交易价格。它还包括每天的数量和价格在最后一天涨了多少的信息。
        TICKER("ticker");

        Channel(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }
    }

    /**
     * ping
     *
     * @return
     */
    public static String getPingCmd() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "ping");
        return jsonObject.toJSONString();
    }

    public static String getOrderBook(@NotNull Pairs pairs) {
        JSONObject jsonObject = getChannelCommon(Channel.BOOK);

        jsonObject.put("symbol", "t"+pairs.name());
        jsonObject.put("prec", "P0");
        jsonObject.put("freq", "F0");
        jsonObject.put("len", "25");

        return jsonObject.toJSONString();
    }

    public static String getTrades(@NotNull Pairs pairs) {
        JSONObject jsonObject = getChannelCommon(Channel.TRADES);
        jsonObject.put("symbol", "t"+pairs.name());
        return jsonObject.toJSONString();
    }



    private static JSONObject getChannelCommon(@NotNull Channel channel) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "subscribe");
        jsonObject.put("channel", channel.getName());
        return jsonObject;
    }


}
