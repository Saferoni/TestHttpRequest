package com.safercript.testhttprequest;


import com.safercript.testhttprequest.entity.User;

public class SharedInfo {
    private static final String LOG_TAG = SharedInfo.class.getSimpleName();

    private volatile User currentUser;
    private String userToken;

    private static SharedInfo instance;

    private SharedInfo(){

    }

    public static SharedInfo get(){
        if (instance == null) {
            instance = new SharedInfo();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
