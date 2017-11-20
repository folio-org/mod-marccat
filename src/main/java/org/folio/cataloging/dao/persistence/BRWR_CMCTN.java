/*
 * (c) LibriCore
 * 
 * Created on 19-nov-2004
 * 
 * BRWR_CMCTN.java
 */

package org.folio.cataloging.dao.persistence;

import java.io.Serializable;


/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class BRWR_CMCTN implements Serializable{

    private BorrowerComKey key;
	private short lockingVersionUpdateNumber;
	private String borrowerComunicationNumber;
	private short personalComunicationTypeCode;
	private short personalComunicationMediumTypeCode;
	private Short preferenceComunicationTypeCode;
	

    /**
     * @return Returns the borrowerComunicationNumber.
     * @exception
     * @since 1.0
     */
    public String getBorrowerComunicationNumber() {
        return borrowerComunicationNumber;
    }
    /**
     * @param borrowerComunicationNumber The borrowerComunicationNumber to set.
     * @exception
     * @since 1.0
     */
    public void setBorrowerComunicationNumber(String borrowerComunicationNumber) {
        this.borrowerComunicationNumber = borrowerComunicationNumber;
    }
    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public BorrowerComKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(BorrowerComKey key) {
        this.key = key;
    }
    /**
     * @return Returns the lockingVersionUpdateNumber.
     * @exception
     * @since 1.0
     */
    public short getLockingVersionUpdateNumber() {
        return lockingVersionUpdateNumber;
    }
    /**
     * @param lockingVersionUpdateNumber The lockingVersionUpdateNumber to set.
     * @exception
     * @since 1.0
     */
    public void setLockingVersionUpdateNumber(short lockingVersionUpdateNumber) {
        this.lockingVersionUpdateNumber = lockingVersionUpdateNumber;
    }
    /**
     * @return Returns the personalComunicationMediumTypeCode.
     * @exception
     * @since 1.0
     */
    public short getPersonalComunicationMediumTypeCode() {
        return personalComunicationMediumTypeCode;
    }
    /**
     * @param personalComunicationMediumTypeCode The personalComunicationMediumTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPersonalComunicationMediumTypeCode(
            short personalComunicationMediumTypeCode) {
        this.personalComunicationMediumTypeCode = personalComunicationMediumTypeCode;
    }
    /**
     * @return Returns the personalComunicationTypeCode.
     * @exception
     * @since 1.0
     */
    public short getPersonalComunicationTypeCode() {
        return personalComunicationTypeCode;
    }
    /**
     * @param personalComunicationTypeCode The personalComunicationTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPersonalComunicationTypeCode(
            short personalComunicationTypeCode) {
        this.personalComunicationTypeCode = personalComunicationTypeCode;
    }
    /**
     * @return Returns the preferenceComunicationTypeCode.
     * @exception
     * @since 1.0
     */
    public Short getPreferenceComunicationTypeCode() {
        return preferenceComunicationTypeCode;
    }
    /**
     * @param preferenceComunicationTypeCode The preferenceComunicationTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPreferenceComunicationTypeCode(
            Short preferenceComunicationTypeCode) {
        this.preferenceComunicationTypeCode = preferenceComunicationTypeCode;
    }
}
