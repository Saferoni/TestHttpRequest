package com.safercript.testhttprequest.entity.response;

import java.util.Arrays;

public class ResponseAllArray {

    private ResponseImage [] images;
    private ResponseGif[] gif;

    public ResponseImage[] getImages() {
        return images;
    }

    public void setImages(ResponseImage[] images) {
        this.images = images;
    }

    public ResponseGif[] getGif() {
        return gif;
    }

    public void setGif(ResponseGif[] gif) {
        this.gif = gif;
    }

    @Override
    public String toString() {
        return "ResponseAllArray " +
                "images=" + Arrays.toString(images) +
                "gif=" + Arrays.toString(gif) +
                '}';
    }
}
