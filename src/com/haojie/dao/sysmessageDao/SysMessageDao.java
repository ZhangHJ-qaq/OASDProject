package com.haojie.dao.sysmessageDao;

import com.haojie.bean.SysMessage;

import java.util.List;

/**
 * The DAO layer for sysmessage
 */
public interface SysMessageDao {

    /**
     * To add a system message into the database.
     * @param sysMessage The sysMessage object
     * @return If success, returns true. Otherwise, returns false.
     */
    public abstract boolean addSysMessage(SysMessage sysMessage);

    /**
     * Get all the system messages a user has received
     * @param myUid uid of the user
     * @return List of message
     */
    public abstract List<SysMessage> getMyMessage(int myUid);

    /**
     * Get a message object entity according to the messageID
     * @param messageID messageID
     * @return Sysmessage object entity
     */
    public abstract SysMessage getMessage(int messageID);

    /**
     * To delete a message from database
     * @param messageID messageID
     * @return If success, returns true. Otherwise, false.
     */
    public abstract boolean deleteMessage(int messageID);
}
