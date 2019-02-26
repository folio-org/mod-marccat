/*
 * (c) LibriCore
 *
 * Created on Jan 25, 2005
 *
 * OrderItem.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class OrderItem implements Persistence {
  private static final long serialVersionUID = -8826668590684591078L;
  private final PersistenceState persistenceState = new PersistenceState();
  private int orderNumber;
  private int orderItemNumber;
  private short type;
  private String title;
  private int quantity;
  private int receivedQuantity;
  private int invoicedQuantity;
  private float prepayAmount;
  private float price;
  private Float discountPercentage;
  private Float subtotal;
  private String librarySymbol;
  private String locationText;
  private int fundNumber;
  private String note;
  private int mainLibraryNumber;
  private int branchLibraryNumber;
  private Short locationCode;
  private float additionalCharges;
  private boolean completedIndicator;
  private int numberInvoiced;
  private Float cancelledSubtotal;
  private String status;
  private int deletedQuantity;


  public OrderItem() {
    super();
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof OrderItem) {
      OrderItem o = (OrderItem) arg0;
      return o.getOrderNumber() == this.getOrderNumber() &&
        o.getOrderItemNumber() == this.getOrderItemNumber();
    }
    return false;
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

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getOrderNumber() + getOrderItemNumber();
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getDeletedQuantity() {
    return deletedQuantity;
  }

  public void setDeletedQuantity(int deletedQuantity) {
    this.deletedQuantity = deletedQuantity;
  }


  public float getAdditionalCharges() {
    return additionalCharges;
  }


  public void setAdditionalCharges(float f) {
    additionalCharges = f;
  }


  public int getBranchLibraryNumber() {
    return branchLibraryNumber;
  }


  public void setBranchLibraryNumber(int i) {
    branchLibraryNumber = i;
  }


  public Float getCancelledSubtotal() {
    return cancelledSubtotal;
  }


  public void setCancelledSubtotal(Float float1) {
    cancelledSubtotal = float1;
  }


  public boolean isCompletedIndicator() {
    return completedIndicator;
  }


  public void setCompletedIndicator(boolean b) {
    completedIndicator = b;
  }


  public Float getDiscountPercentage() {
    return discountPercentage;
  }


  public void setDiscountPercentage(Float float1) {
    discountPercentage = float1;
  }


  public int getFundNumber() {
    return fundNumber;
  }


  public void setFundNumber(int i) {
    fundNumber = i;
  }


  public int getInvoicedQuantity() {
    return invoicedQuantity;
  }


  public void setInvoicedQuantity(int i) {
    invoicedQuantity = i;
  }


  public String getLibrarySymbol() {
    return librarySymbol;
  }


  public void setLibrarySymbol(String string) {
    librarySymbol = string;
  }


  public Short getLocationCode() {
    return locationCode;
  }


  public void setLocationCode(Short short1) {
    locationCode = short1;
  }


  public String getLocationText() {
    return locationText;
  }


  public void setLocationText(String string) {
    locationText = string;
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


  public int getNumberInvoiced() {
    return numberInvoiced;
  }


  public void setNumberInvoiced(int i) {
    numberInvoiced = i;
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


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public float getPrepayAmount() {
    return prepayAmount;
  }


  public void setPrepayAmount(float f) {
    prepayAmount = f;
  }


  public float getPrice() {
    return price;
  }


  public void setPrice(float f) {
    price = f;
  }


  public int getQuantity() {
    return quantity;
  }


  public void setQuantity(int i) {
    quantity = i;
  }


  public int getReceivedQuantity() {
    return receivedQuantity;
  }


  public void setReceivedQuantity(int i) {
    receivedQuantity = i;
  }


  public Float getSubtotal() {
    return subtotal;
  }


  public void setSubtotal(Float float1) {
    subtotal = float1;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String string) {
    title = string;
  }


  public short getType() {
    return type;
  }


  public void setType(short s) {
    type = s;
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
