package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CLCTN_MST_ACS_PNT implements Persistence {

  private int collectionNumber;
  private int bibItemNumber;
  private Date creationDate;
  private Date transactionDate;

  private PersistenceState persistentState = new PersistenceState();

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CLCTN_MST_ACS_PNT() {
    super();
  }

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CLCTN_MST_ACS_PNT(int itemNumber, int collectionNumber, Date creationDate, Date transactionDate) {
    super();
    this.bibItemNumber = itemNumber;
    this.collectionNumber = collectionNumber;
    this.creationDate = creationDate;
    this.transactionDate = transactionDate;
  }


  /**
   * @since 1.0
   */
  public int getBibItemNumber() {
    return bibItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setBibItemNumber(int bibNumber) {
    bibItemNumber = bibNumber;
  }

  /**
   * @since 1.0
   */
  public int getCollectionNumber() {
    return collectionNumber;
  }

  /**
   * @since 1.0
   */
  public void setCollectionNumber(int collId) {
    collectionNumber = collId;
  }

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistentState.evict(obj);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return persistentState.getDAO();
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistentState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistentState.setUpdateStatus(i);
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistentState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistentState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistentState.isNew();
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistentState.isRemoved();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistentState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistentState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistentState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistentState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistentState.onDelete(arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistentState.onLoad(arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistentState.onSave(arg0);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistentState.onUpdate(arg0);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#evict()
   */
  public void evict() throws DataAccessException {
    evict(this);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof CLCTN_MST_ACS_PNT) {
      CLCTN_MST_ACS_PNT c = (CLCTN_MST_ACS_PNT) arg0;
      return this.getBibItemNumber() == c.getBibItemNumber() &&
        this.getCollectionNumber() == c.collectionNumber;
    } else {
      return false;
    }
  }


  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getBibItemNumber() + getCollectionNumber();
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreationDateString() {
    if (getCreationDate() != null) {
      DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
      return formatter.format(getCreationDate());
    } else {
      return "";
    }
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getTransactionDateString() {
    if (getTransactionDate() != null) {
      DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
      return formatter.format(getTransactionDate());
    } else {
      return "";
    }
  }

}


