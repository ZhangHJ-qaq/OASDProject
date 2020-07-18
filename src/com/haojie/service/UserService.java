package com.haojie.service;

import com.haojie.bean.User;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.RegisterLoginResult;
import com.haojie.utils.MD5Utils;
import com.haojie.utils.MyUtils;
import org.apache.commons.dbutils.DbUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户的业务层
 */
public class UserService {
    private HttpSession httpSession;
    private final Connection connection;
    private final HttpServletRequest request;

    public UserService(Connection connection, HttpServletRequest request) {
        this.request = request;
        this.httpSession = request.getSession();
        this.connection = connection;
    }

    /**
     * 尝试用户的注册
     *
     * @param username
     * @param email
     * @param password1
     * @param password2
     * @param captchaInput
     * @return
     * @throws SQLException
     */
    public RegisterLoginResult register(String username, String email, String password1, String password2, String captchaInput) {
        try {
            //在后端检查用户的各项输入是否符合要求
            if (!MyUtils.cleanXSS(username).equals(username)
                    || !MyUtils.cleanXSS(email).equals(email)
                    || !MyUtils.cleanXSS(password1).equals(password1)
                    || !MyUtils.cleanXSS(password2).equals(password2)
            ) {
                return new RegisterLoginResult(false, "注册失败");
            }
            if (!captchaInput.equals((String) httpSession.getAttribute("captcha"))) {
                return new RegisterLoginResult(false, "验证码输入错误");
            }

            if (!(username.length() >= 4 && username.length() <= 15)) {
                //如果用户名长度不符合要求
                return new RegisterLoginResult(false, "用户名长度必须在4-15位之间");
            }
            if (!password1.equals(password2)) {
                return new RegisterLoginResult(false, "两次密码输入不一致");
            }
            if (!(password1.length() >= 6 && password1.length() <= 12)) {
                return new RegisterLoginResult(false, "密码必须在6-12位之间");
            }
            if (!email.matches("([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})")) {
                return new RegisterLoginResult(false, "邮箱格式不符合要求");
            }

            //从数据库连接池里得到连接
            Connection connection = this.connection;

            //用户的数据库访问曾
            UserDaoImpl userDao = new UserDaoImpl(connection);

            //随机生成盐及生成加盐后的密码
            int salt = new SecureRandom().nextInt();
            String saltedPassword = MD5Utils.MD5((String) password1 + salt);

            //更换sessionid,防止固定会话攻击
            request.changeSessionId();

            //尝试在数据层里插入用户，返回结果
            RegisterLoginResult result = userDao.insertAUser(new User(username, email, saltedPassword, 1,
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    salt + "", httpSession.getId()
            ));

            DbUtils.close(connection);
            if (result.isSuccess()) {
                httpSession.setAttribute("username", username);
            }
            return result;
        } catch (Exception e) {
            return new RegisterLoginResult(false, "注册失败");
        }


    }

    /**
     * 尝试用户登录
     *
     * @param username
     * @param password
     * @param captchaInput
     * @return
     */
    public RegisterLoginResult tryLogin(String username, String password, String captchaInput) {
        try {
            //检查用户的输入验证码是否正确
            if (!captchaInput.equals((String) httpSession.getAttribute("captcha"))) {
                return new RegisterLoginResult(false, "验证码输入错误");
            }

            //得到数据库连接
            Connection connection = this.connection;
            UserDaoImpl userDao = new UserDaoImpl(connection);

            //更换sessionid，防止固定会话攻击
            request.changeSessionId();

            //尝试根据用户提供的信息登录，并返回结果
            RegisterLoginResult result = userDao.tryLogin(username, password, request.getSession().getId());
            if (result.isSuccess()) {
                httpSession.setAttribute("username", username);
            }

            DbUtils.close(connection);
            return result;
        } catch (Exception e) {
            return new RegisterLoginResult(false, "登陆失败");
        }


    }


    /**
     * 根据session里面的username和sessionid，尝试自动登录
     *
     * @return
     */
    public User tryAutoLogin() {
        try {
            Connection connection = this.connection;
            UserDaoImpl userDao = new UserDaoImpl(connection);
            User user = userDao.tryAutoLogin((String) httpSession.getAttribute("username"), httpSession.getId());
            return user;

        } catch (Exception exception) {
            return null;
        }


    }


}
