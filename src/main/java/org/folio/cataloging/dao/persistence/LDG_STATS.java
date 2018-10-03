package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.SystemNextNumberDAO;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistent object to import record from file.
 *
 * @author paulm
 * @since 1.0
 */
public class LDG_STATS implements Serializable, Persistence {

  private int modifiedRecords = 0;
  private int modifiedHoldingsCount = 0;
  private int newLocationsCount = 0;
  private int detatiledHoldingsCount = 0;
  private int errorCount = 0;
  private int processedCount = 0;
  private Date loadingDate = new Date();
  private char statusCode = 'c';
  private String retroCurrentCode = "C";
  private String librarySymbolCode = "DOBIS";
  private int inputSourceCode = 999;

  public void generateNewKey(final Session session) throws DataAccessException {
    try {
      setLoadingStatisticsNumber(new SystemNextNumberDAO().getNextNumber("E0", session));
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  private int loadingStatisticsNumber;
  private int recordsAdded;
  private int recordsRejected;
  private PersistenceState persistenceState = new PersistenceState();

  public void cancelChanges() {
    persistenceState.cancelChanges();
  }

  public void confirmChanges() {
    persistenceState.confirmChanges();
  }

  //todo
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public int getModifiedRecords() {
    return modifiedRecords;
  }

  public void setModifiedRecords(int modifiedRecords) {
    this.modifiedRecords = modifiedRecords;
  }

  public int getModifiedHoldingsCount() {
    return modifiedHoldingsCount;
  }

  public void setModifiedHoldingsCount(int modifiedHoldingsCount) {
    this.modifiedHoldingsCount = modifiedHoldingsCount;
  }

  public int getNewLocationsCount() {
    return newLocationsCount;
  }

  public void setNewLocationsCount(int newLocationsCount) {
    this.newLocationsCount = newLocationsCount;
  }

  public int getDetatiledHoldingsCount() {
    return detatiledHoldingsCount;
  }

  public void setDetatiledHoldingsCount(int detatiledHoldingsCount) {
    this.detatiledHoldingsCount = detatiledHoldingsCount;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(int errorCount) {
    this.errorCount = errorCount;
  }

  public int getProcessedCount() {
    return processedCount;
  }

  public void setProcessedCount(int processedCount) {
    this.processedCount = processedCount;
  }

  public Date getLoadingDate() {
    return loadingDate;
  }

  public void setLoadingDate(Date loadingDate) {
    this.loadingDate = loadingDate;
  }

  public char getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(char statusCode) {
    this.statusCode = statusCode;
  }

  public String getRetroCurrentCode() {
    return retroCurrentCode;
  }

  public void setRetroCurrentCode(String retroCurrentCode) {
    this.retroCurrentCode = retroCurrentCode;
  }

  public String getLibrarySymbolCode() {
    return librarySymbolCode;
  }

  public void setLibrarySymbolCode(String librarySymbolCode) {
    this.librarySymbolCode = librarySymbolCode;
  }

  public int getInputSourceCode() {
    return inputSourceCode;
  }

  public void setInputSourceCode(int inputSourceCode) {
    this.inputSourceCode = inputSourceCode;
  }

  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  public void setPersistenceState(PersistenceState persistenceState) {
    this.persistenceState = persistenceState;
  }

  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
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

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  public String toString() {
    return persistenceState.toString();
  }

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

  /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
  public boolean equals(Object arg0) {
    if (arg0 instanceof LDG_STATS) {
      LDG_STATS l = (LDG_STATS) arg0;
      return l.getLoadingStatisticsNumber() == this
        .getLoadingStatisticsNumber();
    } else {
      return false;
    }
  }

  /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
  public int hashCode() {
    return getLoadingStatisticsNumber();
  }

  @Override
  public void evict() throws DataAccessException {
    evict(this);
  }

}
