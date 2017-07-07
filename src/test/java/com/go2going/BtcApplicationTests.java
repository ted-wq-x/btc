package com.go2going;

import com.go2going.utils.EmailUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BtcApplicationTests {
	@Autowired
	private EmailUtils emailUtils;
	@Test
	@Ignore
	public void contextLoads() {

		emailUtils.sendEmail("妹子我喜欢你");
	}

}
