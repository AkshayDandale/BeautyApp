package com.culexy.mehndiandrangoli.Modal;

import java.util.List;

public class NailGridModal {

    private String image;
    private List<String> NailallImageList;

    public NailGridModal(List<String> nailallImageList) {
        NailallImageList = nailallImageList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getNailallImageList() {
        return NailallImageList;
    }

    public void setNailallImageList(List<String> nailallImageList) {
        NailallImageList = nailallImageList;
    }
}
