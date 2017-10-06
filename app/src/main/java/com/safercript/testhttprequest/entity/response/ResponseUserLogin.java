package com.safercript.testhttprequest.entity.response;


public class ResponseUserLogin {

    private String creation_time;
    //2017-09-28 23:53:25
    private String token;
    //5cd62f75221bf4335dc0fa751a7cf716
    private String avatar;
    //http://api.doitserver.in.ua/upload/avatars/e09302c4e1ee9f285b11433aac76ac70.png


    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
