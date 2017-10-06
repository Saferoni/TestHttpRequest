package com.safercript.testhttprequest.entity;

public class User {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String authorizationKey;
    private String avatar;
    private boolean isSetInfo;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSetInfo() {
        return isSetInfo;
    }

    public void setSetInfo(boolean setInfo) {
        isSetInfo = setInfo;
    }
}
