package com.haojie.dao.friendRecordDao;

import com.haojie.bean.FriendRecord;

public interface FriendRecordDao {

    public abstract boolean addFriendRecord(FriendRecord friendRecord);

    public abstract boolean deleteFriendRecord(int uid1,int uid2);

    public abstract FriendRecord getFriendRecord(int uid1,int uid2);
}
