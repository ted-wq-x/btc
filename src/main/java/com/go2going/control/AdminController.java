package com.go2going.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.go2going.common.CodeProp;
import com.go2going.common.Go2goingException;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.RestResponseBo;
import com.go2going.model.bo.UserBo;
import com.go2going.model.vo.TradeRecordVo;
import com.go2going.utils.TimingTask;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangq on 2017/7/10.
 */
@Controller
public class AdminController {

  private static final String LOGIN_SIGN = "go2going_login";
  private static final String REMEMBER = "remember-me";

  @Resource
  private TradeRecordDao tradeRecordDao;

  @Resource
  private SimpMessagingTemplate simpMessagingTemplate;

  @Resource
  private UserBo loginUser;

  /**
   * 登录页
   *
   * @return
   */
  @GetMapping("/")
  public String index(HttpSession httpSession) {
    if (httpSession.getAttribute(LOGIN_SIGN) == null || !httpSession.getAttribute(LOGIN_SIGN).equals(loginUser)) {//未登录
      return "login";
    } else {
      return "index";
    }
  }

  /**
   * 登录
   *
   * @param userBo
   * @param httpSession
   * @return
   */
  @PostMapping("/login")
  public String index(UserBo userBo, HttpSession httpSession, @RequestParam(required = false) String remember) {
    if (!loginUser.equals(userBo)) {//已登录
      throw new Go2goingException(CodeProp.LOGIN_ILLEGAL);
    } else {
      if (REMEMBER.equals(remember)) {
        httpSession.setAttribute(LOGIN_SIGN, userBo);
      }
    }
    return "index";
  }

  @GetMapping("/logout")
  public String logOut(ModelMap map, HttpSession httpSession) {
    httpSession.removeAttribute(LOGIN_SIGN);
    return "login";
  }

  @GetMapping("/test")
  public String test(ModelMap map) {
    return "test";
  }

  @MessageMapping("/change-notice")
  @SendTo("/topic/notice")
  public String  search(String value) throws ParseException {
    JSONObject jsonObject = JSON.parseObject(value);
    String coinType = jsonObject.getString("coinType");
    String startTime = jsonObject.getString("startTime");
    String endTime = jsonObject.getString("endTime");
    String tradeType = jsonObject.getString("tradeType");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd HH:mm");
      Date start = dateFormat.parse(startTime);
      Date end = dateFormat.parse(endTime);
      List<TradeRecordVo> list;
      if (tradeType.equals("all")) {
        list= tradeRecordDao.findAllByTradeTimeBetweenAndGoodsCategory(start, end, coinType);
      } else {
         list = tradeRecordDao.findAllByTradeTimeBetweenAndGoodsCategoryAndTradeType(start, end, coinType,tradeType);
      }

      if (list != null) {
        Map<String, Float> stringFloatMap = TimingTask.calNum(list);
        return JSON.toJSONString(stringFloatMap);
      }
    return null;
  }
}
