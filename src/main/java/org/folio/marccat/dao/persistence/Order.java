/*
 * (c) LibriCore
 *
 * Created on Jan 25, 2005
 *
 * Order.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class Order implements Persistence {
  private final PersistenceState persistenceState = new PersistenceState();
  private int orderNumber;
  private String vendorOrderNumber;
  private Date orderDate;
  private Date receivedDate;
  private Date completedDate;
  private Date sentDate;
  private Date cancelledDate;
  private String userName;
  private Float total;
  private Float totalInput;
  private short orderAcquisitionTypeCode;
  private short orderPaymentTypeCode;
  private short orderFormTypeCode;
  private short currencyTypeCode;
  private Float currencyExchangeRate;
  private int mainLibraryNumber;
  private String note;
  private short statusTypeCode;
  private int vendorOrganisationNumber;
  private int vendorContactPersonNumber;
  private boolean libraryAccess;
  private boolean publicAccess;
  private int orderCopyOrderQuantity;
  private int orderCopyReceivedQuantity;
  private int orderCopyInvoicedQuantity;
  private boolean orderHasSerial;
  private Boolean fundOrderItemIndicator;
  private int lastUpdateNumber;
  private short cancelledReasonCode;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public Order() {
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

  /**
   * @since 1.0
   */
  public Date getCancelledDate() {
    return cancelledDate;
  }

  /**
   * @since 1.0
   */
  public void setCancelledDate(Date date) {
    cancelledDate = date;
  }

  /**
   * @since 1.0
   */
  public short getCancelledReasonCode() {
    return cancelledReasonCode;
  }

  /**
   * @since 1.0
   */
  public void setCancelledReasonCode(short s) {
    cancelledReasonCode = s;
  }

  /**
   * @since 1.0
   */
  public Date getCompletedDate() {
    return completedDate;
  }

  /**
   * @since 1.0
   */
  public void setCompletedDate(Date date) {
    completedDate = date;
  }

  /**
   * @since 1.0
   */
  public Float getCurrencyExchangeRate() {
    return currencyExchangeRate;
  }

  /**
   * @since 1.0
   */
  public void setCurrencyExchangeRate(Float float1) {
    currencyExchangeRate = float1;
  }

  /**
   * @since 1.0
   */
  public short getCurrencyTypeCode() {
    return currencyTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setCurrencyTypeCode(short s) {
    currencyTypeCode = s;
  }

  /**
   * @since 1.0
   */
  public Boolean getFundOrderItemIndicator() {
    return fundOrderItemIndicator;
  }

  /**
   * @since 1.0
   */
  public void setFundOrderItemIndicator(Boolean boolean1) {
    fundOrderItemIndicator = boolean1;
  }

  /**
   * @since 1.0
   */
  public int getLastUpdateNumber() {
    return lastUpdateNumber;
  }

  /**
   * @since 1.0
   */
  public void setLastUpdateNumber(int i) {
    lastUpdateNumber = i;
  }

  /**
   * @since 1.0
   */
  public boolean isLibraryAccess() {
    return libraryAccess;
  }

  /**
   * @since 1.0
   */
  public void setLibraryAccess(boolean b) {
    libraryAccess = b;
  }

  /**
   * @since 1.0
   */
  public int getMainLibraryNumber() {
    return mainLibraryNumber;
  }

  /**
   * @since 1.0
   */
  public void setMainLibraryNumber(int i) {
    mainLibraryNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getNote() {
    return note;
  }

  /**
   * @since 1.0
   */
  public void setNote(String string) {
    note = string;
  }

  /**
   * @since 1.0
   */
  public short getOrderAcquisitionTypeCode() {
    return orderAcquisitionTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setOrderAcquisitionTypeCode(short s) {
    orderAcquisitionTypeCode = s;
  }

  /**
   * @since 1.0
   */
  public int getOrderCopyInvoicedQuantity() {
    return orderCopyInvoicedQuantity;
  }

  /**
   * @since 1.0
   */
  public void setOrderCopyInvoicedQuantity(int i) {
    orderCopyInvoicedQuantity = i;
  }

  /**
   * @since 1.0
   */
  public int getOrderCopyOrderQuantity() {
    return orderCopyOrderQuantity;
  }

  /**
   * @since 1.0
   */
  public void setOrderCopyOrderQuantity(int i) {
    orderCopyOrderQuantity = i;
  }

  /**
   * @since 1.0
   */
  public int getOrderCopyReceivedQuantity() {
    return orderCopyReceivedQuantity;
  }

  /**
   * @since 1.0
   */
  public void setOrderCopyReceivedQuantity(int i) {
    orderCopyReceivedQuantity = i;
  }

  /**
   * @since 1.0
   */
  public Date getOrderDate() {
    return orderDate;
  }

  /**
   * @since 1.0
   */
  public void setOrderDate(Date date) {
    orderDate = date;
  }

  /**
   * @since 1.0
   */
  public short getOrderFormTypeCode() {
    return orderFormTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setOrderFormTypeCode(short s) {
    orderFormTypeCode = s;
  }

  /**
   * @since 1.0
   */
  public boolean isOrderHasSerial() {
    return orderHasSerial;
  }

  /**
   * @since 1.0
   */
  public void setOrderHasSerial(boolean b) {
    orderHasSerial = b;
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
  public short getOrderPaymentTypeCode() {
    return orderPaymentTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setOrderPaymentTypeCode(short s) {
    orderPaymentTypeCode = s;
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
  public boolean isPublicAccess() {
    return publicAccess;
  }

  /**
   * @since 1.0
   */
  public void setPublicAccess(boolean b) {
    publicAccess = b;
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
  public Date getSentDate() {
    return sentDate;
  }

  /**
   * @since 1.0
   */
  public void setSentDate(Date date) {
    sentDate = date;
  }

  /**
   * @since 1.0
   */
  public short getStatusTypeCode() {
    return statusTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setStatusTypeCode(short s) {
    statusTypeCode = s;
  }

  /**
   * @since 1.0
   */
  public Float getTotal() {
    return total;
  }

  /**
   * @since 1.0
   */
  public void setTotal(Float float1) {
    total = float1;
  }

  /**
   * @since 1.0
   */
  public Float getTotalInput() {
    return totalInput;
  }

  /**
   * @since 1.0
   */
  public void setTotalInput(Float float1) {
    totalInput = float1;
  }

  /**
   * @since 1.0
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @since 1.0
   */
  public void setUserName(String string) {
    userName = string;
  }

  /**
   * @since 1.0
   */
  public int getVendorContactPersonNumber() {
    return vendorContactPersonNumber;
  }

  /**
   * @since 1.0
   */
  public void setVendorContactPersonNumber(int i) {
    vendorContactPersonNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getVendorOrderNumber() {
    return vendorOrderNumber;
  }

  /**
   * @since 1.0
   */
  public void setVendorOrderNumber(String string) {
    vendorOrderNumber = string;
  }

  /**
   * @since 1.0
   */
  public int getVendorOrganisationNumber() {
    return vendorOrganisationNumber;
  }

  /**
   * @since 1.0
   */
  public void setVendorOrganisationNumber(int i) {
    vendorOrganisationNumber = i;
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

}
