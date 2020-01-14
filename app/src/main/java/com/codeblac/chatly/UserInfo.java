package com.codeblac.chatly;

public class UserInfo {
    private String Full_Name;
    private String Bio;
    private String Uid;
    private String imageUrl;

    public UserInfo(String full_Name, String bio, String uid, String imageUrl) {
        Full_Name = full_Name;
        Bio = bio;
        Uid = uid;
        this.imageUrl = imageUrl;
    }

    public UserInfo() {
    }

    public UserInfo(UserInfo uf){
        this.Bio=uf.Bio;
        this.Uid=uf.Bio;
        this.Full_Name=uf.Full_Name;
        this.imageUrl=uf.imageUrl ;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }




    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }
}
