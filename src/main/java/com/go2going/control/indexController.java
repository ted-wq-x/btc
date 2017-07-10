package com.go2going.control;

import com.go2going.dao.TradeRecordDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * Created by wangq on 2017/7/10.
 */
@Controller
public class indexController {

  @Resource
  private TradeRecordDao tradeRecordDao;

  @GetMapping("/")
  public String index(ModelMap map){
    map.addAttribute("host", "http://blog.didispace.com");
    return "index";
  }


}
