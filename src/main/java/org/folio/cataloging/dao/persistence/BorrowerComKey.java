/*
 * (c) LibriCore
 * 
 * Created on 18-ene-2005
 * 
 * BorrowerComKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BorrowerComKey implements Serializable{

	private int borrowerNumber;
	private int personalComunicationKeyNumber;
	
	
	

    /** class  constructor
     * 
     */
    public BorrowerComKey() {
        super();
    }
    
    public BorrowerComKey( int brwrNumber , int brwrComKeyNumber ){
        this.setBorrowerNumber(brwrNumber);
        this.setPersonalComunicationKeyNumber(brwrComKeyNumber);
    }
    
    
    /**
     * @return Returns the borrowerNumber.
     * @exception
     * @since 1.0
     */
    public int getBorrowerNumber() {
        return borrowerNumber;
    }
    /**
     * @param borrowerNumber The borrowerNumber to set.
     * @exception
     * @since 1.0
     */
    public void setBorrowerNumber(int borrowerNumber) {
        this.borrowerNumber = borrowerNumber;
    }
    /**
     * @return Returns the personalComunicationKeyNumber.
     * @exception
     * @since 1.0
     */
    public int getPersonalComunicationKeyNumber() {
        return personalComunicationKeyNumber;
    }
    /**
     * @param personalComunicationKeyNumber The personalComunicationKeyNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPersonalComunicationKeyNumber(
            int personalComunicationKeyNumber) {
        this.personalComunicationKeyNumber = personalComunicationKeyNumber;
    }
    

	/**
	 * override equals and hashcode for hibernate key comparison
	 */

	public boolean equals(Object anObject) {
		if (anObject instanceof BorrowerComKey) {
		    BorrowerComKey aKey = (BorrowerComKey) anObject;
			return (
			        borrowerNumber == aKey.getBorrowerNumber()
					&& personalComunicationKeyNumber == aKey.getPersonalComunicationKeyNumber());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return borrowerNumber + personalComunicationKeyNumber;
	}

    
}
