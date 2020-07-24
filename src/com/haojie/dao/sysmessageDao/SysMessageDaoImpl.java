package com.haojie.dao.sysmessageDao;

import com.haojie.bean.SysMessage;
import com.haojie.dao.genericDao.GenericDao;

import java.sql.Connection;
import java.util.List;

public class SysMessageDaoImpl extends GenericDao<SysMessage> implements SysMessageDao {

    private Connection connection;

    public SysMessageDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addSysMessage(SysMessage sysMessage) {
        try {
            String sql = "insert into sysmessage (receiverUID, messageContent) VALUES (?,?)";
            int rowsAffected = this.update(connection, sql, sysMessage.getReceiverUID(), sysMessage.getMessageContent());
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public List<SysMessage> getMyMessage(int myUid) {
        try {
            String sql = "select * from sysmessage where receiverUID=?";
            return this.queryForList(connection, sql, myUid);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SysMessage getMessage(int messageID) {
        try {
            String sql = "select * from sysmessage where messageID=?";
            return this.queryForOne(connection, sql, messageID);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteMessage(int messageID) {
        try {
            String sql = "delete from sysmessage where messageID=?";
            int rowsAffected = this.update(connection, sql, messageID);
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
