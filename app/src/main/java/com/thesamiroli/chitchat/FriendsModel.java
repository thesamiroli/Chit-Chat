package com.thesamiroli.chitchat;

public class FriendsModel {

    public String date;
    public String key;
    public String pname;
    public String thumb_image;


    public FriendsModel() {
    }

    public FriendsModel(String date) {
        this.date =date;
    }
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getDname() {
        return date;
    }

    public void setDname(String dname) {
        this.date = dname;
    }
}
