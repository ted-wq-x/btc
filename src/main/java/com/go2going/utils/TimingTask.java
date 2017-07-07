package com.go2going.utils;

import com.go2going.config.ApiPropModel;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import com.sun.javafx.binding.StringFormatter;
import javafx.beans.binding.StringExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.awt.geom.AreaOp;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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

  @Resource
  private EmailUtils emailUtils;

  private Map<String, Double> remind;

  @PostConstruct
  public void init() {
    remind = apiPropModel.getRemind();
  }

  /**
   * 每分钟执行一次
   */
  @Scheduled(cron = "0 * * * * ?")
  public void countTradingNum() {
    LOGGER.info("Enter countTradingNum method");
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));//解决服务器时间问题
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.SECOND, 0);
    Date now = calendar.getTime();
    calendar.add(Calendar.MINUTE, -1);
    Date before = calendar.getTime();
    TradeRecordDao tradeRecordDao = SpringContext.getBean(TradeRecordDao.class);
    List<TradeRecordVo> list = tradeRecordDao.findAllByTradeTimeBetween(before, now);

//    获取不同币种的交易数据
    Map<String, List<TradeRecordVo>> category = list.stream().
            collect(Collectors.groupingBy(TradeRecordVo::getGoodsCategory, Collectors.toList()));

    category.forEach((s, tradeRecordVos) -> {
      Double aDouble = remind.get(s);
      if (aDouble == null) {
        LOGGER.error("remind value is null:type={}", s);
        return;
      }
      //统计不同币种的交易量
      Map<String, Float> stringFloatMap = calNum(tradeRecordVos);
      Float sum = stringFloatMap.get("sum");
      LOGGER.info("count sum={}",sum);
      if ( sum>= aDouble) {
        String msg = combingMsg(stringFloatMap, s);
        emailUtils.sendEmail(msg);
      }
    });
    LOGGER.info("Exit countTradingNum method");
  }

  /**
   * 整合邮件内容
   *
   * @param map
   * @param s
   * @return
   */
  private String combingMsg(Map<String, Float> map, String s) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, -1);
    calendar.set(Calendar.SECOND, 0);
    Date time = calendar.getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String str = "币种:%s,时间:%s,成交量:%f,买入深度:%f,卖出深度:%f";
    StringExpression format = StringFormatter.format(str, s,simpleDateFormat.format(time),map.get("sum"),map.get("bid"),map.get("ask"));
    return format.get();
  }

  /**
   * 统计交易数据
   *
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
