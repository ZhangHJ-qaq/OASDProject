package com.haojie.dao.imageDao;

import com.haojie.bean.Image;
import com.haojie.bean.User;

import java.util.List;

public interface ImageDao {


    public abstract List<Image> getMostPopularNImages(int n);

    public abstract List<Image> getMostFreshNImages(int n);

    public abstract List<Image> search(String howToSearch, String howToOrder, String input);

    public abstract List<Image> getMyPhotos(User user);
}


