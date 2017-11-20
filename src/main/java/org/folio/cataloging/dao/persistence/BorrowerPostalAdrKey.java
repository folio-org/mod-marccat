/*
 * (c) LibriCore
 * 
 * Created on 24-ene-2005
 * 
 * BRWR_PSTL_ADR_Key.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BorrowerPostalAdrKey implements Serializable{
    
    private int borrowerNumber;
	private short personalPostalAddressTypeCode;
	
	
	 /** class  constructor
     * 
     */
    public BorrowerPostalAdrKey() {
        super();
    }
    
    public BorrowerPostalAdrKey( int brwrNumber , short prsnPostalAdrCde ){
        this.setBorrowerNumber(brwrNumber);
        this.setPersonalPostalAddressTypeCode(prsnPostalAdrCde);
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
     * @return Returns the personalPostalAddressTypeCode.
     * @exception
     * @since 1.0
     */
    public short getPersonalPostalAddressTypeCode() {
        return personalPostalAddressTypeCode;
    }
    /**
     * @param personalPostalAddressTypeCode The personalPostalAddressTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPersonalPostalAddressTypeCode(
            short personalPostalAddressTypeCode) {
        this.personalPostalAddressTypeCode = personalPostalAddressTypeCode;
    }
    
    /**
	 * override equals and hashcode for hibernate key comparison
	 */

	public boolean equals(Object anObject) {
		if (anObject instanceof BorrowerPostalAdrKey) {
		    BorrowerPostalAdrKey aKey = (BorrowerPostalAdrKey) anObject;
			return (
			        borrowerNumber == aKey.getBorrowerNumber()
					&& personalPostalAddressTypeCode == aKey.getPersonalPostalAddressTypeCode());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return borrowerNumber + personalPostalAddressTypeCode;
	}
}
