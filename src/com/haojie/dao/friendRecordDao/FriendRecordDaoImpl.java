package com.haojie.dao.friendRecordDao;

import com.haojie.bean.FriendRecord;
import com.haojie.dao.friendRequestDao.FriendRequestDao;
import com.haojie.dao.genericDao.GenericDao;

import java.sql.Connection;

public class FriendRecordDaoImpl extends GenericDao<FriendRecord> implements FriendRecordDao {
    private Connection connection;


    public FriendRecordDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addFriendRecord(FriendRecord friendRecord) {
        try {
            String sql = "insert into travelfriendrecord (UID1, UID2) VALUES (?,?);";
            int rowsAffected = this.update(connection, sql, friendRecord.getUID1(), friendRecord.getUID2());
            return rowsAffected > 0;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteFriendRecord(int uid1, int uid2) {
        try {
            String sql = "delete from travelfriendrecord where UID1=? and UID2=?";
            this.update(connection, sql, uid1, uid2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FriendRecord getFriendRecord(int uid1, int uid2) {
        try {
            String sql = "select * from travelfriendrecord where UID1=? and UID2=?";
            return this.queryForOne(connection, sql, uid1, uid2);

        } catch (Exception e) {
            return null;
        }
    }
}
