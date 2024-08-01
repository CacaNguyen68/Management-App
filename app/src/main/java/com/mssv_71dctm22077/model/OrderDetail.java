package com.mssv_71dctm22077.model;

public class OrderDetail {
  private String name;
  private double price;
  private byte[] image;
  private int quantity;  // Ensure this field exists and has a setter

  // Getters and Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public double getPrice() { return price; }
  public void setPrice(double price) { this.price = price; }

  public byte[] getImage() { return image; }
  public void setImage(byte[] image) { this.image = image; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
}
