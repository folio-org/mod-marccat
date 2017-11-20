/*
 * (c) LibriCore
 * 
 * Created on Dec 9, 2004
 * 
 * LDG_STATS.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class LDG_STATS implements Serializable {
	private int loadingStatisticsNumber;
	private int recordsAdded;
	private int recordsRejected;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public LDG_STATS() {
		super();
		// TODO Auto-generated constructor stub
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
	public int getRecordsAdded() {
		return recordsAdded;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getRecordsRejected() {
		return recordsRejected;
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
	public void setRecordsAdded(int i) {
		recordsAdded = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRecordsRejected(int i) {
		recordsRejected = i;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof LDG_STATS) {
			LDG_STATS l = (LDG_STATS)arg0;
			return l.getLoadingStatisticsNumber() == this.getLoadingStatisticsNumber();
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getLoadingStatisticsNumber();
	}

}
