package com.bapspatil.carbons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PhotoItem implements Parcelable{

    @SerializedName("owner")
    private String owner;

    @SerializedName("server")
    private String server;

    @SerializedName("ispublic")
    private int ispublic;

    @SerializedName("isfriend")
    private int isfriend;

    @SerializedName("farm")
    private int farm;

    @SerializedName("id")
    private String id;

    @SerializedName("secret")
    private String secret;

    @SerializedName("url_m")
    private String urlM;

    @SerializedName("title")
    private String title;

    @SerializedName("height_m")
    private String heightM;

    @SerializedName("width_m")
    private String widthM;

    @SerializedName("isfamily")
    private int isfamily;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public int getFarm() {
        return farm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setUrlM(String urlM) {
        this.urlM = urlM;
    }

    public String getUrlM() {
        return urlM;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setHeightM(String heightM) {
        this.heightM = heightM;
    }

    public String getHeightM() {
        return heightM;
    }

    public void setWidthM(String widthM) {
        this.widthM = widthM;
    }

    public String getWidthM() {
        return widthM;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    public int getIsfamily() {
        return isfamily;
    }

    @Override
    public String toString() {
        return
                "PhotoItem{" +
                        "owner = '" + owner + '\'' +
                        ",server = '" + server + '\'' +
                        ",ispublic = '" + ispublic + '\'' +
                        ",isfriend = '" + isfriend + '\'' +
                        ",farm = '" + farm + '\'' +
                        ",id = '" + id + '\'' +
                        ",secret = '" + secret + '\'' +
                        ",url_m = '" + urlM + '\'' +
                        ",title = '" + title + '\'' +
                        ",height_m = '" + heightM + '\'' +
                        ",width_m = '" + widthM + '\'' +
                        ",isfamily = '" + isfamily + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.owner);
        dest.writeString(this.server);
        dest.writeInt(this.ispublic);
        dest.writeInt(this.isfriend);
        dest.writeInt(this.farm);
        dest.writeString(this.id);
        dest.writeString(this.secret);
        dest.writeString(this.urlM);
        dest.writeString(this.title);
        dest.writeString(this.heightM);
        dest.writeString(this.widthM);
        dest.writeInt(this.isfamily);
    }

    public PhotoItem() {
    }

    protected PhotoItem(Parcel in) {
        this.owner = in.readString();
        this.server = in.readString();
        this.ispublic = in.readInt();
        this.isfriend = in.readInt();
        this.farm = in.readInt();
        this.id = in.readString();
        this.secret = in.readString();
        this.urlM = in.readString();
        this.title = in.readString();
        this.heightM = in.readString();
        this.widthM = in.readString();
        this.isfamily = in.readInt();
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel source) {
            return new PhotoItem(source);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };
}