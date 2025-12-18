package com.hzcu.order.model;

public class UserProfile {

  private long id;
  private String openid;
  private String unionid;
  private String nickname;
  private String avatar;
  private String phone;
  private String lastLoginAt;

  public UserProfile() {
  }

  public UserProfile(long id, String nickname, String avatar, String phone) {
    this.id = id;
    this.nickname = nickname;
    this.avatar = avatar;
    this.phone = phone;
  }

  public UserProfile(String openid, String nickname, String avatar) {
    this.openid = openid;
    this.nickname = nickname;
    this.avatar = avatar;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getUnionid() {
    return unionid;
  }

  public void setUnionid(String unionid) {
    this.unionid = unionid;
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

  public String getLastLoginAt() {
    return lastLoginAt;
  }

  public void setLastLoginAt(String lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }
}
