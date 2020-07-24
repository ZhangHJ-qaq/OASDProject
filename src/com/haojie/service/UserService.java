package com.haojie.service;

import com.haojie.bean.Image;
import com.haojie.bean.ImageFavor;
import com.haojie.bean.User;
import com.haojie.dao.ImageFavorDao.ImageFavorDao;
import com.haojie.dao.ImageFavorDao.ImageFavorDaoImpl;
import com.haojie.dao.imageDao.ImageDao;
import com.haojie.dao.imageDao.ImageDaoImpl;
import com.haojie.dao.userDao.UserDao;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;
import com.haojie.utils.MD5Utils;
import com.haojie.utils.MyUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import sun.plugin2.main.server.ResultHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
     * @param username     用户名
     * @param email        邮箱
     * @param password1    密码1
     * @param password2    密码2
     * @param captchaInput 验证码输入
     * @return ActionResult对象
     */
    public ActionResult register(String username, String email, String password1, String password2, String captchaInput) {
        try {
            //在后端检查用户的各项输入是否符合要求
            if (!MyUtils.cleanXSS(username).equals(username)
                    || !MyUtils.cleanXSS(email).equals(email)
                    || !MyUtils.cleanXSS(password1).equals(password1)
                    || !MyUtils.cleanXSS(password2).equals(password2)
            ) {
                return new ActionResult(false, "注册失败");
            }
            if (!captchaInput.equals((String) httpSession.getAttribute("captcha"))) {
                return new ActionResult(false, "验证码输入错误");
            }

            if (!(username.length() >= 4 && username.length() <= 15)) {
                //如果用户名长度不符合要求
                return new ActionResult(false, "用户名长度必须在4-15位之间");
            }
            if (!password1.equals(password2)) {
                return new ActionResult(false, "两次密码输入不一致");
            }
            if (!(password1.length() >= 6 && password1.length() <= 12)) {
                return new ActionResult(false, "密码必须在6-12位之间");
            }
            if (!email.matches("([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})")) {
                return new ActionResult(false, "邮箱格式不符合要求");
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
            ActionResult result = userDao.insertAUser(new User(username, email, saltedPassword, 1,
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
            return new ActionResult(false, "注册失败");
        }


    }

    /**
     * 尝试用户登录
     *
     * @param username     用户名
     * @param password     密码
     * @param captchaInput 验证码输入
     * @return 结果对象
     */
    public ActionResult tryLogin(String username, String password, String captchaInput) {
        try {
            //检查用户的输入验证码是否正确
            if (!captchaInput.equals((String) httpSession.getAttribute("captcha"))) {
                return new ActionResult(false, "验证码输入错误");
            }

            //得到数据库连接
            Connection connection = this.connection;
            UserDaoImpl userDao = new UserDaoImpl(connection);

            //更换sessionid，防止固定会话攻击
            request.changeSessionId();

            //尝试根据用户提供的信息登录，并返回结果
            ActionResult result = userDao.tryLogin(username, password, request.getSession().getId());
            if (result.isSuccess()) {
                httpSession.setAttribute("username", username);
            }

            DbUtils.close(connection);
            return result;
        } catch (Exception e) {
            return new ActionResult(false, "登陆失败");
        }

    }


    /**
     * 根据session里面的username和sessionid，尝试自动登录
     *
     * @return 用户对象
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

    /**
     * 检查用户是否已经收藏了这个图片
     *
     * @param user    用户对象
     * @param imageID 图片id
     * @return true代表收藏了 false代表没收藏
     */
    public boolean hasFavoredTheImage(User user, int imageID) {
        try {
            ImageFavorDao imageFavorDao = new ImageFavorDaoImpl(this.connection);
            return imageFavorDao.hasLikedTheImage(user, imageID);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasTheImage(User user, int imageID) {
        try {
            ImageDao imageDao = new ImageDaoImpl(connection);
            return imageDao.imageExists(user, imageID);

        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 用户收藏图片的函数
     *
     * @param user    用户对象
     * @param imageID 图片id
     * @return 结果对象
     */
    public ActionResult favorImage(User user, int imageID) {
        if (user == null) return new ActionResult(false, "未登录的用户不可以收藏图片");
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (!imageDao.imageExists(imageID)) return new ActionResult(false, "图片不存在");
        try {
            boolean hasFavoredTheImage = this.hasFavoredTheImage(user, imageID);
            if (hasFavoredTheImage) return new ActionResult(false, "你已经收藏了这个图片");

            ImageFavorDao imageFavorDao = new ImageFavorDaoImpl(connection);

            boolean success = imageFavorDao.likeImage(user, imageID);
            if (success) return new ActionResult(true, "收藏成功");
            return new ActionResult(false, "收藏失败");

        } catch (Exception e) {
            return new ActionResult(false, "收藏失败");
        }
    }

    /**
     * 用户取消收藏图片的函数
     *
     * @param user    用户对象
     * @param imageID 图片id
     * @return 结果对象
     */
    public ActionResult unfavorImage(User user, int imageID) {
        if (user == null) return new ActionResult(false, "未登录的用户不可以取消收藏图片");
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (!imageDao.imageExists(imageID)) return new ActionResult(false, "图片不存在");
        try {
            boolean hasFavoredTheImage = this.hasFavoredTheImage(user, imageID);
            if (!hasFavoredTheImage) return new ActionResult(false, "你还没有收藏这个图片，不能取消收藏");

            ImageFavorDao imageFavorDao = new ImageFavorDaoImpl(connection);

            boolean success = imageFavorDao.unlikeImage(user, imageID);
            if (success) return new ActionResult(true, "取消收藏成功");
            return new ActionResult(false, "取消收藏失败");

        } catch (Exception e) {
            return new ActionResult(false, "取消收藏失败");
        }
    }

    public ActionResult insertImageToDB(User user, Image image) {
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (user == null) return new ActionResult(false, "没有登陆或登录已经过期");
        return imageDao.insertImage(user, image);
    }

    public ActionResult deleteImageFromDB(User user, int imageID) {
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (!imageDao.imageExists(user, imageID)) return new ActionResult(false, "你不能删除自己没有的图片");
        if (user == null) return new ActionResult(false, "没有登录或登录已经过期");
        return imageDao.deleteImage(imageID);
    }

    public ActionResult modifyImageInDB(User user, int imageID, Image image) {
        ImageDao imageDao = new ImageDaoImpl(connection);
        if (!imageDao.imageExists(user, imageID)) return new ActionResult(false, "你不能删除自己没有的图片");
        if (user == null) return new ActionResult(false, "没有登录或登录已经过期");

        return imageDao.modifyImage(user, imageID, image);

    }

    public SearchResult searchUserToAddFriend(String username, int requestedPage, int pageSize) {

        UserDao userDao = new UserDaoImpl(this.connection);
        List<User> userList = userDao.searchUserToAddFriend(username);

        SearchResult searchResult = getSearchResult(userList, requestedPage, pageSize);

        return searchResult;

    }

    public SearchResult searchMyFriend(int myuid, int requestedPage, int pageSize) {
        UserDao userDao = new UserDaoImpl(this.connection);
        List<User> myFriendList = userDao.getMyFriendList(myuid);

        SearchResult searchResult=getSearchResult(myFriendList,requestedPage,pageSize);
        return searchResult;

    }


    private SearchResult getSearchResult(List<User> originalUserList, int requestedPage, int pageSize) {

        ArrayList<User> subUserList = new ArrayList<>();

        SearchResult searchResult = new SearchResult();

        int maxPage = (int) Math.ceil((double) originalUserList.size() / pageSize);//得到最大页码数
        searchResult.setMaxPage(maxPage);

        requestedPage = Math.max(requestedPage, 1);
        requestedPage = Math.min(maxPage, requestedPage);

        searchResult.setRespondedPage(requestedPage);//净化用户输入的页码数，得到最终相应的页面

        int start = pageSize * (requestedPage - 1);
        int end = Math.min(start + pageSize, originalUserList.size());//根据最终相应的页面，得到应该从原始的list中截取哪一段

        for (int i = start; i < end; i++) {
            subUserList.add(originalUserList.get(i));
        }

        searchResult.setUserList(subUserList);

        return searchResult;

    }




}
