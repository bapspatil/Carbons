package com.bapspatil.carbons.model;

import com.google.gson.annotations.SerializedName;

public class FlickrResponse {

    @SerializedName("stat")
    private String stat;

    @SerializedName("photos")
    private Photos photos;

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStat() {
        return stat;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public Photos getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return
                "FlickrResponse{" +
                        "stat = '" + stat + '\'' +
                        ",photos = '" + photos + '\'' +
                        "}";
    }
}