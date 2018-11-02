/*
 * (c) LibriCore
 *
 * Created on 21-jun-2004
 *
 * SMRY_HLDG.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOLibrary;

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
   * @param i bibItemNumber
   */
  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  /**
   * @return creationDate
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * @param date creationDate
   */
  public void setCreationDate(Date date) {
    creationDate = date;
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
   * @param s holdingFirstIndexCode
   */
  public void setHoldingFirstIndexCode(short s) {
    holdingFirstIndexCode = s;
  }

  /**
   * @return holdingSecondIndexCode
   */
  public short getHoldingSecondIndexCode() {
    return holdingSecondIndexCode;
  }

  /**
   * @param s holdingSecondIndexCode
   */
  public void setHoldingSecondIndexCode(short s) {
    holdingSecondIndexCode = s;
  }

  /**
   * @return holdingStatementText
   */
  public String getHoldingStatementText() {
    return holdingStatementText;
  }

  /**
   * @param string holdingStatementText
   */
  public void setHoldingStatementText(String string) {
    holdingStatementText = string;
  }

  /**
   * @return holdingStatusCode
   */
  public char getHoldingStatusCode() {
    return holdingStatusCode;
  }

  /**
   * @param c holdingStatusCode
   */
  public void setHoldingStatusCode(char c) {
    holdingStatusCode = c;
  }

  /**
   * @return librarySymbolCode
   */
  public String getLibrarySymbolCode() {
    return librarySymbolCode;
  }

  /**
   * @param string librarySymbolCode
   */
  public void setLibrarySymbolCode(String string) {
    librarySymbolCode = string;
  }

  /**
   * @return mainLibraryNumber
   */
  public int getMainLibraryNumber() {
    return mainLibraryNumber;
  }

  /**
   * @param i mainLibraryNumber
   */
  public void setMainLibraryNumber(int i) {
    mainLibraryNumber = i;
  }

  /**
   * @return transactionDate
   */
  public Date getTransactionDate() {
    return transactionDate;
  }

  /**
   * @param date transactionDate
   */
  public void setTransactionDate(Date date) {
    transactionDate = date;
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
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

}
