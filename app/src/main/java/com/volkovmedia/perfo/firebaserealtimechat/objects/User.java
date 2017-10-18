package com.volkovmedia.perfo.firebaserealtimechat.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class User implements DataContainer {

    @Exclude
    private String uIdentifier;

    @PropertyName("name")
    private String uName;

    @PropertyName("picture")
    private String uPicture;

    public User() { } //for firebase only

    public User(String name, String picture) {
        this.uName = name;
        this.uPicture = picture;
    }

    public String getId() {
        return uIdentifier;
    }

    public String getName() {
        return uName;
    }

    public String getPicture() {
        return uPicture;
    }

    public void setId(String id) {
        this.uIdentifier = id;
    }

    public void setName(String name) {
        this.uName = name;
    }

    public void setPicture(String picture) {
        this.uPicture = picture;
    }

}
