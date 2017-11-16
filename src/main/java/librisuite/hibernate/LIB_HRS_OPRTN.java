/*
 * (c) LibriCore
 * 
 * Created on 02-nov-2004
 * 
 * LIB_HRS_OPRTN.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LIB_HRS_OPRTN implements Serializable{
	private int organisationNumber;
	private Date mondayHoursOpeningTime;
	private Date mondayHoursClosingTime;
	private Date tuesdayHoursOpeningTime;
	private Date tuesdayHoursClosingTime;
	private Date wednesdayHoursOpeningTime;
	private Date wednesdayHoursClosingTime;
	private Date thursdayHoursOpeningTime;
	private Date thursdayHoursClosingTime;
	private Date fridayHoursOpeningTime;
	private Date fridayHoursClosingTime; 
	private Date saturdayHoursOpeningTime;
	private Date saturdayHoursClosingTime;
	private Date sundayHoursOpeningTime;
	private Date sundayHoursClosingTime;
	private char generalIndicator;
	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getFridayHoursClosingTime() {
		return fridayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getFridayHoursOpeningTime() {
		return fridayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getMondayHoursClosingTime() {
		return mondayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getMondayHoursOpeningTime() {
		return mondayHoursOpeningTime;
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
	public Date getSaturdayHoursClosingTime() {
		return saturdayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getSaturdayHoursOpeningTime() {
		return saturdayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getSundayHoursClosingTime() {
		return sundayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getSundayHoursOpeningTime() {
		return sundayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getThursdayHoursClosingTime() {
		return thursdayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getThursdayHoursOpeningTime() {
		return thursdayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getTuesdayHoursClosingTime() {
		return tuesdayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getTuesdayHoursOpeningTime() {
		return tuesdayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getWednesdayHoursClosingTime() {
		return wednesdayHoursClosingTime;
	}

	/**
	 * 
	 * 
	 * @return 
	 * @exception
	 * @since 1.0
	 */
	public Date getWednesdayHoursOpeningTime() {
		return wednesdayHoursOpeningTime;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setFridayHoursClosingTime(Date date) {
		fridayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setFridayHoursOpeningTime(Date date) {
		fridayHoursOpeningTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setMondayHoursClosingTime(Date date) {
		mondayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setMondayHoursOpeningTime(Date date) {
		mondayHoursOpeningTime = date;
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
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setSaturdayHoursClosingTime(Date date) {
		saturdayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setSaturdayHoursOpeningTime(Date date) {
		saturdayHoursOpeningTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setSundayHoursClosingTime(Date date) {
		sundayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setSundayHoursOpeningTime(Date date) {
		sundayHoursOpeningTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setThursdayHoursClosingTime(Date date) {
		thursdayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setThursdayHoursOpeningTime(Date date) {
		thursdayHoursOpeningTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setTuesdayHoursClosingTime(Date date) {
		tuesdayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setTuesdayHoursOpeningTime(Date date) {
		tuesdayHoursOpeningTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setWednesdayHoursClosingTime(Date date) {
		wednesdayHoursClosingTime = date;
	}

	/**
	 * 
	 * 
	 * @param date 
	 * @exception
	 * @since 1.0
	 */
	public void setWednesdayHoursOpeningTime(Date date) {
		wednesdayHoursOpeningTime = date;
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
}
