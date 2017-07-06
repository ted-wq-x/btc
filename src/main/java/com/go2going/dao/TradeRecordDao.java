package com.go2going.dao;

import com.go2going.model.vo.TradeRecordVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by wangq on 2017/7/5.
 */
public interface TradeRecordDao extends JpaRepository<TradeRecordVo, Long> {

  List<TradeRecordVo> findAllByTradeTimeBetween(Date startTime, Date endTime);
}
