package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.EnumerationConfigurationException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SRL_ENUM implements Persistence, Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private int predictionPatternNumber;
  private int sequenceNumber;
  private char continuityType = 'c';
  private int startValue = 1;
  private Date startPublicationDate = new Date();
  private int unitCount = 12;
  private PersistenceState persistenceState = new PersistenceState();

  /**
   * @return the predictionPatternNumber
   */
  public int getPredictionPatternNumber() {
    return predictionPatternNumber;
  }

  /**
   * @param predictionPatternNumber the predictionPatternNumber to set
   */
  public void setPredictionPatternNumber(int predictionPatternNumber) {
    this.predictionPatternNumber = predictionPatternNumber;
  }

  /**
   * @return the sequenceNumber
   */
  public int getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * @param sequenceNumber the sequenceNumber to set
   */
  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * @return the startValue
   */
  public int getStartValue() {
    return startValue;
  }

  /**
   * @param startValue the startValue to set
   */
  public void setStartValue(int startValue) {
    this.startValue = startValue;
  }

  /**
   * @return the startPublicationDate
   */
  public Date getStartPublicationDate() {
    return startPublicationDate;
  }

  /**
   * @param startPublicationDate the startPublicationDate to set
   */
  public void setStartPublicationDate(Date startPublicationDate) {
    this.startPublicationDate = startPublicationDate;
  }

  public String getStartPublicationDateAsString() {
    try {
      return new SimpleDateFormat("dd/MM/yyyy").format(getStartPublicationDate());
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * @return the unitCount
   */
  public int getUnitCount() {
    return unitCount;
  }

  /**
   * @param unitCount the unitCount to set
   */
  public void setUnitCount(int unitCount) {
    this.unitCount = unitCount;
  }

  /**
   * @see PersistenceState#cancelChanges()
   */
  public void cancelChanges() {
    persistenceState.cancelChanges();
  }

  /**
   * @see PersistenceState#confirmChanges()
   */
  public void confirmChanges() {
    persistenceState.confirmChanges();
  }

  /**
   * @param arg0
   * @return
   * @see Object#equals(Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof SRL_ENUM) {
      SRL_ENUM o = (SRL_ENUM) arg0;
      return o.getPredictionPatternNumber() == this.getPredictionPatternNumber() &&
        o.getSequenceNumber() == this.getSequenceNumber();
    } else {
      return false;
    }
  }

  /**
   * @param obj
   * @throws DataAccessException
   * @see PersistenceState#evict(Object)
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  /**
   * @return
   * @see PersistenceState#getDAO()
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  /**
   * @return
   * @see PersistenceState#getUpdateStatus()
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * @param i
   * @see PersistenceState#setUpdateStatus(int)
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * @return
   * @see Object#hashCode()
   */
  public int hashCode() {
    return getPredictionPatternNumber() * getSequenceNumber();
  }

  /**
   * @return
   * @see PersistenceState#isChanged()
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * @return
   * @see PersistenceState#isDeleted()
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * @return
   * @see PersistenceState#isNew()
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * @return
   * @see PersistenceState#isRemoved()
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * @see PersistenceState#markChanged()
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * @see PersistenceState#markDeleted()
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * @see PersistenceState#markNew()
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * @see PersistenceState#markUnchanged()
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onDelete(Session)
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   * @see PersistenceState#onLoad(Session, Serializable)
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onSave(Session)
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onUpdate(Session)
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

  /**
   * @return
   * @see PersistenceState#toString()
   */
  public String toString() {
    return persistenceState.toString();
  }

  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }

  public void generateNewKey() throws DataAccessException {


  }

  /**
   * @return the continuityType
   */
  public char getContinuityType() {
    return continuityType;
  }

  /**
   * @param continuityType the continuityType to set
   */
  public void setContinuityType(char continuityType) {
    this.continuityType = continuityType;
  }

  /**
   * Validates that the state of this object is suitable for saving to the
   * database (mandatory fields are present for the given continuityType)
   *
   * @throws EnumerationConfigurationException
   */
  public void validate() throws EnumerationConfigurationException {
    switch (getContinuityType()) {
      case 'c':
        if (getStartValue() <= 0) {
          throw new EnumerationConfigurationException();
        }
        if (getStartPublicationDate() == null) {
          throw new EnumerationConfigurationException();
        }
        break;
      case 'r':
        if (getUnitCount() <= 0) {
          throw new EnumerationConfigurationException();
        }
        break;
      default:
        throw new EnumerationConfigurationException();
    }
  }

}
