/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2005
 * 
 * OrderItem.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class OrderItem implements Persistence 
{
	private static final long serialVersionUID = -8826668590684591078L;
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
	
	private final PersistenceState persistenceState = new PersistenceState();

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof OrderItem) {
			OrderItem o = (OrderItem)arg0;
			return o.getOrderNumber() == this.getOrderNumber() &&
			o.getOrderItemNumber() == this.getOrderItemNumber();
		}
		return false;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return persistenceState.getDAO();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getOrderNumber() + getOrderItemNumber();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
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

	/**
	 * 
	 * @since 1.0
	 */
	public float getAdditionalCharges() {
		return additionalCharges;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getBranchLibraryNumber() {
		return branchLibraryNumber;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public Float getCancelledSubtotal() {
		return cancelledSubtotal;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public boolean isCompletedIndicator() {
		return completedIndicator;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public Float getDiscountPercentage() {
		return discountPercentage;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getFundNumber() {
		return fundNumber;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getInvoicedQuantity() {
		return invoicedQuantity;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public String getLibrarySymbol() {
		return librarySymbol;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public Short getLocationCode() {
		return locationCode;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public String getLocationText() {
		return locationText;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getMainLibraryNumber() {
		return mainLibraryNumber;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getNumberInvoiced() {
		return numberInvoiced;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getOrderItemNumber() {
		return orderItemNumber;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getOrderNumber() {
		return orderNumber;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public PersistenceState getPersistenceState() {
		return persistenceState;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public float getPrepayAmount() {
		return prepayAmount;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public float getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public int getReceivedQuantity() {
		return receivedQuantity;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public Float getSubtotal() {
		return subtotal;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public short getType() {
		return type;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setAdditionalCharges(float f) {
		additionalCharges = f;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setBranchLibraryNumber(int i) {
		branchLibraryNumber = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setCancelledSubtotal(Float float1) {
		cancelledSubtotal = float1;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setCompletedIndicator(boolean b) {
		completedIndicator = b;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setDiscountPercentage(Float float1) {
		discountPercentage = float1;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setFundNumber(int i) {
		fundNumber = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setInvoicedQuantity(int i) {
		invoicedQuantity = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setLibrarySymbol(String string) {
		librarySymbol = string;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setLocationCode(Short short1) {
		locationCode = short1;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setLocationText(String string) {
		locationText = string;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setMainLibraryNumber(int i) {
		mainLibraryNumber = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setNote(String string) {
		note = string;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setNumberInvoiced(int i) {
		numberInvoiced = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setOrderItemNumber(int i) {
		orderItemNumber = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setOrderNumber(int i) {
		orderNumber = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setPrepayAmount(float f) {
		prepayAmount = f;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setPrice(float f) {
		price = f;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setQuantity(int i) {
		quantity = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setReceivedQuantity(int i) {
		receivedQuantity = i;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setSubtotal(Float float1) {
		subtotal = float1;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setTitle(String string) {
		title = string;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
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
