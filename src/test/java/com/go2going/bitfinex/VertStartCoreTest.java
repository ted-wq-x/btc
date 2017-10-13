package com.go2going.bitfinex;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * 项目名称：  btc<br>
 * 类名称：  VertStartCoreTest<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2017/10/13 0013 11:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VertStartCoreTest {

    @Resource
    private VertStartCore core;

    @Test
    public void init(){
        core.start();
    }
}