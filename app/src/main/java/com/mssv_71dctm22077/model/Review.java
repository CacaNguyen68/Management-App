package com.mssv_71dctm22077.model;

public class Review {
  private int reviewId;
  private int orderId;
  private int productId;
  private float rating;
  private String reviewText;
  private String createdAt;

  public Review(int reviewId, int orderId, int productId, float rating, String reviewText, String createdAt) {
    this.reviewId = reviewId;
    this.orderId = orderId;
    this.productId = productId;
    this.rating = rating;
    this.reviewText = reviewText;
    this.createdAt = createdAt;
  }

  public int getReviewId() {
    return reviewId;
  }


  public int getOrderId() {
    return orderId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public float getRating() {
    return rating;
  }

  public String getReviewText() {
    return reviewText;
  }

  public String getCreatedAt() {
    return createdAt;
  }
}
