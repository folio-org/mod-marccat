package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.serialControl.DuplicateVendorException;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOPredictionPattern;
import org.folio.cataloging.dao.SystemNextNumberDAO;

import java.io.Serializable;

public class SRL_VNDR implements Persistence, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private PersistenceState persistenceState = new PersistenceState();

	private int vendorNumber;
	private String name;
	private String address;
	private String url;
	private String phone;
	private String email;
	private String currency;
	private Integer acquisitionType;

	/**
	 *
	 * @see PersistenceState#cancelChanges()
	 */
	public void cancelChanges() {
		persistenceState.cancelChanges();
	}

	/**
	 *
	 * @see PersistenceState#confirmChanges()
	 */
	public void confirmChanges() {
		persistenceState.confirmChanges();
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
	 * @see PersistenceState#getUpdateStatus()
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
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
	 *
	 * @see PersistenceState#markChanged()
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 *
	 * @see PersistenceState#markDeleted()
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 *
	 * @see PersistenceState#markNew()
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 *
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
	 * @see PersistenceState#onLoad(Session,
	 *      Serializable)
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
	 * @param i
	 * @see PersistenceState#setUpdateStatus(int)
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	public void evict() throws DataAccessException {
		evict(this);
	}

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
		setVendorNumber(new SystemNextNumberDAO().getNextNumber("SV", session));
	}

	public AbstractDAO getDAO() {
		return persistenceState.getDAO();
	}

	/**
	 * @return the vendorNumber
	 */
	public int getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * @param vendorNumber
	 *            the vendorNumber to set
	 */
	public void setVendorNumber(int vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the acquisitionType
	 */
	public Integer getAcquisitionType() {
		return acquisitionType;
	}

	/**
	 * @param acquisitionType
	 *            the acquisitionType to set
	 */
	public void setAcquisitionType(Integer acquisitionType) {
		this.acquisitionType = acquisitionType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof SRL_VNDR) {
			SRL_VNDR v = (SRL_VNDR) arg0;
			return v.getVendorNumber() > 0
					&& v.getVendorNumber() == this.getVendorNumber();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.getVendorNumber();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// TODO Auto-generated method stub
		return "SRL_VNDR(" + getName() + " '" + getVendorNumber() + "')";
	}

	public void validate() throws DataAccessException, DuplicateVendorException {
		if (isNew()) {
			new DAOPredictionPattern().checkDuplicateVendor(this);
		}
	}
}
