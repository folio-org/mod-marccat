/*
 * (c) LibriCore
 * 
 * Created on 21-ene-2005
 * 
 * LV_USER_GROUP.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_USER_GROUP implements Serializable{
	
    private UserGroupKey key;

    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public UserGroupKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(UserGroupKey key) {
        this.key = key;
    }
}
