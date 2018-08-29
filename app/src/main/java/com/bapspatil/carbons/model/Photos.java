package com.bapspatil.carbons.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {

    @SerializedName("perpage")
    private int perpage;

    @SerializedName("total")
    private String total;

    @SerializedName("pages")
    private int pages;

    @SerializedName("photo")
    private List<PhotoItem> photo;

    @SerializedName("page")
    private int page;

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }

    public void setPhoto(List<PhotoItem> photo) {
        this.photo = photo;
    }

    public List<PhotoItem> getPhoto() {
        return photo;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return
                "Photos{" +
                        "perpage = '" + perpage + '\'' +
                        ",total = '" + total + '\'' +
                        ",pages = '" + pages + '\'' +
                        ",photo = '" + photo + '\'' +
                        ",page = '" + page + '\'' +
                        "}";
    }
}