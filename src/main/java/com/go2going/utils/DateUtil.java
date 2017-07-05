package com.go2going.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangq on 2017/7/5.
 */
public class DateUtil {
  /**
   * @param time 时分秒
   * @return 返回date类型
   */
  public static Date generateDate(String time) {
    String[] split = time.split(":");
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.set(year,month,day,Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
    return calendar.getTime();
  }
}
