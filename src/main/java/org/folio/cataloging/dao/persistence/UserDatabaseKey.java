/*
 * (c) LibriCore
 * 
 * Created on 21-ene-2005
 * 
 * UserDatabaseKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class UserDatabaseKey implements Serializable {
	private int userNumber;
	private int databaseNumber;



	/**
	 * Class constructor
	 *
	 * 
	 */
	public UserDatabaseKey() {
		super();
	}

	public UserDatabaseKey(int userNumber, int databaseNumber) {
		this.setUserNumber(userNumber);
		this.setDatabaseNumber(databaseNumber);
	}

	/**
	 * override equals and hashcode for hibernate key comparison
	 */

	public boolean equals(Object anObject) {
		if (anObject instanceof UserDatabaseKey) {
		    UserDatabaseKey aKey = (UserDatabaseKey) anObject;
			return (
			        userNumber == aKey.getUserNumber()
					&& databaseNumber == aKey.getDatabaseNumber());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return userNumber + databaseNumber;
	}



    /**
     * @return Returns the databaseNumber.
     * @exception
     * @since 1.0
     */
    public int getDatabaseNumber() {
        return databaseNumber;
    }
    /**
     * @param databaseNumber The databaseNumber to set.
     * @exception
     * @since 1.0
     */
    public void setDatabaseNumber(int databaseNumber) {
        this.databaseNumber = databaseNumber;
    }
    /**
     * @return Returns the userNumber.
     * @exception
     * @since 1.0
     */
    public int getUserNumber() {
        return userNumber;
    }
    /**
     * @param userNumber The userNumber to set.
     * @exception
     * @since 1.0
     */
    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }
}
