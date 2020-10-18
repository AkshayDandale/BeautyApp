package com.culexy.mehndiandrangoli.Modal;

import java.util.List;

public class GridModal {

    private String image;
    private List<String> allImageList;

    public GridModal(List<String> allImageList) {
        this.allImageList = allImageList;
    }

    public List<String> getAllImageList() {
        return allImageList;
    }

    public void setAllImageList(List<String> allImageList) {
        this.allImageList = allImageList;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
