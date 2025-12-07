package com.hzcu.order.model;

public class UserProfile {

  private long id;
  private String nickname;
  private String avatar;
  private String phone;

  public UserProfile() {
  }

  public UserProfile(long id, String nickname, String avatar, String phone) {
    this.id = id;
    this.nickname = nickname;
    this.avatar = avatar;
    this.phone = phone;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
