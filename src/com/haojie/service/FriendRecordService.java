package com.haojie.service;

import com.haojie.bean.FriendRecord;
import com.haojie.dao.friendRecordDao.FriendRecordDao;
import com.haojie.dao.friendRecordDao.FriendRecordDaoImpl;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;

/**
 * The service layer of friend record
 */
public class FriendRecordService {
    private Connection connection;

    public FriendRecordService(Connection connection) {
        this.connection = connection;
    }


    /**
     * Make two users friend
     * @param uid1 the uid of user A
     * @param uid2 the uid of user B
     * @return If success, returns true. Else, returns false.
     */
    public boolean makeFriend(int uid1, int uid2) {
        try {
            FriendRecordDao friendRecordDao = new FriendRecordDaoImpl(connection);
            //开启事务
            connection.setAutoCommit(false);

            boolean result1 = friendRecordDao.addFriendRecord(new FriendRecord(uid1, uid2));
            boolean result2 = friendRecordDao.addFriendRecord(new FriendRecord(uid2, uid1));

            boolean result = result1 & result2;

            if (result) {
                connection.commit();
            } else {
                connection.rollback();
            }
            connection.setAutoCommit(true);
            return result;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * To make two users no longer friends
     * @param uid1 The uid of user A
     * @param uid2 The uid of user B
     * @return
     */
    public boolean deleteFriend(int uid1, int uid2) {
        try {
            FriendRecordDao friendRecordDao = new FriendRecordDaoImpl(connection);

            FriendRecord friendRecord1=friendRecordDao.getFriendRecord(uid1,uid2);

            if(friendRecord1==null){
                return false;
            }


            connection.setAutoCommit(false);

            boolean result1 = friendRecordDao.deleteFriendRecord(uid1, uid2);
            boolean result2 = friendRecordDao.deleteFriendRecord(uid2, uid1);

            if (result1 & result2) {
                connection.commit();
            } else {
                connection.rollback();
            }
            connection.setAutoCommit(true);

            return result1 & result2;

        } catch (Exception e) {
            return false;
        }
    }


}
