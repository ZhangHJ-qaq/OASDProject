package com.haojie.dao.userDao;

import com.haojie.bean.ImageFavor;
import com.haojie.bean.User;
import com.haojie.dao.genericDao.GenericDao;
import com.haojie.others.ActionResult;
import com.haojie.utils.MD5Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl extends GenericDao<User> implements UserDao {
    private Connection connection;


    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }


    /**
     * 在数据表中插入一个用户
     *
     * @param user
     * @return
     */
    @Override
    public ActionResult insertAUser(User user) {
        String sql = "insert into traveluser (Email, UserName, Pass, State, DateJoined, DateLastModified, salt, sessionID) VALUES (?,?,?,?,?,?,?,?);";
        try {
            if (userExists(user.getUsername())) return new ActionResult(false, "用户名已经存在，请换一个用户名再试");
            int insertedRecords = this.update(connection, sql, user.getEmail(), user.getUsername(), user.getPass(), user.getState(), user.getDateJoined(), user.getDateLastModified(), user.getSalt(), user.getSessionID());
            if (insertedRecords == 0) return new ActionResult(false, "注册失败");
            return new ActionResult(true, "注册成功");
        } catch (Exception e) {
            return new ActionResult(false, "注册失败");
        }


    }

    @Override
    public ActionResult tryLogin(String username, String password, String newSessionID) {
        try {
            String sql = "select * from traveluser where username=?";
            List<User> userList = this.queryForList(this.connection, sql, username);
            if (userList.size() == 0) return new ActionResult(false, "用户名或密码错误");
            String salt = userList.get(0).getSalt();
            String encryptedPassword = MD5Utils.MD5(password + salt);

            sql = "select * from traveluser where username=? and pass=?";
            userList = this.queryForList(this.connection, sql, username, encryptedPassword);

            if (userList.size() == 1) {
                sql = "update traveluser set sessionID=? where UserName=?";
                this.update(connection, sql, newSessionID, username);
                return new ActionResult(true, "登陆成功");
            }
            return new ActionResult(false, "用户名或密码错误");

        } catch (Exception e) {
            return new ActionResult(false, "登录失败");
        }


    }

    /**
     * 根据session里的username和sessionID尝试自动登录
     *
     * @param username
     * @param sessionID
     * @return
     */
    @Override
    public User tryAutoLogin(String username, String sessionID) {
        try {

            String sql = "select * from traveluser where UserName=? and sessionID=?";
            User user = this.queryForOne(this.connection, sql, username, sessionID);
            return user;
        } catch (Exception e) {
            return null;
        }

    }


    @Override
    public List<User> searchUserToAddFriend(String username) {
        try {
            String sql = "select UserName,DateJoined,Email from traveluser where UserName regexp ?";
            return this.queryForList(connection, sql, username);
        } catch (Exception e) {
            return null;
        }


    }


    /**
     * 根据username检查这个用户是不是已经存在，在注册的时候用
     *
     * @param username
     * @return
     */
    public boolean userExists(String username) {
        try {
            return !(this.getUser(username) == null);
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public User getUser(String username) {
        try {
            String sql = "select * from traveluser where username=?";
            User user = this.queryForOne(this.connection, sql, username);
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public User getUser(int uid) {
        try {
            String sql = "select * from traveluser where UID=?";
            return this.queryForOne(connection, sql, uid);

        } catch (Exception e) {
            return null
                    ;
        }
    }

    @Override
    public List<User> getMyFriendList(int myuid) {
        try {
            String sql = "select UserName,UID,Email,DateJoined  from travelfriendrecord inner join traveluser on travelfriendrecord.UID2=traveluser.UID where UID1=?";
            return this.queryForList(connection, sql, myuid);


        } catch (Exception e) {
            return null;
        }

    }


}
