package com.haojie.servlet.otherServlet;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.haojie.bean.*;
import com.haojie.dao.ImageFavorDao.ImageFavorDao;
import com.haojie.dao.ImageFavorDao.ImageFavorDaoImpl;
import com.haojie.dao.friendRecordDao.FriendRecordDao;
import com.haojie.dao.friendRecordDao.FriendRecordDaoImpl;
import com.haojie.dao.friendRequestDao.FriendRequestDao;
import com.haojie.dao.friendRequestDao.FriendRequestDaoImpl;
import com.haojie.dao.sysmessageDao.SysMessageDao;
import com.haojie.dao.sysmessageDao.SysMessageDaoImpl;
import com.haojie.dao.userDao.UserDao;
import com.haojie.dao.userDao.UserDaoImpl;
import com.haojie.exception.*;
import com.haojie.others.ActionResult;
import com.haojie.others.SearchResult;
import com.haojie.others.UploadPhotoInfo;
import com.haojie.service.*;
import com.haojie.servlet.pageServlet.IndexServlet;
import com.haojie.utils.MyUtils;
import com.haojie.utils.PicReduce;
import org.apache.commons.dbutils.DbUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@MultipartConfig()
@WebServlet("/UserServlet")
/**
 *
 * 处理用户各种请求的servlet
 */

