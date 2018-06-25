package com.thesamiroli.chitchat;

//This is a Model Class for the data that are to be taken from Firebase Database and show them to Recycler View.
public class UsersModel {

    public String name, email, gender, image, thumb_image;

    public UsersModel(String name, String email, String gender, String image, String thumb_image) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.image = image;
        this.thumb_image = thumb_image;
    }

    public UsersModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
