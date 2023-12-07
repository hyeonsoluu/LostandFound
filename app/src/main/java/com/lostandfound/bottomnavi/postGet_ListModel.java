package com.lostandfound.bottomnavi;

public class postGet_ListModel {
    private String userID;

    private String title;
    private String category;
    private String Ddate;
    private String firstLocation;
    private String detailLocation;
    private String storage;
    private String detailContent;
    private String imageUrl;

    public postGet_ListModel(String userID, String imageUrl, String category, String title, String Ddate, String firstLocation, String detailLocation, String storage, String detailContent) {

        this.userID = userID;
        this.imageUrl = imageUrl;
        this.category = category;
        this.title = title;
        this.Ddate = Ddate;
        this.firstLocation = firstLocation;
        this.detailLocation = detailLocation;
        this.storage = storage;
        this.detailContent = detailContent;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageUrl() { return imageUrl; }

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

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
}
