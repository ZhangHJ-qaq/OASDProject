package com.haojie.service;

import com.haojie.bean.FriendRecord;
import com.haojie.bean.FriendRequest;
import com.haojie.bean.SysMessage;
import com.haojie.bean.User;
import com.haojie.dao.friendRecordDao.FriendRecordDao;
import com.haojie.dao.friendRecordDao.FriendRecordDaoImpl;
import com.haojie.dao.friendRequestDao.FriendRequestDao;
import com.haojie.dao.friendRequestDao.FriendRequestDaoImpl;
import com.haojie.dao.sysmessageDao.SysMessageDao;
import com.haojie.dao.sysmessageDao.SysMessageDaoImpl;
import com.haojie.others.ActionResult;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class FriendRequestService {
    private Connection connection;

    public FriendRequestService(Connection connection) {
        this.connection = connection;
    }


    /**
     * 向数据库中添加请求
     *
     * @param friendRequest
     * @return
     */
    public ActionResult addRequest(FriendRequest friendRequest) {
        try {
            if (friendRequest.getSenderUID() == friendRequest.getReceiverUID()) {//检测用户是否添加自己为好友
                return new ActionResult(false, "你不能添加自己为好友");
            }

            FriendRequestDao friendRequestDao = new FriendRequestDaoImpl(connection);

            if (friendRequestDao.requestExists(friendRequest.getSenderUID(), friendRequest.getReceiverUID())) {
                return new ActionResult(false, "你已经向这个用户发出过好友申请了，请耐心等待TA回复");
            }

            boolean success = friendRequestDao.addRequest(friendRequest);

            if (success) {
                return new ActionResult(true, "已向对方发送申请");
            } else {
                return new ActionResult(false, "发送申请失败");
            }

        } catch (Exception e) {
            return new ActionResult(false, "加好友失败");
        }


    }

    public List<FriendRequest> getFriendRequestsIReceived(int myUid) {
        try {
            FriendRequestDao friendRequestDao = new FriendRequestDaoImpl(connection);

            return friendRequestDao.getFriendRequestsIReceived(myUid);

        } catch (Exception e) {
            return null;
        }

    }

    public ActionResult refuseRequest(User me, int requestID) {

        FriendRequestDao friendRequestDao = new FriendRequestDaoImpl(connection);

        //得到friendRequest对象
        FriendRequest friendRequest = friendRequestDao.getFriendRequestByID(requestID);

        if (friendRequest == null) return new ActionResult(false, "没有找到这个请求");

        //如果这个请求的收信人不是正在操作的用户的话
        if (me.getUid() != friendRequest.getReceiverUID()) return new ActionResult(false, "没有找到这个请求");

        //在数据库中删除请求信息
        boolean success = friendRequestDao.deleteFriendRequest(requestID);

        SysMessageDao sysMessageDao = new SysMessageDaoImpl(connection);


        if (success) {
            //给发好友请求的人发一条对方拒绝了你的好友请求的系统消息
            sysMessageDao.addSysMessage(new SysMessage(
                    friendRequest.getSenderUID(), me.getUsername() + "拒绝了你的好友请求。"
            ));

            return new ActionResult(true, "已拒绝用户" + friendRequest.getSenderUsername());
        } else {
            return new ActionResult(false, "拒绝失败");
        }

    }

    public ActionResult acceptRequest(User me, int requestID) {
        try {
            FriendRequestDao friendRequestDao = new FriendRequestDaoImpl(connection);

            //得到friendRequest对象
            FriendRequest friendRequest = friendRequestDao.getFriendRequestByID(requestID);

            if (friendRequest == null) return new ActionResult(false, "没有找到这个请求");

            //如果操作者不是消息的收信人的话
            if (friendRequest.getReceiverUID() != me.getUid()) return new ActionResult(false, "没有找到这个请求");


            boolean resultOfDeleteMessage = friendRequestDao.deleteFriendRequest(requestID);


            if (resultOfDeleteMessage) {

                SysMessageDao sysMessageDao = new SysMessageDaoImpl(connection);

                FriendRecordService friendRecordService = new FriendRecordService(connection);

                sysMessageDao.addSysMessage(
                        new SysMessage(friendRequest.getSenderUID(), me.getUsername() + "已同意你的好友请求。")
                );

                friendRecordService.makeFriend(friendRequest.getSenderUID(), me.getUid());

                return new ActionResult(true,"接受请求成功");
            } else {
                return new ActionResult(false, "接受请求失败");
            }

        } catch (Exception e) {
            return new ActionResult(false, "接受请求失败");
        }
    }


}
