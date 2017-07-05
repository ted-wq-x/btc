package com.go2going.interfaceApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.DateUtil;
import com.go2going.utils.SpringContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangq on 2017/7/5.
 */
@ApiAnnotation(api="ok_sub_spotcny_ltc_trades")
public class TradeLTCRecord extends AbstractSubject {

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
      recordVos.add(recordVo);
    });

    if (recordVos.size() != 0) {
      TradeRecordDao recordDao = SpringContext.getBean(TradeRecordDao.class);
      recordDao.save(recordVos);
    }
  }
}
