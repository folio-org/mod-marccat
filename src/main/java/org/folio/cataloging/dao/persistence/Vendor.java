/*
 * (c) LibriCore
 *
 * Created on Jan 26, 2005
 *
 * Vendor.java
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class Vendor implements Persistence {
  private final PersistenceState persistenceState = new PersistenceState();
  private int vendorNumber;
  private String name;
  private String abbreviatedName;
  private short typeCode;
  private Integer OwningMainLibraryNumber;
  private boolean localOrShared;
  private Integer claimPolicyNumber;
  private Short formTypeCode;
  private Short paymentTypeCode;
  private Short acquisitionTypeCode;
  private Short currencyCode;
  private Float discount;
  private String url;
  private Date lastUpdateDate;
  private String languageCode;
  private String note;
  private int lastUpdateNumber;
  private boolean reportMailIndicator;
  private String ediSanNumber;
  private String ediHostName;
  private String ediFilePattern;
  private String ediUserString;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public Vendor() {
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

  /**
   * @since 1.0
   */
  public String getAbbreviatedName() {
    return abbreviatedName;
  }

  /**
   * @since 1.0
   */
  public void setAbbreviatedName(String string) {
    abbreviatedName = string;
  }

  /**
   * @since 1.0
   */
  public Short getAcquisitionTypeCode() {
    return acquisitionTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setAcquisitionTypeCode(Short short1) {
    acquisitionTypeCode = short1;
  }

  /**
   * @since 1.0
   */
  public Integer getClaimPolicyNumber() {
    return claimPolicyNumber;
  }

  /**
   * @since 1.0
   */
  public void setClaimPolicyNumber(Integer integer) {
    claimPolicyNumber = integer;
  }

  /**
   * @since 1.0
   */
  public Short getCurrencyCode() {
    return currencyCode;
  }

  /**
   * @since 1.0
   */
  public void setCurrencyCode(Short short1) {
    currencyCode = short1;
  }

  /**
   * @since 1.0
   */
  public Float getDiscount() {
    return discount;
  }

  /**
   * @since 1.0
   */
  public void setDiscount(Float float1) {
    discount = float1;
  }

  /**
   * @since 1.0
   */
  public String getEdiFilePattern() {
    return ediFilePattern;
  }

  /**
   * @since 1.0
   */
  public void setEdiFilePattern(String string) {
    ediFilePattern = string;
  }

  /**
   * @since 1.0
   */
  public String getEdiHostName() {
    return ediHostName;
  }

  /**
   * @since 1.0
   */
  public void setEdiHostName(String string) {
    ediHostName = string;
  }

  /**
   * @since 1.0
   */
  public String getEdiSanNumber() {
    return ediSanNumber;
  }

  /**
   * @since 1.0
   */
  public void setEdiSanNumber(String string) {
    ediSanNumber = string;
  }

  /**
   * @since 1.0
   */
  public String getEdiUserString() {
    return ediUserString;
  }

  /**
   * @since 1.0
   */
  public void setEdiUserString(String string) {
    ediUserString = string;
  }

  /**
   * @since 1.0
   */
  public Short getFormTypeCode() {
    return formTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setFormTypeCode(Short short1) {
    formTypeCode = short1;
  }

  /**
   * @since 1.0
   */
  public String getLanguageCode() {
    return languageCode;
  }

  /**
   * @since 1.0
   */
  public void setLanguageCode(String string) {
    languageCode = string;
  }

  /**
   * @since 1.0
   */
  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  /**
   * @since 1.0
   */
  public void setLastUpdateDate(Date date) {
    lastUpdateDate = date;
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
  public boolean isLocalOrShared() {
    return localOrShared;
  }

  /**
   * @since 1.0
   */
  public void setLocalOrShared(boolean b) {
    localOrShared = b;
  }

  /**
   * @since 1.0
   */
  public String getName() {
    return name;
  }

  /**
   * @since 1.0
   */
  public void setName(String string) {
    name = string;
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
  public Integer getOwningMainLibraryNumber() {
    return OwningMainLibraryNumber;
  }

  /**
   * @since 1.0
   */
  public void setOwningMainLibraryNumber(Integer integer) {
    OwningMainLibraryNumber = integer;
  }

  /**
   * @since 1.0
   */
  public Short getPaymentTypeCode() {
    return paymentTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setPaymentTypeCode(Short short1) {
    paymentTypeCode = short1;
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
  public boolean isReportMailIndicator() {
    return reportMailIndicator;
  }

  /**
   * @since 1.0
   */
  public void setReportMailIndicator(boolean b) {
    reportMailIndicator = b;
  }

  /**
   * @since 1.0
   */
  public short getTypeCode() {
    return typeCode;
  }

  /**
   * @since 1.0
   */
  public void setTypeCode(short s) {
    typeCode = s;
  }

  /**
   * @since 1.0
   */
  public String getUrl() {
    return url;
  }

  /**
   * @since 1.0
   */
  public void setUrl(String string) {
    url = string;
  }

  /**
   * @since 1.0
   */
  public int getVendorNumber() {
    return vendorNumber;
  }

  /**
   * @since 1.0
   */
  public void setVendorNumber(int i) {
    vendorNumber = i;
  }

}
