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
@Entity(name = "trade_record")
@Table(appliesTo = "trade_record", comment = "okcoin交易记录")
public class TradeRecordVo {

  @Id
  @Column(nullable = false, unique = true, columnDefinition = "bigint(20) COMMENT '主键'")
  private Long id;

  @Column(nullable = false, columnDefinition = "float COMMENT '成交价格'")
  private Float price;

  @Column(nullable = false, columnDefinition = "float COMMENT '交易数量'")
  private Float tradeNum;

  @Column(nullable = false, columnDefinition = "varchar(32) COMMENT '成交类型（bids=买方深度 asks=卖方深度）'")
  private String tradeType;

  @Column(nullable = false, columnDefinition = "DATETIME COMMENT '成交时间'")
  private Date tradeTime;

  @Column(nullable = false, columnDefinition = "varchar(32) COMMENT '币种'")
  private String goodsCategory;


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

  public String getGoodsCategory() {
    return goodsCategory;
  }

  public void setGoodsCategory(String goodsCategory) {
    this.goodsCategory = goodsCategory;
  }

  @Override
  public String toString() {
    return "TradeRecordVo{" +
            "id=" + id +
            ", price=" + price +
            ", tradeNum=" + tradeNum +
            ", tradeType='" + tradeType + '\'' +
            ", tradeTime=" + tradeTime +
            ", goodsCategory='" + goodsCategory + '\'' +
            '}';
  }
}
