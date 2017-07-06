package com.go2going.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by wangq on 2017/7/6.
 */
@Component
public class EmailUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

  @Autowired
  private JavaMailSender mailSender;

  public void sendEmail(String msg){
    LOGGER.info("Enter sendEmail msg");
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("blueted@outlook.com");
    message.setTo("736445126@qq.com");
    message.setSubject("主题：熊先生的日常");
    message.setText(msg);
    mailSender.send(message);
    LOGGER.info("Exit sendEmail msg");
  }
}
