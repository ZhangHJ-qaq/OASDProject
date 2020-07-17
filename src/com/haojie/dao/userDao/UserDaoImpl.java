package com.haojie.dao.userDao;

import com.haojie.bean.User;
import com.haojie.dao.genericDao.GenericDao;
import com.haojie.others.RegisterLoginResult;
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
    public RegisterLoginResult insertAUser(User user) {
        String sql = "insert into traveluser (Email, UserName, Pass, State, DateJoined, DateLastModified, salt, sessionID) VALUES (?,?,?,?,?,?,?,?);";
        try {
            if (userExists(user.getUsername())) return new RegisterLoginResult(false, "用户名已经存在，请换一个用户名再试");
            int insertedRecords = this.update(connection, sql, user.getEmail(), user.getUsername(), user.getPass(), user.getState(), user.getDateJoined(), user.getDateLastModified(), user.getSalt(), user.getSessionID());
            if (insertedRecords == 0) return new RegisterLoginResult(false, "注册失败");
            return new RegisterLoginResult(true, "注册成功");
        } catch (Exception e) {
            return new RegisterLoginResult(false, "注册失败");
        }


    }

    @Override
    public RegisterLoginResult tryLogin(String username, String password, String sessionID) {
        try {
            String sql = "select * from traveluser where username=?";
            List<User> userList = this.queryForList(this.connection, sql, username);
            if (userList.size() == 0) return new RegisterLoginResult(false, "用户名或密码错误");
            String salt = userList.get(0).getSalt();
            String encryptedPassword = MD5Utils.MD5(password + salt);

            sql = "select * from traveluser where username=? and pass=?";
            userList = this.queryForList(this.connection, sql, username, encryptedPassword);

            if (userList.size() == 1) {
                sql = "update traveluser set sessionID=? where UserName=?";
                this.update(connection, sql, sessionID, username);
                return new RegisterLoginResult(true, "登陆成功");
            }
            return new RegisterLoginResult(false, "用户名或密码错误");

        } catch (Exception e) {
            return new RegisterLoginResult(false, "登录失败");
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


    /**
     * 根据username检查这个用户是不是已经存在，在注册的时候用
     *
     * @param username
     * @return
     */
    private boolean userExists(String username) throws SQLException {
        String sql = "select UID from traveluser where UserName=?";
        List<User> list = this.queryForList(this.connection, sql, username);
        return list.size() != 0;

    }
}
