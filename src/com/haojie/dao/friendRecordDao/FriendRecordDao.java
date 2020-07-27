package com.haojie.dao.friendRecordDao;

import com.haojie.bean.FriendRecord;

/**
 * The DAO layer for friend record.
 */
public interface FriendRecordDao {

    /**
     * To insert a new friend record into the database
     *
     * @param friendRecord The new friend record object entity.
     * @return If successful, returns true. Otherwise, false.
     */
    public abstract boolean addFriendRecord(FriendRecord friendRecord);

    /**
     * To delete the friend record from database.
     *
     * @param uid1 The uid of the first user
     * @param uid2 The uid of the second user
     * @return If successful, returns true, Otherwise, returns false.
     */
    public abstract boolean deleteFriendRecord(int uid1, int uid2);


    /**
     * To get the fried record object from the database.
     *
     * @param uid1 The uid of the first user.
     * @param uid2 The uid of the second user.
     * @return The friend record entity
     */
    public abstract FriendRecord getFriendRecord(int uid1, int uid2);
}
