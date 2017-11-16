/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2005
 * 
 * SerialPart.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class SerialPart implements Persistence, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int serialPartNumber;
	private Integer serialCopyNumber;
	private Integer copyNumber;
	private char serialCopyStatusCode;
	private Date receivedDate;
	private Date publicationDate;
	private Date expiryDate;
	private short type;
	private String note;
	private String enumDescription;
	private String nameUnit;
	private String typeOfSupplementMaterial;
	private Date convertedGregorianYear;
	private String titleOfSupplementMaterial;
	private boolean tableOfContents;
	private BigDecimal price;
	private Integer localStatusCode;
	private Integer supplementaryMaterialCode;

	private final PersistenceState persistenceState = new PersistenceState();

	/**
	 * 
	 * @since 1.0
	 */
	public Date getConvertedGregorianYear() {
		return convertedGregorianYear;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Integer getCopyNumber() {
		return copyNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getEnumDescription() {
		return enumDescription;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getNameUnit() {
		return nameUnit;
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
	public Date getPublicationDate() {
		return publicationDate;
	}

	public String getPublicationDateAsString() {
		return new SimpleDateFormat("dd/MM/yyyy").format(getPublicationDate());
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}

	public String getReceivedDateAsString() {
		if (getReceivedDate() != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format( getReceivedDate());
		}
		else {
			return "";
		}
	}
	/**
	 * 
	 * @since 1.0
	 */
	public Integer getSerialCopyNumber() {
		return serialCopyNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getSerialCopyStatusCode() {
		return serialCopyStatusCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getSerialPartNumber() {
		return serialPartNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getTitleOfSupplementMaterial() {
		return titleOfSupplementMaterial;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getType() {
		return type;
	}

	public String getPartTypeText(Locale locale) {
		try {
			return new DAOCodeTable().load(T_SRL_PRT_TYP.class, getType(), locale).getLongText();
		} catch (DataAccessException e) {
			return "";
		}
	}
	/**
	 * 
	 * @since 1.0
	 */
	public String getTypeOfSupplementMaterial() {
		return typeOfSupplementMaterial;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setConvertedGregorianYear(Date date) {
		convertedGregorianYear = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCopyNumber(Integer integer) {
		copyNumber = integer;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setEnumDescription(String string) {
		enumDescription = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setExpiryDate(Date date) {
		expiryDate = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNameUnit(String string) {
		nameUnit = string;
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
	public void setPublicationDate(Date date) {
		publicationDate = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReceivedDate(Date date) {
		receivedDate = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSerialCopyNumber(Integer integer) {
		serialCopyNumber = integer;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSerialCopyStatusCode(char c) {
		serialCopyStatusCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSerialPartNumber(int i) {
		serialPartNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTitleOfSupplementMaterial(String string) {
		titleOfSupplementMaterial = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setType(short s) {
		type = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTypeOfSupplementMaterial(String string) {
		typeOfSupplementMaterial = string;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#evict()
	 */
	public void evict() throws DataAccessException {
		evict(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof SerialPart) {
			SerialPart o = (SerialPart) arg0;
			return o.getSerialPartNumber() > 0
					&& o.getSerialPartNumber() == this.getSerialPartNumber();
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.getSerialPartNumber();
	}

	/**
	 * @return the tableOfContents
	 */
	public boolean isTableOfContents() {
		return tableOfContents;
	}

	/**
	 * @param tableOfContents the tableOfContents to set
	 */
	public void setTableOfContents(boolean tableOfContents) {
		this.tableOfContents = tableOfContents;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the localStatusCode
	 */
	public Integer getLocalStatusCode() {
		return localStatusCode;
	}

	/**
	 * @param localStatusCode the localStatusCode to set
	 */
	public void setLocalStatusCode(Integer localStatusCode) {
		this.localStatusCode = localStatusCode;
	}

	/**
	 * @return the supplementaryMaterialCode
	 */
	public Integer getSupplementaryMaterialCode() {
		return supplementaryMaterialCode;
	}

	/**
	 * @param supplementaryMaterialCode the supplementaryMaterialCode to set
	 */
	public void setSupplementaryMaterialCode(Integer supplementaryMaterialCode) {
		this.supplementaryMaterialCode = supplementaryMaterialCode;
	}

}
