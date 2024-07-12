package com.mssv_71dctm22077.model;

import java.util.Arrays;

public class User {
  private int id;
  private String name;
  private String dob;
  private String phone;
  private String email;
  private String userType;
  private String createdAt;
  private String userCreatedAt;
  private byte[] image;

  public User(int id, String name, String dob, String phone, String email, String userType, String createdAt, String userCreatedAt, byte[] image) {
    this.id = id;
    this.name = name;
    this.dob = dob;
    this.phone = phone;
    this.email = email;
    this.userType = userType;
    this.createdAt = createdAt;
    this.userCreatedAt = userCreatedAt;
    this.image = image;
  }

  public User() {

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

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
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
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", dob='" + dob + '\'' +
      ", phone='" + phone + '\'' +
      ", email='" + email + '\'' +
      ", userType='" + userType + '\'' +
      ", createdAt='" + createdAt + '\'' +
      ", userCreatedAt='" + userCreatedAt + '\'' +
      ", image=" + Arrays.toString(image) +
      '}';
  }
}
