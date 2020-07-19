package com.haojie.others;

import com.haojie.bean.Image;

import java.util.List;

/**
 * 封装了搜索图片得到的结果的对象。imageList为图片列表，maxPage表示最大页数，respondedPage表示相应页数
 */
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
