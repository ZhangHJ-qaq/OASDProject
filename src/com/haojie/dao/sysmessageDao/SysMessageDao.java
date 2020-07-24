package com.haojie.dao.sysmessageDao;

import com.haojie.bean.SysMessage;

import java.util.List;

public interface SysMessageDao {

    public abstract boolean addSysMessage(SysMessage sysMessage);

    public abstract List<SysMessage> getMyMessage(int myUid);

    public abstract SysMessage getMessage(int messageID);

    public abstract boolean deleteMessage(int messageID);
}
