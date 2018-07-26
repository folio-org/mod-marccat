/*
 * (c) LibriCore
 * 
 * Created on 21-jun-2004
 * 
 * SMRY_HLDG.java
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOLibrary;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Usuario
 * @version $Revision: 1.4 $, $Date: 2007/04/09 09:58:07 $
 * @since 1.0
 */
public class SMRY_HLDG implements Persistence, Serializable {
	private int bibItemNumber;

	private int mainLibraryNumber;

	private short holdingFirstIndexCode = 5;

	private short holdingSecondIndexCode = 5;

	private String librarySymbolCode;

	/*modifica barbara 24/04/2007 il valore deve essere inizializzato a 0*/
	private char holdingStatusCode = '0';

	private Date creationDate = new Date();

	private Date transactionDate = creationDate;

	private String holdingStatementText;

	private PersistenceState persistenceState = new PersistenceState();

	public SMRY_HLDG() {
	}
	
	public SMRY_HLDG(CPY_ID copy) {
		setBibItemNumber(copy.getBibItemNumber());
		setMainLibraryNumber(copy.getOrganisationNumber());
		try {
			setLibrarySymbolCode(new DAOLibrary().getLibrarySymbol(copy.getOrganisationNumber()));
		} catch (DataAccessException e) {
			// leave symbol null when data access exception
		}
		setHoldingStatementText(copy.getCopyStatementText());
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof SMRY_HLDG) {
			SMRY_HLDG aHldg = (SMRY_HLDG) obj;
			return this.getBibItemNumber() == aHldg.getBibItemNumber()
					&& this.getMainLibraryNumber() == aHldg
							.getMainLibraryNumber();
		}
		return false;
	}

	public void evict() throws DataAccessException {
		evict(this);
	}

	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void generateNewKey() throws DataAccessException {
		// do nothing
	}

	/**
	 * @return bibItemNumber
	 */
	public int getBibItemNumber() {
		return bibItemNumber;
	}

	/**
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	public AbstractDAO getDAO() {
		return persistenceState.getDAO();
	}

	/**
	 * @return holdingFirstIndexCode
	 */
	public short getHoldingFirstIndexCode() {
		return holdingFirstIndexCode;
	}

	/**
	 * @return holdingSecondIndexCode
	 */
	public short getHoldingSecondIndexCode() {
		return holdingSecondIndexCode;
	}

	/**
	 * @return holdingStatementText
	 */
	public String getHoldingStatementText() {
		return holdingStatementText;
	}

	/**
	 * @return holdingStatusCode
	 */
	public char getHoldingStatusCode() {
		return holdingStatusCode;
	}

	/**
	 * @return librarySymbolCode
	 */
	public String getLibrarySymbolCode() {
		return librarySymbolCode;
	}

	/**
	 * @return mainLibraryNumber
	 */
	public int getMainLibraryNumber() {
		return mainLibraryNumber;
	}

	/**
	 * @return transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	public int hashCode() {
		return getBibItemNumber() + getMainLibraryNumber();
	}

	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	public boolean isNew() {
		return persistenceState.isNew();
	}

	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	public void markChanged() {
		persistenceState.markChanged();
	}

	public void markDeleted() {
		persistenceState.markDeleted();
	}

	public void markNew() {
		persistenceState.markNew();
	}

	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * @param i
	 *            bibItemNumber
	 */
	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	/**
	 * @param date
	 *            creationDate
	 */
	public void setCreationDate(Date date) {
		creationDate = date;
	}

	/**
	 * @param s
	 *            holdingFirstIndexCode
	 */
	public void setHoldingFirstIndexCode(short s) {
		holdingFirstIndexCode = s;
	}

	/**
	 * @param s
	 *            holdingSecondIndexCode
	 */
	public void setHoldingSecondIndexCode(short s) {
		holdingSecondIndexCode = s;
	}

	/**
	 * @param string
	 *            holdingStatementText
	 */
	public void setHoldingStatementText(String string) {
		holdingStatementText = string;
	}

	/**
	 * @param c
	 *            holdingStatusCode
	 */
	public void setHoldingStatusCode(char c) {
		holdingStatusCode = c;
	}

	/**
	 * @param string
	 *            librarySymbolCode
	 */
	public void setLibrarySymbolCode(String string) {
		librarySymbolCode = string;
	}

	/**
	 * @param i
	 *            mainLibraryNumber
	 */
	public void setMainLibraryNumber(int i) {
		mainLibraryNumber = i;
	}

	/**
	 * @param date
	 *            transactionDate
	 */
	public void setTransactionDate(Date date) {
		transactionDate = date;
	}

	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

}
