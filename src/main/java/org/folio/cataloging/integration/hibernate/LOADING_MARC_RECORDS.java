/*
 * (c) LibriCore
 * 
 * Created on Dec 9, 2004
 * 
 * LOADING_MARC_RECORDS.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class LOADING_MARC_RECORDS implements Serializable {
	private int sequence;
	private int loadingStatisticsNumber;
	private int oldBibItemNumber;
	private int BibItemNumber;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public LOADING_MARC_RECORDS() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return BibItemNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getLoadingStatisticsNumber() {
		return loadingStatisticsNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getOldBibItemNumber() {
		return oldBibItemNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		BibItemNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLoadingStatisticsNumber(int i) {
		loadingStatisticsNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOldBibItemNumber(int i) {
		oldBibItemNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSequence(int i) {
		sequence = i;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof LOADING_MARC_RECORDS) {
			LOADING_MARC_RECORDS l = (LOADING_MARC_RECORDS) arg0;
			return l.getLoadingStatisticsNumber()
				== this.getLoadingStatisticsNumber()
				&& l.getSequence() == this.getSequence();
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.getLoadingStatisticsNumber() + this.getSequence();
	}

}
