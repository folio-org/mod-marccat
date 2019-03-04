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

  }


  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }


  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
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


  public int getBibItemNumber() {
    return bibItemNumber;
  }


  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }


  public String getConditionNote() {
    return conditionNote;
  }


  public void setConditionNote(String string) {
    conditionNote = string;
  }


  public String getLibrarySymbolCode() {
    return librarySymbolCode;
  }


  public void setLibrarySymbolCode(String string) {
    librarySymbolCode = string;
  }


  public String getLocationText() {
    return locationText;
  }


  public void setLocationText(String string) {
    locationText = string;
  }


  public int getOrderItemNumber() {
    return orderItemNumber;
  }


  public void setOrderItemNumber(int i) {
    orderItemNumber = i;
  }


  public int getOrderNumber() {
    return orderNumber;
  }


  public void setOrderNumber(int i) {
    orderNumber = i;
  }


  public int getPackageItemNumber() {
    return packageItemNumber;
  }


  public void setPackageItemNumber(int i) {
    packageItemNumber = i;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public short getReceivedCopyQuantity() {
    return receivedCopyQuantity;
  }


  public void setReceivedCopyQuantity(short s) {
    receivedCopyQuantity = s;
  }


  public Date getReceivedDate() {
    return receivedDate;
  }


  public void setReceivedDate(Date date) {
    receivedDate = date;
  }


  public int getReceiveItemNumber() {
    return receiveItemNumber;
  }


  public void setReceiveItemNumber(int i) {
    receiveItemNumber = i;
  }

}
