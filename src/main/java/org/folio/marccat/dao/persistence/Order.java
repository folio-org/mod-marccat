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
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

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


  public Order() {
    super();

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


  public Date getCancelledDate() {
    return cancelledDate;
  }


  public void setCancelledDate(Date date) {
    cancelledDate = date;
  }


  public short getCancelledReasonCode() {
    return cancelledReasonCode;
  }


  public void setCancelledReasonCode(short s) {
    cancelledReasonCode = s;
  }


  public Date getCompletedDate() {
    return completedDate;
  }


  public void setCompletedDate(Date date) {
    completedDate = date;
  }


  public Float getCurrencyExchangeRate() {
    return currencyExchangeRate;
  }


  public void setCurrencyExchangeRate(Float float1) {
    currencyExchangeRate = float1;
  }


  public short getCurrencyTypeCode() {
    return currencyTypeCode;
  }


  public void setCurrencyTypeCode(short s) {
    currencyTypeCode = s;
  }


  public Boolean getFundOrderItemIndicator() {
    return fundOrderItemIndicator;
  }


  public void setFundOrderItemIndicator(Boolean boolean1) {
    fundOrderItemIndicator = boolean1;
  }


  public int getLastUpdateNumber() {
    return lastUpdateNumber;
  }


  public void setLastUpdateNumber(int i) {
    lastUpdateNumber = i;
  }


  public boolean isLibraryAccess() {
    return libraryAccess;
  }


  public void setLibraryAccess(boolean b) {
    libraryAccess = b;
  }


  public int getMainLibraryNumber() {
    return mainLibraryNumber;
  }


  public void setMainLibraryNumber(int i) {
    mainLibraryNumber = i;
  }


  public String getNote() {
    return note;
  }


  public void setNote(String string) {
    note = string;
  }


  public short getOrderAcquisitionTypeCode() {
    return orderAcquisitionTypeCode;
  }


  public void setOrderAcquisitionTypeCode(short s) {
    orderAcquisitionTypeCode = s;
  }


  public int getOrderCopyInvoicedQuantity() {
    return orderCopyInvoicedQuantity;
  }


  public void setOrderCopyInvoicedQuantity(int i) {
    orderCopyInvoicedQuantity = i;
  }


  public int getOrderCopyOrderQuantity() {
    return orderCopyOrderQuantity;
  }


  public void setOrderCopyOrderQuantity(int i) {
    orderCopyOrderQuantity = i;
  }


  public int getOrderCopyReceivedQuantity() {
    return orderCopyReceivedQuantity;
  }


  public void setOrderCopyReceivedQuantity(int i) {
    orderCopyReceivedQuantity = i;
  }


  public Date getOrderDate() {
    return orderDate;
  }


  public void setOrderDate(Date date) {
    orderDate = date;
  }


  public short getOrderFormTypeCode() {
    return orderFormTypeCode;
  }


  public void setOrderFormTypeCode(short s) {
    orderFormTypeCode = s;
  }


  public boolean isOrderHasSerial() {
    return orderHasSerial;
  }


  public void setOrderHasSerial(boolean b) {
    orderHasSerial = b;
  }


  public int getOrderNumber() {
    return orderNumber;
  }


  public void setOrderNumber(int i) {
    orderNumber = i;
  }


  public short getOrderPaymentTypeCode() {
    return orderPaymentTypeCode;
  }


  public void setOrderPaymentTypeCode(short s) {
    orderPaymentTypeCode = s;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public boolean isPublicAccess() {
    return publicAccess;
  }


  public void setPublicAccess(boolean b) {
    publicAccess = b;
  }


  public Date getReceivedDate() {
    return receivedDate;
  }


  public void setReceivedDate(Date date) {
    receivedDate = date;
  }


  public Date getSentDate() {
    return sentDate;
  }


  public void setSentDate(Date date) {
    sentDate = date;
  }


  public short getStatusTypeCode() {
    return statusTypeCode;
  }


  public void setStatusTypeCode(short s) {
    statusTypeCode = s;
  }


  public Float getTotal() {
    return total;
  }


  public void setTotal(Float float1) {
    total = float1;
  }


  public Float getTotalInput() {
    return totalInput;
  }


  public void setTotalInput(Float float1) {
    totalInput = float1;
  }


  public String getUserName() {
    return userName;
  }


  public void setUserName(String string) {
    userName = string;
  }


  public int getVendorContactPersonNumber() {
    return vendorContactPersonNumber;
  }


  public void setVendorContactPersonNumber(int i) {
    vendorContactPersonNumber = i;
  }


  public String getVendorOrderNumber() {
    return vendorOrderNumber;
  }


  public void setVendorOrderNumber(String string) {
    vendorOrderNumber = string;
  }


  public int getVendorOrganisationNumber() {
    return vendorOrganisationNumber;
  }


  public void setVendorOrganisationNumber(int i) {
    vendorOrganisationNumber = i;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // Do nothing because it doesn't have a key to generate

  }

}
