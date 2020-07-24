package com.haojie.dao.friendRequestDao;

import com.haojie.bean.FriendRequest;
import com.haojie.dao.genericDao.GenericDao;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.util.List;

public class FriendRequestDaoImpl extends GenericDao<FriendRequest> implements FriendRequestDao {
    private Connection connection;

    public FriendRequestDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean requestExists(int senderUID, int receiverUID) {
        try {
            String sql = "select * from friendrequest where senderUID=? and receiverUID=?";
            FriendRequest friendRequest
                    = this.queryForOne(connection, sql, senderUID, receiverUID);
            return !(friendRequest == null);

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addRequest(FriendRequest friendRequest) {
        try {
            String sql = "insert into friendrequest (senderUID, receiverUID) VALUES (?,?)";
            int rowsAffected = this.update(connection, sql, friendRequest.getSenderUID(), friendRequest.getReceiverUID());
            return rowsAffected > 0;

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public List<FriendRequest> getFriendRequestsIReceived(int myUid) {
        try {
            String sql = "select traveluser.UserName as senderUsername,senderUID,receiverUID,RequestID from friendrequest inner join traveluser on senderUID=traveluser.UID where receiverUID=?";
            List<FriendRequest> friendRequestList = this.queryForList(connection, sql, myUid);
            return friendRequestList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public FriendRequest getFriendRequestByID(int requestID) {
        try {
            String sql = "select traveluser.UserName as senderUsername,senderUID,receiverUID,RequestID from friendrequest inner join traveluser on senderUID=traveluser.UID where RequestID=?";
            return this.queryForOne(connection, sql, requestID);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteFriendRequest(int requestID) {
        try {
            String sql = "delete from friendrequest where RequestID=?";
            int rowsAffected = this.update(connection, sql, requestID);
            return rowsAffected>0;
        } catch (Exception e) {
            return false;
        }
    }
}
