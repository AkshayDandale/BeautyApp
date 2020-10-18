package com.culexy.mehndiandrangoli.Modal;

import java.util.List;

public class RangoliGridModal {

    private String image;
    private List<String> RangoliallImageList;


    public RangoliGridModal(List<String> rangoliallImageList) {
        RangoliallImageList = rangoliallImageList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getRangoliallImageList() {
        return RangoliallImageList;
    }

    public void setRangoliallImageList(List<String> rangoliallImageList) {
        RangoliallImageList = rangoliallImageList;
    }
}
