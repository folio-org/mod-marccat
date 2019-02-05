/*
 * (c) LibriCore
 *
 * Created on Jan 25, 2005
 *
 * ReceiveItem.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.util.Date;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class ReceiveItem implements Persistence {
  private final PersistenceState persistenceState = new PersistenceState();
  private int receiveItemNumber;
  private int orderNumber;
  private int orderItemNumber;
  private int packageItemNumber;
  private Date receivedDate;
  private int bibItemNumber;
  private String librarySymbolCode;
  private String locationText;
  private short receivedCopyQuantity;
  private String conditionNote;


  public ReceiveItem() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
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
    // TODO Auto-generated method stub

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
  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getConditionNote() {
    return conditionNote;
  }

  /**
   * @since 1.0
   */
  public void setConditionNote(String string) {
    conditionNote = string;
  }

  /**
   * @since 1.0
   */
  public String getLibrarySymbolCode() {
    return librarySymbolCode;
  }

  /**
   * @since 1.0
   */
  public void setLibrarySymbolCode(String string) {
    librarySymbolCode = string;
  }

  /**
   * @since 1.0
   */
  public String getLocationText() {
    return locationText;
  }

  /**
   * @since 1.0
   */
  public void setLocationText(String string) {
    locationText = string;
  }

  /**
   * @since 1.0
   */
  public int getOrderItemNumber() {
    return orderItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setOrderItemNumber(int i) {
    orderItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getOrderNumber() {
    return orderNumber;
  }

  /**
   * @since 1.0
   */
  public void setOrderNumber(int i) {
    orderNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getPackageItemNumber() {
    return packageItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setPackageItemNumber(int i) {
    packageItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  /**
   * @since 1.0
   */
  public short getReceivedCopyQuantity() {
    return receivedCopyQuantity;
  }

  /**
   * @since 1.0
   */
  public void setReceivedCopyQuantity(short s) {
    receivedCopyQuantity = s;
  }

  /**
   * @since 1.0
   */
  public Date getReceivedDate() {
    return receivedDate;
  }

  /**
   * @since 1.0
   */
  public void setReceivedDate(Date date) {
    receivedDate = date;
  }

  /**
   * @since 1.0
   */
  public int getReceiveItemNumber() {
    return receiveItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setReceiveItemNumber(int i) {
    receiveItemNumber = i;
  }

}
