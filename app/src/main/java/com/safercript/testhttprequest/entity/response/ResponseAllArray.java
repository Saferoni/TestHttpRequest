package com.safercript.testhttprequest.entity.response;

import java.util.Arrays;

/*
"images": [
        {
        "id": 945,
        "description": "test",
        "hashtag": "#testimage",
        "parameters": {
        "longitude": 23.175977,
        "latitude": 53.122042,
        "address": "Adama Mickiewicza 43, Białystok, Poland",
        "weather": "Rain"
        },
        "smallImagePath": "http://api.doitserver.in.ua/upload/images/small/ef14d74c687b6000074179c7d8227e80.jpeg",
        "bigImagePath": "http://api.doitserver.in.ua/upload/images/big/e395584d54341fbc97dfae520e91da1e.jpeg",
        "created": "05-10-2017 03:48:07"
        }
        ],
        "gif": [
        {
        "id": 1785,
        "weather": "rain",
        "path": "http://api.doitserver.in.ua/upload/images/gif/6b593ee12c464b2dc1ba22f3ef14a991.gif",
        "created": "05-10-2017 03:49:14"
        }
        ]
*/

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
