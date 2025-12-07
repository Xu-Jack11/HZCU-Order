package com.hzcu.order.model;

public class Comment {

  private long id;
  private String avatar;
  private String nickname;
  private String time;
  private int rating;
  private String content;

  public Comment() {
  }

  public Comment(long id, String avatar, String nickname, String time, int rating, String content) {
    this.id = id;
    this.avatar = avatar;
    this.nickname = nickname;
    this.time = time;
    this.rating = rating;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
