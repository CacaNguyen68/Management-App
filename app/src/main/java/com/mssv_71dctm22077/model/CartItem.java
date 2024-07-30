package com.mssv_71dctm22077.model;

public class CartItem {
  private int cartItemId;
  private String productName;
  private byte[] productImage; // Thay đổi từ String thành byte[]
  private double productPrice;
  private int quantity;

  public CartItem(int cartItemId, String productName, byte[] productImage, double productPrice, int quantity) {
    this.cartItemId = cartItemId;
    this.productName = productName;
    this.productImage = productImage;
    this.productPrice = productPrice;
    this.quantity = quantity;
  }

  public int getCartItemId() {
    return cartItemId;
  }

  public String getProductName() {
    return productName;
  }

  public byte[] getProductImage() {
    return productImage;
  }

  public double getProductPrice() {
    return productPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
