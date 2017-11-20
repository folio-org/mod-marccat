/*
 * (c) LibriCore
 * 
 * Created on 22-mar-2005
 * 
 * LOAN_IN_TRNST_PLCY.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

 /**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LOAN_IN_TRNST_PLCY implements Serializable{

	private LoanInTransitPolicyKey key;
	private short checkInIndicator;
	private short chargeOutIndicator;
	private short renewalIndicator;
	private short finesIndicator;


    /**
     * @return Returns the chargeOutIndicator.
     * @exception
     * @since 1.0
     */
    public short getChargeOutIndicator() {
        return chargeOutIndicator;
    }
    /**
     * @param chargeOutIndicator The chargeOutIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setChargeOutIndicator(short chargeOutIndicator) {
        this.chargeOutIndicator = chargeOutIndicator;
    }
    /**
     * @return Returns the checkInIndicator.
     * @exception
     * @since 1.0
     */
    public short getCheckInIndicator() {
        return checkInIndicator;
    }
    /**
     * @param checkInIndicator The checkInIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setCheckInIndicator(short checkInIndicator) {
        this.checkInIndicator = checkInIndicator;
    }
    /**
     * @return Returns the finesIndicator.
     * @exception
     * @since 1.0
     */
    public short getFinesIndicator() {
        return finesIndicator;
    }
    /**
     * @param finesIndicator The finesIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setFinesIndicator(short finesIndicator) {
        this.finesIndicator = finesIndicator;
    }
    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public LoanInTransitPolicyKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(LoanInTransitPolicyKey key) {
        this.key = key;
    }
    /**
     * @return Returns the renewalIndicator.
     * @exception
     * @since 1.0
     */
    public short getRenewalIndicator() {
        return renewalIndicator;
    }
    /**
     * @param renewalIndicator The renewalIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setRenewalIndicator(short renewalIndicator) {
        this.renewalIndicator = renewalIndicator;
    }
}
