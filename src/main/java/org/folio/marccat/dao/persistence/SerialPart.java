/*
 * (c) LibriCore
 *
 * Created on Jan 25, 2005
 *
 * SerialPart.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
  private final PersistenceState persistenceState = new PersistenceState();
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


  public Date getConvertedGregorianYear() {
    return convertedGregorianYear;
  }


  public void setConvertedGregorianYear(Date date) {
    convertedGregorianYear = date;
  }


  public Integer getCopyNumber() {
    return copyNumber;
  }


  public void setCopyNumber(Integer integer) {
    copyNumber = integer;
  }


  public String getEnumDescription() {
    return enumDescription;
  }


  public void setEnumDescription(String string) {
    enumDescription = string;
  }


  public Date getExpiryDate() {
    return expiryDate;
  }


  public void setExpiryDate(Date date) {
    expiryDate = date;
  }


  public String getNameUnit() {
    return nameUnit;
  }


  public void setNameUnit(String string) {
    nameUnit = string;
  }


  public String getNote() {
    return note;
  }


  public void setNote(String string) {
    note = string;
  }


  public Date getPublicationDate() {
    return publicationDate;
  }


  public void setPublicationDate(Date date) {
    publicationDate = date;
  }

  public String getPublicationDateAsString() {
    return new SimpleDateFormat("dd/MM/yyyy").format(getPublicationDate());
  }


  public Date getReceivedDate() {
    return receivedDate;
  }


  public void setReceivedDate(Date date) {
    receivedDate = date;
  }

  public String getReceivedDateAsString() {
    if (getReceivedDate() != null) {
      return new SimpleDateFormat("dd/MM/yyyy").format(getReceivedDate());
    } else {
      return "";
    }
  }


  public Integer getSerialCopyNumber() {
    return serialCopyNumber;
  }


  public void setSerialCopyNumber(Integer integer) {
    serialCopyNumber = integer;
  }


  public char getSerialCopyStatusCode() {
    return serialCopyStatusCode;
  }


  public void setSerialCopyStatusCode(char c) {
    serialCopyStatusCode = c;
  }


  public int getSerialPartNumber() {
    return serialPartNumber;
  }


  public void setSerialPartNumber(int i) {
    serialPartNumber = i;
  }


  public String getTitleOfSupplementMaterial() {
    return titleOfSupplementMaterial;
  }


  public void setTitleOfSupplementMaterial(String string) {
    titleOfSupplementMaterial = string;
  }


  public short getType() {
    return type;
  }


  public void setType(short s) {
    type = s;
  }

  //TODO refactoring
  public String getPartTypeText(Locale locale) {
		/*try {
			return new DAOCodeTable().load(T_SRL_PRT_TYP.class, getType(), locale).getLongText();
		} catch (DataAccessException e) {*/
    return "";
    //}
  }


  public String getTypeOfSupplementMaterial() {
    return typeOfSupplementMaterial;
  }


  public void setTypeOfSupplementMaterial(String string) {
    typeOfSupplementMaterial = string;
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
  public void generateNewKey(final Session session) throws DataAccessException {
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
