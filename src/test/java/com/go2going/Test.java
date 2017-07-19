package com.go2going;


import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * fuckyou
 * Created by BlueT on 2017/7/4.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017,Calendar.JULY,6 ,14,28,0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(calendar.getTime()));
    }
}
