package com.haojie.service;

import com.haojie.bean.SysMessage;
import com.haojie.dao.sysmessageDao.SysMessageDao;
import com.haojie.dao.sysmessageDao.SysMessageDaoImpl;

import java.sql.Connection;

public class SysMessageService {
    private Connection connection;

    public SysMessageService(Connection connection) {
        this.connection = connection;
    }

    public boolean deleteMessage(int myUid, int messageID) {
        SysMessageDao sysMessageDao = new SysMessageDaoImpl(connection);
        SysMessage sysMessageToBeDeleted = sysMessageDao.getMessage(messageID);

        if (sysMessageToBeDeleted == null) return false;
        if (sysMessageToBeDeleted.getReceiverUID() != myUid) return false;

        return sysMessageDao.deleteMessage(messageID);

    }
}
