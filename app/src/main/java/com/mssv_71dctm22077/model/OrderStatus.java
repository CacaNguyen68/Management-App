package com.mssv_71dctm22077.model;

public enum OrderStatus {
  PLACED("Đơn hàng đã đặt", "#000000"), // Gold
  CONFIRMED("Đã xác nhận đơn hàng", "#FF6F00"), // Orange
  SHIPPED("Đơn hàng đang vận chuyển", "#4CAF50"), // Green
  DELIVERED("Giao thành công", "#2196F3"), // Blue
  CANCELED("Huỷ đơn hàng", "#F44336"); // Red

  private final String displayName;
  private final String color;

  OrderStatus(String displayName, String color) {
    this.displayName = displayName;
    this.color = color;
  }

  public String getDisplayName() {
    return displayName;
  }

  public int getColor() {
    return android.graphics.Color.parseColor(color);
  }
}
