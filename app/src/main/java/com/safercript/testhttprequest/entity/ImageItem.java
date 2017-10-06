package com.safercript.testhttprequest.entity;

public class ImageItem {
    private int image;
    private String address;
    private String weather;

    public ImageItem(int image, String address, String weather) {
        this.image = image;
        this.address = address;
        this.weather = weather;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeather(){
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
