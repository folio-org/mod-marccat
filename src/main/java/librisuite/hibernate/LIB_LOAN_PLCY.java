/*
 * (c) LibriCore
 * 
 * Created on 02-nov-2004
 * 
 * LIB_LOAN_PLCY.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LIB_LOAN_PLCY implements Serializable{
    
    //TODO cambiar quality por quantity
	private int libraryLoanPolicyNumber;
	private int organisationNumber;
	private char loanPeriodCode;
	private short borrowerTypeCode;
	private short libraryLoanTypeCode; /* TODO set default */
	private short normalLoanPeriodQuality;
	private char normalLoanPeriodUnit;
	private short onDemandLoanPeriodQuality;
	private char onDemandLoanPeriodUnit;
	private short hourlyLoanPeriodQuality;
	private char hourlyLoanPeriodUnit;
	private short overnightTimeThresholdQuality; 
	private char overnightTimeThresholdUnit;
	private short noOvernightRefusalTimeQuality;
	private char noOvernightRefusalTimeUnit;
	private Date fixLoanDate;
	private short extendedLoanMinimunGuaranteeQuality;
	private char extendedLoanMinimunGaranteeUnit;
	private short maximunRenewalCount;
	private int loanMaxCount;
	private char generalIndicator;

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getBorrowerTypeCode() {
		return borrowerTypeCode;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getExtendedLoanMinimunGaranteeUnit() {
		return extendedLoanMinimunGaranteeUnit;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getExtendedLoanMinimunGuaranteeQuality() {
		return extendedLoanMinimunGuaranteeQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getFixLoanDate() {
		return fixLoanDate;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getHourlyLoanPeriodQuality() {
		return hourlyLoanPeriodQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getHourlyLoanPeriodUnit() {
		return hourlyLoanPeriodUnit;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public int getLibraryLoanPolicyNumber() {
		return libraryLoanPolicyNumber;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getLibraryLoanTypeCode() {
		return libraryLoanTypeCode;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getLoanPeriodCode() {
		return loanPeriodCode;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getMaximunRenewalCount() {
		return maximunRenewalCount;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getNoOvernightRefusalTimeQuality() {
		return noOvernightRefusalTimeQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getNoOvernightRefusalTimeUnit() {
		return noOvernightRefusalTimeUnit;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getNormalLoanPeriodQuality() {
		return normalLoanPeriodQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getNormalLoanPeriodUnit() {
		return normalLoanPeriodUnit;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getOnDemandLoanPeriodQuality() {
		return onDemandLoanPeriodQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getOnDemandLoanPeriodUnit() {
		return onDemandLoanPeriodUnit;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public int getOrganisationNumber() {
		return organisationNumber;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public short getOvernightTimeThresholdQuality() {
		return overnightTimeThresholdQuality;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public char getOvernightTimeThresholdUnit() {
		return overnightTimeThresholdUnit;
	}

	/**
	 * 
	 * 
	 * @param s 
	 * @exception
	 * @since 1.0
	 */
	public void setBorrowerTypeCode(short s) {
		borrowerTypeCode = s;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setExtendedLoanMinimunGaranteeUnit(char character) {
		extendedLoanMinimunGaranteeUnit = character;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setExtendedLoanMinimunGuaranteeQuality(short short1) {
		extendedLoanMinimunGuaranteeQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setFixLoanDate(Date date) {
		fixLoanDate = date;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setHourlyLoanPeriodQuality(short short1) {
		hourlyLoanPeriodQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setHourlyLoanPeriodUnit(char character) {
		hourlyLoanPeriodUnit = character;
	}

	/**
	 * 
	 * 
	 * @param i 
	 * @exception
	 * @since 1.0
	 */
	public void setLibraryLoanPolicyNumber(int i) {
		libraryLoanPolicyNumber = i;
	}

	/**
	 * 
	 * 
	 * @param s 
	 * @exception
	 * @since 1.0
	 */
	public void setLibraryLoanTypeCode(short s) {
		libraryLoanTypeCode = s;
	}

	/**
	 * 
	 * 
	 * @param c 
	 * @exception
	 * @since 1.0
	 */
	public void setLoanPeriodCode(char c) {
		loanPeriodCode = c;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setMaximunRenewalCount(short short1) {
		maximunRenewalCount = short1;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setNoOvernightRefusalTimeQuality(short short1) {
		noOvernightRefusalTimeQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setNoOvernightRefusalTimeUnit(char character) {
		noOvernightRefusalTimeUnit = character;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setNormalLoanPeriodQuality(short short1) {
		normalLoanPeriodQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setNormalLoanPeriodUnit(char character) {
		normalLoanPeriodUnit = character;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setOnDemandLoanPeriodQuality(short short1) {
		onDemandLoanPeriodQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setOnDemandLoanPeriodUnit(char character) {
		onDemandLoanPeriodUnit = character;
	}

	/**
	 * 
	 * 
	 * @param i 
	 * @exception
	 * @since 1.0
	 */
	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

	/**
	 * 
	 * 
	 * @param short1 
	 * @exception
	 * @since 1.0
	 */
	public void setOvernightTimeThresholdQuality(short short1) {
		overnightTimeThresholdQuality = short1;
	}

	/**
	 * 
	 * 
	 * @param character 
	 * @exception
	 * @since 1.0
	 */
	public void setOvernightTimeThresholdUnit(char character) {
		overnightTimeThresholdUnit = character;
	}
	
	

    /**
     * @return Returns the libraryLoanMaxLoanCount.
     * @exception
     * @since 1.0
     */
    public int getLibraryLoanMaxLoanCount() {
        return loanMaxCount;
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
     * @return Returns the loanMaxCount.
     * @exception
     * @since 1.0
     */
    public int getLoanMaxCount() {
        return loanMaxCount;
    }
    /**
     * @param loanMaxCount The loanMaxCount to set.
     * @exception
     * @since 1.0
     */
    public void setLoanMaxCount(int loanMaxCount) {
        this.loanMaxCount = loanMaxCount;
    }
}
