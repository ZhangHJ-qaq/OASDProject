package com.haojie.dao.userDao;

import com.haojie.bean.User;
import com.haojie.others.ActionResult;

import java.util.List;

/**
 * 为User类设计的dao层
 */
public interface UserDao {

    /**
     * 插入一个用户 注册的时候会用到
     * @param user 用户对象
     * @return 封装了成功与否 与 信息的对象
     */
    public abstract ActionResult insertAUser(User user);

    /**
     * 尝试根据给出的用户名和密码登录（dao层主要是查找数据库里有没有对应的记录，并更新sessionid）
     * @param username 用户名
     * @param password 密码
     * @param newSessionID sessionid 用户每次登录以后都会更新在数据库里存储的sessionid 即：每个用户只能有一个sessionid 实现单点登录
     * @return 封装了成功与否 与 信息的对象
     */
    public abstract ActionResult tryLogin(String username, String password, String newSessionID);


    /**
     * 根据用户名和sessionid 尝试（自动）登录
     * @param username 用户名
     * @param sessionID sessionid
     * @return 如果登录成功，返回用户对象，如果登录失败则返回null
     */
    public abstract User tryAutoLogin(String username, String sessionID);


    /**
     * 在发出好友请求前，根据用户名搜索好友的函数
     * @param username
     * @return
     */
    public abstract List<User> searchUserToAddFriend(String username);


    public abstract boolean userExists(String username);

    public abstract User getUser(String username);

    public abstract User getUser(int uid);

    public abstract List<User> getMyFriendList(int myuid);

    public abstract boolean setCanBeSeenFavor(int uid,int canBeSeenFavor);

}

