package com.mssv_71dctm22077.model;

public class Content {
  private int id;
  private byte[] image;
  private String content;
  private String title;
  private String createdAt;
  private int click;

  public Content() {
  }

  // Constructor, getters, and setters
  public Content(byte[] image, String content, String title, String createdAt, int click) {
    this.image = image;
    this.content = content;
    this.title = title;
    this.createdAt = createdAt;
    this.click = click;
  }

  public byte[] getImage() { return image; }
  public String getContent() { return content; }
  public String getTitle() { return title; }
  public String getCreatedAt() { return createdAt; }
  public int getClick() { return click; }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public void setClick(int click) {
    this.click = click;
  }
}
