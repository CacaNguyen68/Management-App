package com.mssv_71dctm22077.model;

import android.content.Intent;

import java.util.Arrays;

public class Product {
  private int id;
  private String name;
  private Double price;
  private Integer categoryId;
  private String createdAt;
  private String userCreatedAt;
  private byte[] image;

  public Product(int id, String name, Double price, int categoryId, String createdAt, String userCreatedAt, byte[] image) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.categoryId = categoryId;
    this.createdAt = createdAt;
    this.userCreatedAt = userCreatedAt;
    this.image = image;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUserCreatedAt() {
    return userCreatedAt;
  }

  public void setUserCreatedAt(String userCreatedAt) {
    this.userCreatedAt = userCreatedAt;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", price='" + price + '\'' +
      ", categoryId='" + categoryId + '\'' +
      ", createdAt='" + createdAt + '\'' +
      ", userCreatedAt='" + userCreatedAt + '\'' +
      ", image=" + Arrays.toString(image) +
      '}';
  }
}
