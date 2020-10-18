package com.culexy.mehndiandrangoli.Modal;

import java.util.List;

public class HairStyleGridModal {

    private String image;
    private List<String> hairStyleallImageList;

    public List<String> getHairStyleallImageList() {
        return hairStyleallImageList;
    }

    public void setHairStyleallImageList(List<String> hairStyleallImageList) {
        this.hairStyleallImageList = hairStyleallImageList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HairStyleGridModal(List<String> hairStyleallImageList) {
        this.hairStyleallImageList = hairStyleallImageList;
    }
}
