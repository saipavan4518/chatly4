package com.codeblac.chatly;

public class UserInfo {
    private String Full_Name;
    private String Bio;
    private String Uid;
    private String imageUrl;
    private String status;





    public UserInfo(String full_Name, String bio, String uid, String imageUrl,String status) {
        Full_Name = full_Name;
        Bio = bio;
        Uid = uid;
        this.imageUrl = imageUrl;
        this.status=status;
    }

    public UserInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
