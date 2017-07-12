package com.go2going.control;

import com.go2going.common.CodeProp;
import com.go2going.common.Go2goingException;
import com.go2going.dao.TradeRecordDao;
import com.go2going.model.bo.UserBo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
  private UserBo loginUser;

  /**
   * 登录页
   * @return
   */
  @GetMapping("/")
  public String index(HttpSession httpSession){
    if (httpSession.getAttribute(LOGIN_SIGN) == null || !httpSession.getAttribute(LOGIN_SIGN).equals(loginUser)) {//未登录
      return "login";
    } else {
      return "index";
    }
  }

  /**
   * 登录
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
        httpSession.setAttribute(LOGIN_SIGN,userBo);
      }
    }
    return "index";
  }

  @GetMapping("/test")
  public String test(ModelMap map){
    return "test";
  }


}
