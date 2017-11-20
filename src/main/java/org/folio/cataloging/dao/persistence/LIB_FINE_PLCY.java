/*
 * (c) LibriCore
 * 
 * Created on 04-mar-2005
 * 
 * LIB_FINE_PLCY.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LIB_FINE_PLCY implements Serializable{
	private int libraryFinePolicyNumber;
	private int organisationNumber;
	private char loanPeriodCode;
	private short borrowerTypeCode;
	private int fineGracePeriodTime;
	private int finePeriodTime;
	private float fineAmount;
	private float minFineAmount;
	private float maxFineAmount;
	private int fineUnitDuration;
	private int minFinePeriod;
	private int maxFinePeriod;
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
     * @return Returns the fineAmount.
     * @exception
     * @since 1.0
     */
    public float getFineAmount() {
        return fineAmount;
    }
    /**
     * @param fineAmount The fineAmount to set.
     * @exception
     * @since 1.0
     */
    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }
    /**
     * @return Returns the fineGracePeriodTime.
     * @exception
     * @since 1.0
     */
    public int getFineGracePeriodTime() {
        return fineGracePeriodTime;
    }
    /**
     * @param fineGracePeriodTime The fineGracePeriodTime to set.
     * @exception
     * @since 1.0
     */
    public void setFineGracePeriodTime(int fineGracePeriodTime) {
        this.fineGracePeriodTime = fineGracePeriodTime;
    }
    /**
     * @return Returns the finePeriodTime.
     * @exception
     * @since 1.0
     */
    public int getFinePeriodTime() {
        return finePeriodTime;
    }
    /**
     * @param finePeriodTime The finePeriodTime to set.
     * @exception
     * @since 1.0
     */
    public void setFinePeriodTime(int finePeriodTime) {
        this.finePeriodTime = finePeriodTime;
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
     * @return Returns the libraryFinePolicyNumber.
     * @exception
     * @since 1.0
     */
    public int getLibraryFinePolicyNumber() {
        return libraryFinePolicyNumber;
    }
    /**
     * @param libraryFinePolicyNumber The libraryFinePolicyNumber to set.
     * @exception
     * @since 1.0
     */
    public void setLibraryFinePolicyNumber(int libraryFinePolicyNumber) {
        this.libraryFinePolicyNumber = libraryFinePolicyNumber;
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
     * @return Returns the maxFineAmount.
     * @exception
     * @since 1.0
     */
    public float getMaxFineAmount() {
        return maxFineAmount;
    }
    /**
     * @param maxFineAmount The maxFineAmount to set.
     * @exception
     * @since 1.0
     */
    public void setMaxFineAmount(float maxFineAmount) {
        this.maxFineAmount = maxFineAmount;
    }
    /**
     * @return Returns the minFineAmount.
     * @exception
     * @since 1.0
     */
    public float getMinFineAmount() {
        return minFineAmount;
    }
    /**
     * @param minFineAmount The minFineAmount to set.
     * @exception
     * @since 1.0
     */
    public void setMinFineAmount(float minFineAmount) {
        this.minFineAmount = minFineAmount;
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
     * @return Returns the fineUnitDuration.
     * @exception
     * @since 1.0
     */
    public int getFineUnitDuration() {
        return fineUnitDuration;
    }
    /**
     * @param fineUnitDuration The fineUnitDuration to set.
     * @exception
     * @since 1.0
     */
    public void setFineUnitDuration(int fineUnitDuration) {
        this.fineUnitDuration = fineUnitDuration;
    }
    /**
     * @return Returns the maxFinePeriod.
     * @exception
     * @since 1.0
     */
    public int getMaxFinePeriod() {
        return maxFinePeriod;
    }
    /**
     * @param maxFinePeriod The maxFinePeriod to set.
     * @exception
     * @since 1.0
     */
    public void setMaxFinePeriod(int maxFinePeriod) {
        this.maxFinePeriod = maxFinePeriod;
    }
    /**
     * @return Returns the minFinePeriod.
     * @exception
     * @since 1.0
     */
    public int getMinFinePeriod() {
        return minFinePeriod;
    }
    /**
     * @param minFinePeriod The minFinePeriod to set.
     * @exception
     * @since 1.0
     */
    public void setMinFinePeriod(int minFinePeriod) {
        this.minFinePeriod = minFinePeriod;
    }
    
    
}
