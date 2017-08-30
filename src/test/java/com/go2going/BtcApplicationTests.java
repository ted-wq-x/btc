package com.go2going;

import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.GoodsCategory;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.EmailUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BtcApplicationTests {
	@Autowired
	private EmailUtils emailUtils;

	@Resource
	private TradeRecordDao tradeRecordDao;
	@Test
	@Ignore
	public void contextLoads() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017,Calendar.JULY,6 ,14,28,0);
		Date start = calendar.getTime();
		calendar.set(2017,Calendar.JULY,6 ,14,29,0);
		Date end = calendar.getTime();
		List<TradeRecordVo> ask = tradeRecordDao.findAllByTradeTimeBetweenAndGoodsCategoryAndTradeType(start, end, GoodsCategory.LTC, "");

		System.out.println(ask.size());
	}

}
