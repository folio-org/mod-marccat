package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.OrderConfigurationException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.serialControl.DuplicateVendorException;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class SRL_ORDR implements Persistence, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(SRL_ORDR.class);

	private int orderNumber;
	private int amicusNumber;
	private Date reminderDate;
	private Date expiryDate;
	private int quantity = 1;
	private List/* <SRL_LGCL_CPY> */subscriptions = new ArrayList();
	private List deletedSubscriptions = new ArrayList();
	private SRL_VNDR vendor;
	private int orderNo;
	private String documentNo;

	public SRL_ORDR() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.MONTH, 8);
		reminderDate = gc.getTime();
		gc = new GregorianCalendar();
		gc.add(Calendar.MONTH, 12);
		expiryDate = gc.getTime();
	}

	/**
	 * @return the orderNumber
	 */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *            the orderNumber to set
	 */
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	private Integer vendorNumber;
	private BigDecimal price;

	/**
	 * @return the amicusNumber
	 */
	public int getAmicusNumber() {
		return amicusNumber;
	}

	/**
	 * @param amicusNumber
	 *            the amicusNumber to set
	 */
	public void setAmicusNumber(int amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	/**
	 * @return the vendorNumber
	 */
	public Integer getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * @param vendorNumber
	 *            the vendorNumber to set
	 */
	public void setVendorNumber(Integer vendorNumber) {
		logger.debug("setVendorNumber(" + vendorNumber + ")");
		if (getVendorNumber() != null) {
			if (!getVendorNumber().equals(vendorNumber)) {
				logger.debug("setting Vendor to null");
				setVendor(null);
			}
		}
		this.vendorNumber = vendorNumber;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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

	private String currency = T_SRL_CURCY_TYP.EURO;

	private PersistenceState persistenceState = new PersistenceState();

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
	 * @return
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof SRL_ORDR) {
			SRL_ORDR o = (SRL_ORDR) obj;
			return o.getOrderNumber() == this.getOrderNumber();
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
	 * @return
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return this.getOrderNumber();
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

	/**
	 * @return
	 * @see PersistenceState#toString()
	 */
	public String toString() {
		return "SRL_ORDR(" + getOrderNumber() + ")";
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
		setOrderNumber(new SystemNextNumberDAO().getNextNumber("SO", session));
	}

	/**
	 * @return the subscriptions
	 */
	public List getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions
	 *            the subscriptions to set
	 */
	public void setSubscriptions(List subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * Validates that the state of this object is suitable for saving to the
	 * database (mandatory fields are present)
	 *
	 * @throws OrderConfigurationException
	 */
	public void validate() throws OrderConfigurationException {
		if (getQuantity() <= 0) {
			throw new OrderConfigurationException();
		}
	}

	/**
	 * @return the reminderDate
	 */
	public Date getReminderDate() {
		return reminderDate;
	}

	public String getReminderDateAsString() {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").format(getReminderDate());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param reminderDate
	 *            the reminderDate to set
	 */
	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	public String getExpiryDateAsString() {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").format(getExpiryDate());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the vendor
	 * @throws DataAccessException
	 */
	public SRL_VNDR getVendor() throws DataAccessException {
		//Modificato
		//if (vendor == null && getVendorNumber() != null) {
		if (getVendorNumber().intValue()!=0) {
			logger.debug("lazy init of vendor" + getVendorNumber());
			vendor = (SRL_VNDR) new HibernateUtil().load(SRL_VNDR.class,
					getVendorNumber());
		}
		return vendor;
	}

	public String getVendorName() {
		try {
		if (getVendor() != null) {
			return getVendor().getName();
		}
		else {
			return null;
		}
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(SRL_VNDR vendor) {
		this.vendor = vendor;
		if (vendor != null) {
			this.vendorNumber = new Integer(vendor.getVendorNumber());
		} else {
			this.vendorNumber = null;
		}
	}

	public void saveVendor(final Session session) throws DataAccessException, DuplicateVendorException, HibernateException {
		vendor.validate();
		if (vendor.isNew()) {
      vendor.generateNewKey(session);
    }
		vendor.markChanged();
		getDAO().persistByStatus(vendor, session);
		setVendorNumber(new Integer(vendor.getVendorNumber()));
	}

	public void deleteSubscription(Integer index) {
		if (index == null) {
			return;
		}
		SerialLogicalCopy lc = (SerialLogicalCopy) getSubscriptions().get(
				index.intValue());
		if (!lc.isNew()) {
			getDeletedSubscriptions().add(lc);
			lc.markDeleted();
		}
		List issues = new ArrayList(lc.getIssues());
		for (int i=0; i<issues.size(); i++) {
			//07/10/2014 modificato by Carmen
			lc.deleteSingleIssue(new Integer(i));
		}
		//07/10/2014
		lc.getIssues().removeAll(lc.getIssues());

		getSubscriptions().remove(index.intValue());
	}

	/**
	 * @return the deletedSubscriptions
	 */
	public List getDeletedSubscriptions() {
		return deletedSubscriptions;
	}

	/**
	 * @param deletedSubscriptions
	 *            the deletedSubscriptions to set
	 */
	public void setDeletedSubscriptions(List deletedSubscriptions) {
		this.deletedSubscriptions = deletedSubscriptions;
	}


	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}
