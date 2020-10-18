package com.culexy.mehndiandrangoli.Modal;

import java.util.List;

public class FavouriteModal {

   private List<String> favouriteImageList;

    public List<String> getFavouriteImageList() {
        return favouriteImageList;
    }

    public void setFavouriteImageList(List<String> favouriteImageList) {
        this.favouriteImageList = favouriteImageList;
    }

    public FavouriteModal(List<String> favouriteImageList) {
        this.favouriteImageList = favouriteImageList;
    }
}
