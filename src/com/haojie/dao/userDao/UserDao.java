package com.haojie.dao.userDao;

import com.haojie.bean.User;
import com.haojie.others.ActionResult;

import java.util.List;

/**
 * The Dao layer designed for the traveluser table.
 */
public interface UserDao {

    /**
     * Insert a new user into the database
     * @param user The user object
     * @return Actionresult object.
     */
    public abstract ActionResult insertAUser(User user);

    /**
     * Try login and update the sessionID
     * @param username username
     * @param password password
     * @param newSessionID The new sessionID. In this method, the user's sessionID stored in the database will be changed
     *                     to the new sessionID
     * @return ActionResult object.
     */
    public abstract ActionResult tryLogin(String username, String password, String newSessionID);


    /**
     * Try auto login according to username and sessionid
     * @param username username
     * @param sessionID sessionid
     * @return If login succeed, the user object entity will be returned. Otherwise ,you'll get a null.
     */
    public abstract User tryAutoLogin(String username, String sessionID);


    /**
     * To search the possible users when a user is trying to add friends
     * @param username username
     * @return The list of user
     */
    public abstract List<User> searchUserToAddFriend(String username);


    /**
     * To check whether a user has existed in the database
     * @param username username
     * @return If exists, returns true. Otherwise, false.
     */
    public abstract boolean userExists(String username);

    /**
     * Get the user entity
     * @param username username
     * @return The user entity
     */
    public abstract User getUser(String username);

    /**
     * Get the user entity
     * @param uid uid
     * @return The user object entity
     */
    public abstract User getUser(int uid);

    /**
     * Get all the friends of a specified user
     * @param myuid The uid of the operator
     * @return The user list
     */
    public abstract List<User> getMyFriendList(int myuid);

    /**
     * To configure whether a user's favors can be seen by his or her friends
     * @param uid The user's uid
     * @param canBeSeenFavor Two options. 1 represents can, while 0 represents cannot.
     * @return If action succeeds, returns true. Otherwise, returns false.
     */
    public abstract boolean setCanBeSeenFavor(int uid,int canBeSeenFavor);

}

