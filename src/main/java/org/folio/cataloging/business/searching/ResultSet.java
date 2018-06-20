package org.folio.cataloging.business.searching;

import net.sf.hibernate.Session;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.persistence.T_AUT_DSPLY_FRMT;
import org.folio.cataloging.dao.persistence.T_BIB_DSPLY_FRMT;
import org.folio.cataloging.dao.persistence.T_ITM_DSPLY_FRMT;
import org.folio.cataloging.exception.ModCatalogingException;

import java.util.List;
import java.util.Locale;

public abstract class ResultSet  {
	private String complexQuery;

	public String getComplexQuery() {
		return complexQuery;
	}

	public void setComplexQuery(String complexQuery) {
		this.complexQuery = complexQuery;
	}
	
	protected static final int RETRIEVE_MAX_NUMBER_OF_RECORDS = 10;
	abstract public Integer getAmicusNumber(int i);
	protected Record[] record = null;
	protected String displayQuery = "";
	private int searchingView;
	private SearchEngine searchEngine;
	protected int sortCriteria = 0;

	/**
	 * @return true if the current searchingView is BIBLIOGRAPHICS
	 */
	public boolean isBibliographic() {
		// MIKE I need to consider archives too
		return getSearchingView() != View.AUTHORITY;
	}
	
	/**
	 * @return true if the current searchingView is ARCHIVES
	 */
	public boolean isArchive() {
		return getSearchingView() < View.AUTHORITY;
	}

	public int getSize() {
		return this.record.length;
	}

	public void setSize(int size) {
		if ((this.record == null) && (size > 0)) {
			this.record = new Record[size];
		}
	}

	public Record[] getRecord() {
		return this.record;
	}

	public Record getRecord(int recordNumber, String elementSetName) 
	{
		if ((recordNumber < this.record.length) && (recordNumber >= 0)) {
			if ((this.record[recordNumber] == null)
				|| (!this.record[recordNumber].hasContent(elementSetName))) {
				retrieveRecords(recordNumber, elementSetName);
			}
			return this.record[recordNumber];
		} else {
			return null;
		}
	}

	public void setRecord(int recordNumber, Record aRecord) 
	{
		if ((this.record != null) && (recordNumber < this.record.length)) {
			this.record[recordNumber] = aRecord;
		}
	}

	protected void retrieveRecords(int recordNumber, String elementSetName) 
	{
		int firstRecordNumber = recordNumber;
		int lastRecordNumber = recordNumber;

		if ((recordNumber < this.record.length)
			&& (recordNumber >= 0)
			&& ((this.record[recordNumber] == null)
				|| (!this.record[recordNumber].hasContent(elementSetName)))) {
			lastRecordNumber =
				computeLastRecordNumber(
					firstRecordNumber + 1,
					elementSetName,
					RETRIEVE_MAX_NUMBER_OF_RECORDS);
			firstRecordNumber =
				computeFirstRecordNumber(
					lastRecordNumber,
					elementSetName,
					RETRIEVE_MAX_NUMBER_OF_RECORDS);
		}

		searchEngine.fetchRecords(
			this,
			elementSetName,
			firstRecordNumber,
			lastRecordNumber);
	}

	protected int computeLastRecordNumber(int firstRecordNumber, String elementSetName,	int maxNumberOfRecords) 
	{
		int lastRecordNumber = firstRecordNumber;
		while (((lastRecordNumber - firstRecordNumber)
			< (maxNumberOfRecords - 1))
			&& (lastRecordNumber < getSize())
			&& ((this.record[lastRecordNumber] == null)
				|| (!this.record[lastRecordNumber].hasContent(elementSetName)))) {
			lastRecordNumber++;
		}
		return lastRecordNumber;
	}

	protected int computeFirstRecordNumber(int lastRecordNumber, String elementSetName, int maxNumberOfRecords) 
	{
		int firstRecordNumber = lastRecordNumber;
		while (((lastRecordNumber - firstRecordNumber)
			< (maxNumberOfRecords - 1))
			&& (firstRecordNumber > 1)
			&& ((this.record[firstRecordNumber - 2] == null)
				|| (!this.record[firstRecordNumber
					- 2].hasContent(elementSetName)))) {
			firstRecordNumber--;
		}
		return firstRecordNumber;
	}

	public String getDisplayQuery() {
		return displayQuery;
	}

	public void setDisplayQuery(String string) {
		displayQuery = string;
	}

	public void clearRecords() 
	{
		for (int klm = 0; klm < record.length; klm++) {
			record[klm] = null;
		}
	}

	public int getSortCriteria() {
		return sortCriteria;
	}

	public void setSortCriteria(int i) {
		sortCriteria = i;
	}

	public int getSearchingView() {
		return searchingView;
	}

	public void setSearchingView(int i) {
		searchingView = i;
	}

	public void sort(String[] attributes, String[] directions)
		throws ModCatalogingException {
		searchEngine.sort(this, attributes, directions);
	}

	public SearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(SearchEngine engine) {
		searchEngine = engine;
	}

	public List getDisplayFormatList(Locale locale) 
	{
		if (isBibliographic() || isArchive()) {
			return CodeListsBean.getBibliographicDisplayFormat().getCodeList(locale);
		} else {
			return CodeListsBean.getAuthorityDisplayFormat().getCodeList(locale);
		}
	}

	public T_ITM_DSPLY_FRMT getDisplayFormat(final Session session, final short code, final Locale locale)	throws DataAccessException
	{
		if (isBibliographic() || isArchive()) {		
			return (T_BIB_DSPLY_FRMT) new DAOCodeTable().load(session, T_BIB_DSPLY_FRMT.class, code, locale);
		} else {
			return (T_AUT_DSPLY_FRMT) new DAOCodeTable().load(session, T_AUT_DSPLY_FRMT.class, code,	locale);
		}
	}
}
