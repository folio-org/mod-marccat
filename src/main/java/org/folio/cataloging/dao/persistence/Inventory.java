/*
 * (c) LibriCore
 * 
 * Created on Jan 21, 2005
 * 
 * Inventory.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.folio.cataloging.dao.DAOInventory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class Inventory implements Persistence {
	private static DAOInventory daoInventory = new DAOInventory();
	//private int inventoryNumber;
	//private int mainLibraryNumber;
	private int bibItemNumber;
	private int copyNumber;
	private char materialTypeCode;
	private Date dateReceived;
	private String author;
	private String title;
	private String edition;
	private String publisher;
	private Short acquisitionTypeCode;
	private Float price;
	private String vendorName;
	private String note;
	private String possessed;
	private String serialNumber;
	private boolean checked = false;
	private short currencyCode = 0;
	private PersistenceState persistenceState = new PersistenceState();
	InventoryKey key;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public Inventory() {
		super();
		setPrice(new Float(0));
		setDateReceived(Calendar.getInstance().getTime());
		setKey(new InventoryKey());
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public Inventory(int copyNumber, int cataloguingView, int mainLibrary) throws DataAccessException {
		this();
		setCopyNumber(copyNumber);
		key.setMainLibraryNumber(mainLibrary);
		DAOInventory dao = new DAOInventory();
		dao.populateNewItem(this, cataloguingView);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Short getAcquisitionTypeCode() {
		return acquisitionTypeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return bibItemNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getCopyNumber() {
		return copyNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Date getDateReceived() {
		return dateReceived;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDateReceivedString() {
		if (getDateReceived() != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(getDateReceived());
		}
		else {
			return "";
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getEdition() {
		return edition;
	}

	/**
	

	/**
	 * 
	 * @since 1.0
	 */
	public char getMaterialTypeCode() {
		return materialTypeCode;
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
	public Float getPrice() {
		return price;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getPublisher() {
		return publisher;
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
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAcquisitionTypeCode(Short short1) {
		acquisitionTypeCode = short1;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthor(String string) {
		author = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setChecked(boolean b) {
		checked = b;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCopyNumber(int i) {
		copyNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCurrencyCode(short s) {
		currencyCode = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDateReceived(Date date) {
		dateReceived = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setEdition(String string) {
		edition = string;
	}

	
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Inventory other = (Inventory) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	

	/**
	 * 
	 * @since 1.0
	 */
	public void setMaterialTypeCode(char c) {
		materialTypeCode = c;
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
	public void setPrice(Float float1) {
		price = float1;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPublisher(String string) {
		publisher = string;
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
	public void setVendorName(String string) {
		vendorName = string;
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
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
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
		throw new IllegalArgumentException("New inventory key requires main library");
	}

	/**
	 * special case for next number -- derived from S_INVTRY keyed on main library
	 * 
	 * @since 1.0
	 */
	public void generateNewKey(int mainLibrary) throws DataAccessException {
		key.setInventoryNumber(daoInventory.getNextNumber(mainLibrary));
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return daoInventory;
	}

	public String getPossessed() {
		return possessed;
	}

	public void setPossessed(String possessed) {
		this.possessed = possessed;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public InventoryKey getKey() {
		return key;
	}

	public void setKey(InventoryKey key) {
		this.key = key;
	}

	

	
}
