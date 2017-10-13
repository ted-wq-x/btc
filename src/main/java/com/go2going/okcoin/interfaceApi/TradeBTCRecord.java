package com.go2going.okcoin.interfaceApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.DateUtil;
import com.go2going.utils.SpringContext;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅BTC成交记录
 * Created by BlueT on 2017/7/3.
 */
@ApiAnnotation(api="ok_sub_spotcny_btc_trades")
public class TradeBTCRecord extends AbstractSubject{
    private static final Logger LOGGER = Logger.getLogger(TradeBTCRecord.class);

    @Override
    public void handler(String json) {
        JSONArray objects = JSON.parseArray(json);
        List<TradeRecordVo> recordVos = new ArrayList<>();
        objects.forEach(o -> {
            JSONArray jsonObject = (JSONArray) o;
            TradeRecordVo recordVo = new TradeRecordVo();
            recordVo.setId(jsonObject.getLongValue(0));
            recordVo.setPrice(jsonObject.getFloatValue(1));
            recordVo.setTradeNum(jsonObject.getFloatValue(2));
            recordVo.setTradeTime(DateUtil.generateDate(jsonObject.getString(3)));
            recordVo.setTradeType(jsonObject.getString(4));
            recordVo.setGoodsCategory(GoodsCategory.BTC);
            recordVos.add(recordVo);
        });

        if (recordVos.size() != 0) {
            TradeRecordDao recordDao = SpringContext.getBean(TradeRecordDao.class);
            recordDao.save(recordVos);
        }
    }

}
