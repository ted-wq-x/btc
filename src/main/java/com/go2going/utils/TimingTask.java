package com.go2going.utils;

import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务统计数据
 * Created by wangq on 2017/7/5.
 */
@Component
public class TimingTask {

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
  }
}
