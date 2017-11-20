/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2004
 * 
 * NME_HDG_KEY.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for NME_HDG class
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2005/07/13 12:45:10 $
 * @since 1.0
 */
public class DescriptorKey implements Serializable{
	private int headingNumber;
	private String userViewString;

	/**
	 * Class constructor
	 *
	 * 
	 */
	public DescriptorKey() {
		super();
		headingNumber = -1;
		userViewString = "0000000000000000";
	}
	
	/**
	 * Class constructor
	 *
	 * 
	 */
	public DescriptorKey(int headingNumber, String view) {
		this.setHeadingNumber(headingNumber);
		this.setUserViewString(view);
	}

	/**
	 * override equals and hashcode for hibernate key comparison
	 */
	public boolean equals(Object anObject) {
		if (anObject instanceof DescriptorKey) {
			DescriptorKey aKey = (DescriptorKey) anObject;
			return (
				headingNumber == aKey.getHeadingNumber()
					&& userViewString.equals(aKey.getUserViewString()));
		} else {
			return false;
		}
	}

	public int hashCode() {
		return headingNumber + userViewString.hashCode();
	}

	/**
	 * Getter for headingNumber
	 * 
	 * @return headingNumber
	 */
	public int getHeadingNumber() {
		return headingNumber;
	}

	/**
	 * Getter for view
	 * 
	 * @return view
	 */
	public String getUserViewString() {
		return userViewString;
	}

	/**
	 * Setter for headingNumber
	 * 
	 * @param i headingNumber
	 */
	public void setHeadingNumber(int i) {
		headingNumber = i;
	}

	/**
	 * Setter for view
	 * 
	 * @param string view
	 */
	public void setUserViewString(String string) {
		userViewString = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Heading nbr: " + String.valueOf(headingNumber) + "  User view: " + userViewString;
	}

}
