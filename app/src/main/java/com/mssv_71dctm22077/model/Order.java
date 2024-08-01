package com.mssv_71dctm22077.model;

public class Order {
  private int orderId;
  private int userId;
  private String createdAt;

  public Order(int orderId, int userId, String createdAt) {
    this.orderId = orderId;
    this.userId = userId;
    this.createdAt = createdAt;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }
}
