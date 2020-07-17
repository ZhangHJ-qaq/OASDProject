package com.haojie.others;

import com.haojie.bean.Image;

import java.util.List;

public class SearchResult {
    private List<Image> imageList;
    private int respondedPage;
    private int maxPage;

    public SearchResult(){

    }

    public SearchResult(List<Image> imageList, int respondedPage, int maxPage) {
        this.imageList = imageList;
        this.respondedPage = respondedPage;
        this.maxPage = maxPage;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public int getRespondedPage() {
        return respondedPage;
    }

    public void setRespondedPage(int respondedPage) {
        this.respondedPage = respondedPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
