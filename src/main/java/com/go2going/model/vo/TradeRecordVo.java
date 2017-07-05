package com.go2going.model.vo;

import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 网站交易记录数据
 * Created by wangq on 2017/7/5.
 */
@Entity
//@Table(appliesTo = "trade_record",comment = "okcoin交易记录")
public class TradeRecordVo {

  @Id
//  @Column(nullable = true,unique = true,columnDefinition = "COMMENT '主键'")
  private Long id;

//  @Column(nullable = true,columnDefinition="COMMENT '成交价格'")
@Column
  private Float price;

//  @Column(nullable = true,columnDefinition="COMMENT '交易数量'")
@Column
  private Float tradeNum;

//  @Column(nullable = true,columnDefinition="COMMENT '成交类型（bids=买方深度 asks=卖方深度）'")
@Column
  private String tradeType;

//  @Column(nullable = true,columnDefinition="DATETIME COMMENT '成交时间'")
  @Column
  private Date tradeTime;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Float getTradeNum() {
    return tradeNum;
  }

  public void setTradeNum(Float tradeNum) {
    this.tradeNum = tradeNum;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public Date getTradeTime() {
    return tradeTime;
  }

  public void setTradeTime(Date tradeTime) {
    this.tradeTime = tradeTime;
  }

  @Override
  public String toString() {
    return "TradeRecordVo{" +
            "id=" + id +
            ", price=" + price +
            ", tradeNum=" + tradeNum +
            ", tradeType='" + tradeType + '\'' +
            ", tradeTime=" + tradeTime +
            '}';
  }
}
