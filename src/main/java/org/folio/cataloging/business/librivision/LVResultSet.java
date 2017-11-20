/*
 * (c) LibriCore
 * 
 * Created on Jun 7, 2004
 * 
 * LVResultSet.java
 */
package org.folio.cataloging.business.librivision;

import org.folio.cataloging.business.searching.ResultSet;
import org.folio.cataloging.business.searching.SearchEngine;

/**
 * This class contains a result set from a search
 * 
 * @author Wim Crols
 * @version $Revision: 1.3 $, $Date: 2006/07/28 14:16:54 $
 * @since 1.0
 */
public class LVResultSet extends ResultSet {

	private String lvSessionId = null;

	private String id = null;

	private String databaseId = "";

	public LVResultSet(
		SearchEngine searchEngine,
		String lvSessionId,
		String id,
		int size,
		String databaseId) {
		setSearchEngine(searchEngine);
		this.lvSessionId = new String(lvSessionId);
		this.id = new String(id);
		this.record = new Record[size];
		this.databaseId = databaseId;
	}

	/**
	 * Getter for Id
	 * 
	 * @return id
	 * @since 1.0
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Setter for Id
	 * 
	 * @param Id
	 *            Id
	 * @since 1.0
	 */
	public void setId(String id) {
		if (id != null) {
			this.id = new String(id);
		}
	}

	/**
	 * @since 1.0
	 */
	public String getDatabaseId() {
		return this.databaseId;
	}

	/**
	 * @since 1.0
	 */
	public void setDatabaseId(String databaseId) {
		if ((this.databaseId == null) && (databaseId != null)) {
			this.databaseId = new String(databaseId);
		}
	}

	/**
	 * Getter for lvSessionId
	 * 
	 * @return lvSessionId
	 * @since 1.0
	 */
	public String getLvSessionId() {
		return lvSessionId;
	}

	/**
	 * Setter for lvSessionId
	 * 
	 * @param string
	 *            lvSessionId
	 * @since 1.0
	 */
	public void setLvSessionId(String string) {
		lvSessionId = string;
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.ResultSet#getAmicusNumber(int)
	 */
	public Integer getAmicusNumber(int i) {
		/*
		 * Librivision result sets do not have Amicus numbers.
		 * In z3950 records are identified by sequence numbers within
		 * result sets.
		 */
		return new Integer(0);
	}

}