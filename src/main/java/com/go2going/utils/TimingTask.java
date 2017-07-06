package com.go2going.utils;

import com.go2going.config.ApiPropModel;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务统计数据
 * Created by wangq on 2017/7/5.
 */
@Component
public class TimingTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimingTask.class);

  @Resource
  private ApiPropModel apiPropModel;

  private Map<String, Double> remind;

  @PostConstruct
  public void init(){
    remind = apiPropModel.getRemind();
  }

  /**
   * 每分钟执行一次
   */
  @Scheduled(cron = "0 * * * * ?")
  public void countTradingNum(){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.SECOND,0);
    Date now = calendar.getTime();
    calendar.add(Calendar.MINUTE,-1);
    Date before = calendar.getTime();
    TradeRecordDao tradeRecordDao = SpringContext.getBean(TradeRecordDao.class);
    List<TradeRecordVo> list = tradeRecordDao.findAllByTradeTimeBetweenAndAndGoodsCategory(before, now, GoodsCategory.BTC);

//    获取不同币种的交易数据
    Map<String, List<TradeRecordVo>> category = list.stream().
            collect(Collectors.groupingBy(TradeRecordVo::getGoodsCategory, Collectors.toList()));

    category.forEach((s, tradeRecordVos) -> {
      Double aDouble = remind.get(s);
      if (aDouble == null) {
        LOGGER.error("remind value is null:type={}",s);
        return;
      }
      //统计不同币种的交易量
      Map<String, Float> stringFloatMap = calNum(tradeRecordVos);

      if (stringFloatMap.get("sum") >= aDouble) {
        String msg = combingMsg(stringFloatMap, s);
        EmailUtils.sendEmail(msg);
      }
    });



  }

  /**
   * 整合邮件内容
   * @param stringFloatMap
   * @param s
   * @return
   */
  private String combingMsg(Map<String, Float> stringFloatMap, String s) {
    return null;
  }

  /**
   * 统计交易数据
   * @param recordVoList map->key为bid,ask,sum
   */
  private Map<String, Float> calNum(List<TradeRecordVo> recordVoList) {
    Map<String, List<Float>> collect = recordVoList.stream().collect(Collectors.groupingBy(TradeRecordVo::getTradeType, Collectors.mapping(TradeRecordVo::getTradeNum, Collectors.toList())));
    Map<String, Float> returnMap = new HashMap<>();
    returnMap.put("sum", 0.0f);
    collect.forEach((s, floats) -> {
      Optional<Float> reduce = floats.stream().reduce((aFloat, aFloat2) -> aFloat += aFloat2);
      if (reduce.isPresent()) {
        Float aFloat = reduce.get();
        returnMap.put(s, aFloat);
        returnMap.put("sum", returnMap.get("sum") + aFloat);
      }
    });

    return returnMap;
  }


}
