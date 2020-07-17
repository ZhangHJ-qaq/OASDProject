package com.haojie.dao.userDao;

import com.haojie.bean.User;
import com.haojie.others.RegisterLoginResult;

public interface UserDao {
    public abstract RegisterLoginResult insertAUser(User user);

    public abstract RegisterLoginResult tryLogin(String username, String password, String sessionID);

    public abstract User tryAutoLogin(String username, String sessionID);

}

