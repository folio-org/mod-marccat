/*
 * (c) LibriCore
 * 
 * Created on 21-ene-2005
 * 
 * LV_USER_DATABASE.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_USER_DATABASE implements Serializable{
	
    private UserDatabaseKey key;
	private String userDatabasePassword;

    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public UserDatabaseKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(UserDatabaseKey key) {
        this.key = key;
    }
    /**
     * @return Returns the userDatabasePassword.
     * @exception
     * @since 1.0
     */
    public String getUserDatabasePassword() {
        return userDatabasePassword;
    }
    /**
     * @param userDatabasePassword The userDatabasePassword to set.
     * @exception
     * @since 1.0
     */
    public void setUserDatabasePassword(String userDatabasePassword) {
        this.userDatabasePassword = userDatabasePassword;
    }
}
