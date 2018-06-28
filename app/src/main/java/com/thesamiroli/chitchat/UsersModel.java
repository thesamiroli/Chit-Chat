package com.thesamiroli.chitchat;

//This is a Model Class for the data that are to be taken from Firebase Database and show them to Recycler View.
public class UsersModel {

    public String dname;
    public String pname;
    public String phone;
    public String bio;
    public String email;
    public String gender;
    public String image;

    public UsersModel() {
    }

    public String thumb_image;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UsersModel(String dname, String pname, String phone, String bio, String email, String gender, String image, String thumb_image) {
        this.dname = dname;
        this.pname = pname;
        this.phone = phone;
        this.bio = bio;
        this.email = email;
        this.gender = gender;
        this.image = image;
        this.thumb_image = thumb_image;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}