package com.safercript.testhttprequest.entity.response;

/*{
        "children": {
        "image": {
        "errors": [
        "The image width is too small (96px). Minimum width expected is 600px."
        ]
        },
        "description": {},
        "hashtag": {},
        "latitude": {},
        "longitude": {}
        }
        }*/

import java.util.Arrays;

public class ResponseError {
    private Children children;



    private class Children{
        private Image image;
        private Description description;
        private HashTag hashtag;
        private Latitude latitude;
        private Longitude longitude;


        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public HashTag getHashtag() {
            return hashtag;
        }

        public void setHashtag(HashTag hashtag) {
            this.hashtag = hashtag;
        }

        public Latitude getLatitude() {
            return latitude;
        }

        public void setLatitude(Latitude latitude) {
            this.latitude = latitude;
        }

        public Longitude getLongitude() {
            return longitude;
        }

        public void setLongitude(Longitude longitude) {
            this.longitude = longitude;
        }
    }

    private class Image{
        private String [] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }

    private class Description{
        private String [] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }
    private class HashTag{
        private String [] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }

    private class Latitude{
        private String [] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }

    private class Longitude{
        private String [] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }

    public Children getChildren() {
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public String getAllErrors(){

        return Arrays.toString(children.image.errors) +
                Arrays.toString(children.latitude.errors) +
                Arrays.toString(children.longitude.errors);
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "children=" + children + Arrays.toString(children.image.errors) +
                ", description='" + children.description + '\'' +
                ", hashtag='" + children.hashtag + '\'' +
                ", latitude='" + children.latitude + '\'' + Arrays.toString(children.latitude.errors) +
                ", longitude='" + children.longitude + '\'' + Arrays.toString(children.longitude.errors) +
                '}';
    }
}

