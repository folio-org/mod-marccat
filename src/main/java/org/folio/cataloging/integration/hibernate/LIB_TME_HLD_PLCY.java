/*
 * (c) LibriCore
 * 
 * Created on 03-mar-2005
 * 
 * LIB_TME_HLD_PLCY.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LIB_TME_HLD_PLCY implements Serializable{
	private int libraryTimeHoldPolicyNumber;
	private int organisationNumber;
	private char loanPeriodCode;
	private short borrowerTypeCode;
	private int timeHoldSetupCounter;
	private int timeHoldIntermediateCounter;
	private char generalIndicator;
	
	
	

    /**
     * @return Returns the borrowerTypeCode.
     * @exception
     * @since 1.0
     */
    public short getBorrowerTypeCode() {
        return borrowerTypeCode;
    }
    /**
     * @param borrowerTypeCode The borrowerTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setBorrowerTypeCode(short borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }
    /**
     * @return Returns the generalIndicator.
     * @exception
     * @since 1.0
     */
    public char getGeneralIndicator() {
        return generalIndicator;
    }
    /**
     * @param generalIndicator The generalIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setGeneralIndicator(char generalIndicator) {
        this.generalIndicator = generalIndicator;
    }
    /**
     * @return Returns the libraryHoldPolicyNumber.
     * @exception
     * @since 1.0
     */
    public int getLibraryTimeHoldPolicyNumber() {
        return libraryTimeHoldPolicyNumber;
    }
    /**
     * @param libraryHoldPolicyNumber The libraryHoldPolicyNumber to set.
     * @exception
     * @since 1.0
     */
    public void setLibraryTimeHoldPolicyNumber(int libraryTimeHoldPolicyNumber) {
        this.libraryTimeHoldPolicyNumber = libraryTimeHoldPolicyNumber;
    }
    /**
     * @return Returns the loanPeriodCode.
     * @exception
     * @since 1.0
     */
    public char getLoanPeriodCode() {
        return loanPeriodCode;
    }
    /**
     * @param loanPeriodCode The loanPeriodCode to set.
     * @exception
     * @since 1.0
     */
    public void setLoanPeriodCode(char loanPeriodCode) {
        this.loanPeriodCode = loanPeriodCode;
    }
    /**
     * @return Returns the organisationNumber.
     * @exception
     * @since 1.0
     */
    public int getOrganisationNumber() {
        return organisationNumber;
    }
    /**
     * @param organisationNumber The organisationNumber to set.
     * @exception
     * @since 1.0
     */
    public void setOrganisationNumber(int organisationNumber) {
        this.organisationNumber = organisationNumber;
    }
    /**
     * @return Returns the timeHoldIntermediateCounter.
     * @exception
     * @since 1.0
     */
    public int getTimeHoldIntermediateCounter() {
        return timeHoldIntermediateCounter;
    }
    /**
     * @param timeHoldIntermediateCounter The timeHoldIntermediateCounter to set.
     * @exception
     * @since 1.0
     */
    public void setTimeHoldIntermediateCounter(int timeHoldIntermediateCounter) {
        this.timeHoldIntermediateCounter = timeHoldIntermediateCounter;
    }
    /**
     * @return Returns the timeHoldSetupCounter.
     * @exception
     * @since 1.0
     */
    public int getTimeHoldSetupCounter() {
        return timeHoldSetupCounter;
    }
    /**
     * @param timeHoldSetupCounter The timeHoldSetupCounter to set.
     * @exception
     * @since 1.0
     */
    public void setTimeHoldSetupCounter(int timeHoldSetupCounter) {
        this.timeHoldSetupCounter = timeHoldSetupCounter;
    }
}
