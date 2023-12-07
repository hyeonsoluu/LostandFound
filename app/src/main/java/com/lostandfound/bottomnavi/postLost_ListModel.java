package com.lostandfound.bottomnavi;

public class postLost_ListModel {
    //firstly define instance variables

    String userID;
    String category;
    String title;
    String Ddate;
    String firstLocation;
    String detailLocation;
    String detailContent;

    private String imageUrl;

    //then create an constructor that will be called by MainActivity.java

    public postLost_ListModel(String imageUrl, String userID, String category, String title, String Ddate, String firstLocation, String detailLocation, String detailContent) {
        this.userID = userID;
        this.category = category;
        this.title = title;
        this.Ddate = Ddate;
        this.firstLocation = firstLocation;
        this.detailLocation = detailLocation;
        this.detailContent = detailContent;
        this.imageUrl = imageUrl;
    }

    //then create getter and setter methods


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDdate() {
        return Ddate;
    }

    public void setDdate(String Ddate) {
        this.Ddate = Ddate;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getDetailLocation() {
        return detailLocation;
    }

    public void setDetailLocation(String detailLocation) {
        this.detailLocation = detailLocation;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
}

