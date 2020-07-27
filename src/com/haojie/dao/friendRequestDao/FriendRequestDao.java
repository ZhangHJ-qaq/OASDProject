package com.haojie.dao.friendRequestDao;

import com.haojie.bean.FriendRequest;

import java.util.List;

/**
 * The DAO layer for friend request
 */
public interface FriendRequestDao {

    /**
     * To check whether the friend request exists in the database.
     * @param senderUID The uid of the request maker
     * @param receiverUID The uid of the request receiver
     * @return If exists, returns true. Otherwise,false
     */
    public abstract boolean requestExists(int senderUID, int receiverUID);

    /**
     * To add a friend request into the database
     * @param friendRequest The friend request entity object
     * @return If successful, returns true. Otherwise, false.
     */
    public abstract boolean addRequest(FriendRequest friendRequest);

    /**
     * To get all the requests a user has received.
     * @param myUid The use's uid
     * @return Friend request list.
     */
    public abstract List<FriendRequest> getFriendRequestsIReceived(int myUid);


    /**
     * To get the friend request entity object
     * @param requestID The requestID that can represent the request.
     * @return The friend request entity
     */
    public abstract FriendRequest getFriendRequestByID(int requestID);

    /**
     * To delete the friend request from database
     * @param requestID
     * @return If delete success, returns true. Otherwise, false.
     */
    public abstract boolean deleteFriendRequest(int requestID);
}