public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getParameter("method").equals("register")) {

                register(request, response);

                return;
            }
            if (request.getParameter("method").equals("login")) {

                login(request, response);

                return;
            }
            if (request.getParameter("method").equals("unfavor")) {
                unfavor(request, response);
                return;

            }
            if (request.getParameter("method").equals("upload")) {
                upload(request, response);
                return;
            }
            if (request.getParameter("method").equals("delete")) {
                delete(request, response);
                return;
            }
            if (request.getParameter("method").equals("modify")) {
                modify(request, response);
                return;
            }
            if (request.getParameter("method").equals("getUid")) {
                getUid(request, response);
                return;
            }
            if (request.getParameter("method").equals("searchFriendToAdd")) {
                searchFriendToAdd(request, response);
                return;
            }
            if (request.getParameter("method").equals("addfriend")) {
                addfriend(request, response);
                return;
            }
            if (request.getParameter("method").equals("getFriendRequests")) {
                getFriendRequests(request, response);
                return;
            }
            if (request.getParameter("method").equals("refuseRequest")) {
                refuseRequest(request, response);
                return;
            }
            if (request.getParameter("method").equals("acceptRequest")) {
                acceptRequest(request, response);
                return;
            }
            if (request.getParameter("method").equals("getSysMessage")) {
                getSysMessage(request, response);
                return;
            }
            if (request.getParameter("method").equals("readMessage")) {
                readMessage(request, response);
                return;
            }
            if (request.getParameter("method").equals("getMyFriend")) {
                getMyFriend(request, response);
                return;
            }
            if (request.getParameter("method").equals("deleteFriend")) {
                deleteFriend(request, response);
                return;
            }
            if (request.getParameter("method").equals("othersfavor")) {
                othersfavor(request, response);
                return;
            }
            if (request.getParameter("method").equals("comment")) {
                comment(request, response);
                return;
            }
            if (request.getParameter("method").equals("favorComment")) {
                favorComment(request, response);
                return;
            }
            if (request.getParameter("method").equals("cancelFavorComment")) {
                cancelFavorComment(request, response);
                return;
            }
            if (request.getParameter("method").equals("setCanBeSeenFavor")) {
                setCanBeSeenFavor(request, response);
                return;
            }


        } catch (Exception ignored) {

        }


    }

    /**
     * 处理用户的注册请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void register(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ActionResult registerResult = userService.register(
                    request.getParameter("username"), request.getParameter("email"), request.getParameter("password1"),
                    request.getParameter("password2"), request.getParameter("captcha")
            );
            DbUtils.close(connection);


            PrintWriter out = response.getWriter();
            out.println(JSON.toJSON(registerResult));
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    /**
     * 处理用户的登录请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ActionResult loginResult = userService.tryLogin(request.getParameter("username"), request.getParameter("password"), request.getParameter("captcha"));

            PrintWriter out = response.getWriter();
            Object o = JSON.toJSON(loginResult);
            out.println(o);
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    /**
     * 处理用户的取消收藏请求
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    private void unfavor(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, httpServletRequest);
            User user = userService.tryAutoLogin();
            ActionResult actionResult = userService.unfavorImage(user, Integer.parseInt(httpServletRequest.getParameter("imageID")));

            PrintWriter out = httpServletResponse.getWriter();
            out.println(JSON.toJSON(actionResult));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void upload(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            //检查用户是否登录
            UserService userService = new UserService(connection, request);
            User user = userService.tryAutoLogin();
            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录，或是登录已过期，请重新登录")));
                return;

            }


            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String content = request.getParameter("content");
            String country = request.getParameter("country");
            String city = request.getParameter("city");
            Part photo = request.getPart("photo");

            //得到上传图片信息的封装对象
            UploadPhotoInfo uploadPhotoInfo = new UploadPhotoInfo(title, description, city, country, content, photo);

            //清除xss
            uploadPhotoInfo.cleanXSS();

            //检测图片信息完整性
            try {
                uploadPhotoInfo.checkComplete();
            } catch (PhotoInfoIncompleteException photoInfoIncompleteException) {
                out.println(JSON.toJSON(new ActionResult(false, photoInfoIncompleteException.getMessage())));
                return;
            }

            //检测用户是否忘记选择了图片
            try {
                uploadPhotoInfo.checkFileExists();
            } catch (FileNotExistException fileNotExistException) {
                out.println(JSON.toJSON(new ActionResult(false, fileNotExistException.getMessage())));
            }

            //检测用户上传照片的大小是否太大了。
            try {
                uploadPhotoInfo.checkFileSize();
            } catch (SizeToLargeException sizeToLargeException) {
                out.println(JSON.toJSON(new ActionResult(false, sizeToLargeException.getMessage())));
            }


            //检测图片中国家与城市是否对应
            try {
                uploadPhotoInfo.checkCountryAndCity(connection);
            } catch (CountryCityMismatchException countryCityMismatchException) {
                out.println(JSON.toJSON(new ActionResult(false, countryCityMismatchException.getMessage())));
                return;
            }


            //检查图片类型
            try {
                uploadPhotoInfo.checkType();
            } catch (TypeIncorrectException typeIncorrectException) {
                out.println(JSON.toJSON(new ActionResult(false, typeIncorrectException.getMessage())));
                return;
            }


            uploadPhotoInfo.generateFileName(connection);


            Image image = new Image(
                    uploadPhotoInfo.getTitle(), uploadPhotoInfo.getDescription(), 0, 0, Integer.parseInt(uploadPhotoInfo.getCity()),
                    uploadPhotoInfo.getCountry(), user.getUid(), uploadPhotoInfo.getFilename(), uploadPhotoInfo.getContent(), new Timestamp(System.currentTimeMillis())

            );

            //开启事务
            connection.setAutoCommit(false);

            try {
                ActionResult insertResult = userService.insertImageToDB(user, image);

                String largeFilePlace = getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename();
                String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename();
                String smallFilePlace = getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename();

                uploadPhotoInfo.getPhoto().write(largeFilePlace);

                PicReduce.saveMinPhoto(largeFilePlace, mediumFilePlace, 800, 1);
                PicReduce.saveMinPhoto(largeFilePlace, smallFilePlace, 200, 1);

                if (!insertResult.isSuccess()) {
                    File largeFile = new File(largeFilePlace);
                    File mediumFile = new File(mediumFilePlace);
                    File smallFile = new File(smallFilePlace);
                    largeFile.delete();
                    mediumFile.delete();
                    smallFile.delete();
                    out.println(JSON.toJSON(new ActionResult(false, "上传失败")));
                    return;
                }
            } catch (Exception exception) {
                connection.rollback();
                File largeFile = new File(getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename());
                File mediumFile = new File(getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename());
                File smallFile = new File(getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename());
                largeFile.delete();
                mediumFile.delete();
                smallFile.delete();
                out.println(JSON.toJSON(new ActionResult(false, "上传失败")));
                return;
            }

            connection.commit();
            connection.setAutoCommit(true);
            out.println(JSON.toJSON(new ActionResult(true, "上传成功！")));


        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            connection = dataSource.getConnection();
            UserService userService = new UserService(connection, request);
            ImageService imageService = new ImageService(connection);
            ImageFavorDao imageFavorDao = new ImageFavorDaoImpl(connection);

            //得到imageID
            int imageID = Integer.parseInt(request.getParameter("imageID"));

            Image image = imageService.getImage(imageID);

            User user = userService.tryAutoLogin();

            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录或登录已过期")));
            }

            connection.setAutoCommit(false);

            ActionResult deleteResult = userService.deleteImageFromDB(user, imageID);
            boolean deleteFavorResult = imageFavorDao.deleteAllImageFavor(imageID);


            if (!deleteResult.isSuccess()) {
                out.println(JSON.toJSON(deleteResult));
                return;
            }

            String filename = image.getPath();

            String largeFilePlace = getServletContext().getRealPath("/photos/large/") + filename;
            String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + filename;
            String smallFilePlace = getServletContext().getRealPath("/photos/small/") + filename;
            File largeFile = new File(largeFilePlace);
            File mediumFile = new File(mediumFilePlace);
            File smallFile = new File(smallFilePlace);

            largeFile.delete();
            mediumFile.delete();
            smallFile.delete();

            boolean deleteLargeResult =
                    !largeFile.exists();
            boolean deleteMediumResult = !largeFile.exists();
            boolean deleteSmallResult = !largeFile.exists();

            if (deleteLargeResult && deleteMediumResult && deleteSmallResult && deleteResult.isSuccess() && deleteFavorResult) {
                connection.commit();
                out.println(JSON.toJSON(new ActionResult(true, "删除成功")));
                connection.setAutoCommit(true);

            } else {
                connection.rollback();
                out.println(JSON.toJSON(new ActionResult(false, "删除失败")));
                connection.setAutoCommit(true);
            }

        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void modify(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) this.getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            //检查用户是否登录
            UserService userService = new UserService(connection, request);
            ImageService imageService = new ImageService(connection);
            User user = userService.tryAutoLogin();
            int imageID = Integer.parseInt(request.getParameter("imageID"));


            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录，或是登录已过期，请重新登录")));
                return;
            }

            if (!userService.hasTheImage(user, imageID)) {
                out.println(JSON.toJSON(new ActionResult(false, "你还没有这张图片，不能修改！")));
                return;
            }


            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String content = request.getParameter("content");
            String country = request.getParameter("country");
            String city = request.getParameter("city");
            Part photo = request.getPart("photo");

            //得到上传图片信息的封装对象
            UploadPhotoInfo uploadPhotoInfo = new UploadPhotoInfo(title, description, city, country, content, photo);

            //清除xss
            uploadPhotoInfo.cleanXSS();

            //检测图片信息完整性
            try {
                uploadPhotoInfo.checkComplete();
            } catch (PhotoInfoIncompleteException photoInfoIncompleteException) {
                out.println(JSON.toJSON(new ActionResult(false, "你还没有这张图片，不能修改！")));
                return;
            }

            //检测图片中国家与城市是否对应
            try {
                uploadPhotoInfo.checkCountryAndCity(connection);
            } catch (CountryCityMismatchException countryCityMismatchException) {
                out.println(JSON.toJSON(new ActionResult(false, countryCityMismatchException.getMessage())));
                return;
            }

            boolean photoChanged = !(uploadPhotoInfo.getPhoto().getSubmittedFileName() == null);

            if (photoChanged) {
                //检查图片类型
                try {
                    uploadPhotoInfo.checkType();
                } catch (TypeIncorrectException typeIncorrectException) {
                    out.println(JSON.toJSON(new ActionResult(false, typeIncorrectException.getMessage())));
                    return;
                }

                //检测用户上传照片的大小是否太大了。
                try {
                    uploadPhotoInfo.checkFileSize();
                } catch (SizeToLargeException sizeToLargeException) {
                    out.println(JSON.toJSON(new ActionResult(false, sizeToLargeException.getMessage())));
                }

                //得到原先的图片信息
                Image originalImage = imageService.getImage(imageID);


                //得到原先的文件名
                String originalFilename = originalImage.getPath();


                //更新图片信息
                originalImage.setTitle(uploadPhotoInfo.getTitle());
                originalImage.setDescription(uploadPhotoInfo.getTitle());
                originalImage.setContent(uploadPhotoInfo.getContent());
                originalImage.setCountry_RegionCodeISO(uploadPhotoInfo.getCountry());
                originalImage.setCityCode(Integer.parseInt(uploadPhotoInfo.getCity()));
                originalImage.setPath(uploadPhotoInfo.generateFileName(connection));
                originalImage.setDateReleased(new Timestamp(System.currentTimeMillis()));

                Image newImage = originalImage;


                //开启事务
                connection.setAutoCommit(false);


                //这是修改数据库中字段的结果
                ActionResult modifyResult = userService.modifyImageInDB(user, imageID, newImage);

                //如果修改数据库不成功则退出
                if (!modifyResult.isSuccess()) {
                    out.println(JSON.toJSON(modifyResult));
                    connection.setAutoCommit(true);
                    return;
                }


                //得到源文件的路径和新文件的路径
                String largeFilePlace = getServletContext().getRealPath("/photos/large/") + uploadPhotoInfo.getFilename();
                String originalLargeFilePlace = getServletContext().getRealPath("/photos/large/") + originalFilename;

                String mediumFilePlace = getServletContext().getRealPath("/photos/medium/") + uploadPhotoInfo.getFilename();
                String originalMediumFilePlace = getServletContext().getRealPath("/photos/medium/") + originalFilename;

                String smallFilePlace = getServletContext().getRealPath("/photos/small/") + uploadPhotoInfo.getFilename();
                String originalSmallFilePlace = getServletContext().getRealPath("/photos/small/") + originalFilename;

                try {
                    //尝试删除源文件，添加新文件
                    uploadPhotoInfo.getPhoto().write(largeFilePlace);
                    PicReduce.saveMinPhoto(largeFilePlace, mediumFilePlace, 800, 1);
                    PicReduce.saveMinPhoto(largeFilePlace, smallFilePlace, 200, 1);

                    File originalLargeFile = new File(originalLargeFilePlace);
                    File originalMediumFile = new File(originalMediumFilePlace);
                    File originalSmallFile = new File(originalSmallFilePlace);

                    originalLargeFile.delete();
                    originalMediumFile.delete();
                    originalSmallFile.delete();

                    //提交
                    connection.commit();
                    out.println(JSON.toJSON(new ActionResult(true, "修改成功")));
                    return;
                } catch (Exception e) {
                    //如果出错则回滚，删除新图片
                    new File(largeFilePlace).delete();
                    new File(smallFilePlace).delete();
                    new File(mediumFilePlace).delete();
                    connection.rollback();
                    out.println(JSON.toJSON(new ActionResult(false, "修改失败")));
                    return;
                } finally {
                    connection.setAutoCommit(true);
                }


            } else {//如果图片没有更改
                Image originalImage = imageService.getImage(imageID);
                originalImage.setTitle(uploadPhotoInfo.getTitle());
                originalImage.setDescription(uploadPhotoInfo.getTitle());
                originalImage.setContent(uploadPhotoInfo.getContent());
                originalImage.setCountry_RegionCodeISO(uploadPhotoInfo.getCountry());
                originalImage.setCityCode(Integer.parseInt(uploadPhotoInfo.getCity()));
                originalImage.setDateReleased(new Timestamp(System.currentTimeMillis()));

                Image newImage = originalImage;

                ActionResult modifyResult = userService.modifyImageInDB(user, imageID, newImage);

                out.println(JSON.toJSON(modifyResult));
            }


        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void getUid(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();

            if (user == null) return;

            HashMap<String, Integer> hashMap = new HashMap<>();
            hashMap.put("uid", user.getUid());

            PrintWriter out = response.getWriter();

            out.println(JSON.toJSON(hashMap));

        } catch (Exception ignored) {
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void searchFriendToAdd(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();

            if (user == null) return;

            String username = request.getParameter("username");
            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));

            SearchResult userSearchResult = userService.searchUserToAddFriend(
                    username, requestedPage, pageSize
            );

            PrintWriter out = response.getWriter();

            out.println(JSON.toJSON(userSearchResult));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void addfriend(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);
            UserDao userDao = new UserDaoImpl(connection);

            User user = userService.tryAutoLogin();

            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录或是登录已过期")));
                return;
            }

            String targetUsername = request.getParameter("username");

            User targetUser = userDao.getUser(targetUsername);

            if (targetUser == null) {
                out.println(JSON.toJSON(new ActionResult(false, "目标的用户不存在")));
                return;
            }

            int myUID = user.getUid();
            int targetUID = targetUser.getUid();

            FriendRequestService friendRequestService = new FriendRequestService(connection);

            ActionResult addRequestResult = friendRequestService.addRequest(new FriendRequest(myUID, targetUID));

            out.println(JSON.toJSON(addRequestResult));


        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }


    }

    private void getFriendRequests(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);

            User user = userService.tryAutoLogin();

            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "你还没有登陆")));
                return;
            }

            int uid = user.getUid();

            FriendRequestService friendRequestService = new FriendRequestService(connection);

            List<FriendRequest> friendRequestList = friendRequestService.getFriendRequestsIReceived(uid);

            out.println(JSON.toJSON(friendRequestList));

        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void refuseRequest(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            PrintWriter out = response.getWriter();
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            UserService userService = new UserService(connection, request);
            FriendRequestService friendRequestService = new FriendRequestService(connection);


            User user = userService.tryAutoLogin();
            int requestID = Integer.parseInt(request.getParameter("requestID"));
            if (user == null) {
                out.println(JSON.toJSON(new ActionResult(false, "没有登录或登录已过期")));
                return;
            }


            //在service层中进行拒绝请求，并返回拒绝结果
            ActionResult refuseResult = friendRequestService.refuseRequest(user, requestID);


            out.println(JSON.toJSON(refuseResult));

        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void acceptRequest(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();

            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int requestID = Integer.parseInt(request.getParameter("requestID"));

            FriendRequestService friendRequestService = new FriendRequestService(connection);

            ActionResult acceptResult = friendRequestService.acceptRequest(me, requestID);

            out.println(JSON.toJSON(acceptResult));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void getSysMessage(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            SysMessageDao sysMessageDao = new SysMessageDaoImpl(connection);

            List<SysMessage> sysMessageList = sysMessageDao.getMyMessage(me.getUid());

            out.println(JSON.toJSON(sysMessageList));
        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void readMessage(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            SysMessageService sysMessageService = new SysMessageService(connection);

            int messageID = Integer.parseInt(request.getParameter("messageID"));

            sysMessageService.deleteMessage(me.getUid(), messageID);

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void getMyFriend(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));

            SearchResult searchResult = userService.searchMyFriend(me.getUid(), requestedPage, pageSize);

            out.println(JSON.toJSON(searchResult));

        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void deleteFriend(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            FriendRecordService friendRecordService = new FriendRecordService(connection);

            int targetUid = Integer.parseInt(request.getParameter("targetUid"));

            boolean deleteFriendResult = friendRecordService.deleteFriend(me.getUid(), targetUid);

            if (deleteFriendResult) {
                SysMessageDao sysMessageDao = new SysMessageDaoImpl(connection);
                sysMessageDao.addSysMessage(new SysMessage(targetUid, me.getUsername() + "已删除你好友"));

                out.println(JSON.toJSON(new ActionResult(true, "删除好友成功")));
            } else {
                out.println(JSON.toJSON(new ActionResult(false, "删除好友失败")));
            }


        } catch (Exception ignored) {

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void othersfavor(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();

            UserService userService = new UserService(connection, request);
            UserDao userDao = new UserDaoImpl(connection);
            FriendRecordDao friendRecordDao = new FriendRecordDaoImpl(connection);

            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            String othersusername = request.getParameter("username");
            User other = userDao.getUser(othersusername);

            if (other == null) {
                out.println(JSON.toJSON(new ActionResult(false, "这个用户不存在！")));
                return;
            }
            if (other.getCanBeSeenFavors() != 1) {
                out.println(JSON.toJSON(new ActionResult(false, "对方设置了不能被好友查看收藏")));
                return;
            }


            //查找我是不是对方的好友
            FriendRecord friendRecord = friendRecordDao.getFriendRecord(other.getUid(), me.getUid());
            if (friendRecord == null) {
                out.println(JSON.toJSON(new ActionResult(false, "你不是对方的好友，不能查看对方的收藏")));
                return;
            }

            ImageService imageService = new ImageService(connection);

            int requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));

            SearchResult searchResult = imageService.getFavor(other, requestedPage, pageSize);

            out.println(JSON.toJSON(searchResult));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void comment(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();


            UserService userService = new UserService(connection, request);


            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            //得到imageID和用户输入的评论内容
            int imageID = Integer.parseInt(request.getParameter("imageID"));
            String text = MyUtils.cleanXSS(request.getParameter("comment"));

            //创建新的评论对象
            Comment newComment = new Comment(imageID, me.getUid(), text, new Timestamp(System.currentTimeMillis()));

            CommentService commentService = new CommentService(connection);

            //插入评论，得到结果
            ActionResult commentResult = commentService.addComment(newComment);

            out.println(JSON.toJSON(commentResult));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private void favorComment(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();


            UserService userService = new UserService(connection, request);


            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int commentID = Integer.parseInt(request.getParameter("commentID"));


            ActionResult favorCommentResult = userService.favorComment(me.getUid(), commentID);

            out.println(JSON.toJSON(favorCommentResult));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void cancelFavorComment(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();


            UserService userService = new UserService(connection, request);


            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int commentID = Integer.parseInt(request.getParameter("commentID"));


            ActionResult favorCommentResult = userService.cancelFavorComment(me.getUid(), commentID);

            out.println(JSON.toJSON(favorCommentResult));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }

    }

    private void setCanBeSeenFavor(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
            connection = dataSource.getConnection();
            PrintWriter out = response.getWriter();


            UserService userService = new UserService(connection, request);


            User me = userService.tryAutoLogin();

            if (me == null) {
                out.println(JSON.toJSON(new ActionResult(false, "未登录或登录已过期")));
                return;
            }

            int canBeSeenFavor = Integer.parseInt(request.getParameter("canBeSeenFavor"));

            ActionResult sendResult = userService.setCanBeSeenFavor(me.getUid(), canBeSeenFavor);

            out.println(JSON.toJSON(sendResult));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

}
