package com.safercript.testhttprequest.entity;


public class ImageToSend {
    private String imagePath;
    private String description;
    private String hashTag;
    private String latitude;
    private String longitude;

    public ImageToSend(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ImageToSend(String imagePath, String description, String hashTag) {
        this.imagePath = imagePath;
        this.description = description;
        this.hashTag = hashTag;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public String toString() {
        return "ImageToSend{" +
                "imagePath=" + imagePath +
                ", description=" + description + '\'' +
                ", hashTag=" + hashTag + '\'' +
                ", latitude=" + latitude + '\'' +
                ", longitude=" + longitude + '\'' +
                '}';
    }
}
