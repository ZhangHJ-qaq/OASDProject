package com.haojie.dao.friendRequestDao;

import com.haojie.bean.FriendRequest;

import java.util.List;

public interface FriendRequestDao {
    public abstract boolean requestExists(int senderUID, int receiverUID);

    public abstract boolean addRequest(FriendRequest friendRequest);

    public abstract List<FriendRequest> getFriendRequestsIReceived(int myUid);

    public abstract FriendRequest getFriendRequestByID(int requestID);

    public abstract boolean deleteFriendRequest(int requestID);
}
