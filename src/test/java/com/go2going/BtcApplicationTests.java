package com.go2going;

import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BtcApplicationTests {

	@Test
	public void contextLoads() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND,0);
		Date now = calendar.getTime();
		calendar.add(Calendar.MINUTE,-1);
		Date before = calendar.getTime();
		TradeRecordDao tradeRecordDao = SpringContext.getBean(TradeRecordDao.class);
		List<TradeRecordVo> list = tradeRecordDao.findAllByTradeTimeBetweenAndAndGoodsCategory(before, now, GoodsCategory.BTC);
		System.out.println(list);
	}

}
