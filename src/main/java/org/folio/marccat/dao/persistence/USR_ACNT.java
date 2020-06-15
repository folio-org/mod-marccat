/*
 * (c) LibriCore
 *
 * Created on Jun 21, 2004
 *
 * USR_ACNT.java
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
 * @version $Revision: 1.4 $, $Date: 2005/07/14 13:32:57 $
 * @since 1.0
 */
public class USR_ACNT implements Serializable, Persistence {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String name;
  private int orderNumber;
  private int orderDetailNumber;
  private int databasePreferenceOrder;
  private int cataloguingView;
  private int personNumber;
  private int branchLibrary;
  private int systemKeyNumber;
  private String profileTemplateName;
  private String operatingSystemGroupName;
  private short lockingVersionUpdateNumber;
  private int defaultSearchingView;
  private int defaultBibTemplateNumber;
  private int maxRecordCount;
  private short communicationsAccessType;
  private short defaultAddressType;
  private T_ITM_DSPLY defaultRecordDisplay;
  private short defaultMarcDisplay;
  private short defaultFullDisplay;
  private short defaultBriefDisplay;
  private boolean showResultsIndicator;
  private boolean accountActiveIndicator;
  private Date lastLogonDate;
  private Date passwordExpiryDate;
  private String note;
  private String queryFilter;
  private int displayLimit;
  private Integer defaultAuthorityModel;
  private Integer defaultBibliographicModel;
  private PersistenceState persistenceState = new PersistenceState();

  public void cancelChanges() {
    persistenceState.cancelChanges();
  }

  public void confirmChanges() {
    persistenceState.confirmChanges();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final USR_ACNT other = (USR_ACNT) obj;
    if (name == null) {
      return other.name == null;
    } else return name.equals(other.name);
  }


  /**
   * Getter for branchLibrary
   *
   * @return branchLibrary
   */
  public int getBranchLibrary() {
    return branchLibrary;
  }

  /**
   * Setter for branchLibrary
   *
   * @param i branchLibrary
   */
  public void setBranchLibrary(int i) {
    branchLibrary = i;
  }

  /**
   * Getter for cataloguingView
   *
   * @return cataloguingView
   */
  public int getCataloguingView() {
    return cataloguingView;
  }

  /**
   * Setter for cataloguingView
   *
   * @param i cataloguingView
   */
  public void setCataloguingView(int i) {
    cataloguingView = i;
  }

  /**
   * Getter for communicationsAccessType
   *
   * @return communicationsAccessType
   */
  public short getCommunicationsAccessType() {
    return communicationsAccessType;
  }

  /**
   * Setter for communicationAccessType
   *
   * @param s communicationAccessType
   */
  public void setCommunicationsAccessType(short s) {
    communicationsAccessType = s;
  }

  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  /**
   * Getter for databasePreferenceOrder
   *
   * @return databasePreferenceOrder
   */
  public int getDatabasePreferenceOrder() {
    return databasePreferenceOrder;
  }

  /**
   * Setter for databasePreferenceOrder
   *
   * @param i databasePreferenceOrder
   */
  public void setDatabasePreferenceOrder(int i) {
    databasePreferenceOrder = i;
  }

  /**
   * Getter for defaultAddresType
   *
   * @return defaultAddresType
   */
  public short getDefaultAddressType() {
    return defaultAddressType;
  }

  /**
   * Setter for defaultAddressType
   *
   * @param s defaultAddressType
   */
  public void setDefaultAddressType(short s) {
    defaultAddressType = s;
  }

  public Integer getDefaultAuthorityModel() {
    return defaultAuthorityModel;
  }

  public void setDefaultAuthorityModel(Integer defaultAuthorityModel) {
    this.defaultAuthorityModel = defaultAuthorityModel;
  }

  public Integer getDefaultBibliographicModel() {
    return defaultBibliographicModel;
  }

  public void setDefaultBibliographicModel(Integer defaultBibliographicModel) {
    this.defaultBibliographicModel = defaultBibliographicModel;
  }

  /**
   * Getter for defaultBibTemplateNumber
   *
   * @return defaultBibTemplateNumber
   */
  public int getDefaultBibTemplateNumber() {
    return defaultBibTemplateNumber;
  }

  /**
   * Setter for defaultBibTemplateNumber
   *
   * @param i defaultBibTemplateNumber
   */
  public void setDefaultBibTemplateNumber(int i) {
    defaultBibTemplateNumber = i;
  }

  /**
   * Getter for defaultBriefDisplay
   *
   * @return defaultBriefDisplay
   */
  public short getDefaultBriefDisplay() {
    return defaultBriefDisplay;
  }

  /**
   * Setter for defaultBriefDisplay
   *
   * @param s defaultBriefDisplay
   */
  public void setDefaultBriefDisplay(short s) {
    defaultBriefDisplay = s;
  }

  /**
   * Getter for defaultFullDisplay
   *
   * @return defaultFullDisplay
   */
  public short getDefaultFullDisplay() {
    return defaultFullDisplay;
  }

  /**
   * Setter for defaultFullDisplay
   *
   * @param s defaultFullDisplay
   */
  public void setDefaultFullDisplay(short s) {
    defaultFullDisplay = s;
  }

  /**
   * Getter for defaultMarcDisplay
   *
   * @return defaultMarcDisplay
   */
  public short getDefaultMarcDisplay() {
    return defaultMarcDisplay;
  }

  /**
   * Setter for defaultMarcDisplay
   *
   * @param s defaultMarcDisplay
   */
  public void setDefaultMarcDisplay(short s) {
    defaultMarcDisplay = s;
  }

  /**
   * Getter for defaultRecordDisplay
   *
   * @return defaultRecordDisplay
   */
  public T_ITM_DSPLY getDefaultRecordDisplay() {
    return defaultRecordDisplay;
  }

  /**
   * Setter for defaultRecordDisplay
   *
   * @param s defaultRecordDisplay
   */
  public void setDefaultRecordDisplay(T_ITM_DSPLY s) {
    defaultRecordDisplay = s;
  }

  /**
   * Getter for defaultSearchingView
   *
   * @return defaultSearchingView
   */
  public int getDefaultSearchingView() {
    return defaultSearchingView;
  }

  /**
   * Setter for defaultSearchingView
   *
   * @param i defaultSearchingView
   */
  public void setDefaultSearchingView(int i) {
    defaultSearchingView = i;
  }

  /**
   * Getter for displayLimit
   *
   * @return displayLimit
   */
  public int getDisplayLimit() {
    return displayLimit;
  }

  /**
   * Setter for displayLimit
   *
   * @param i displayLimit
   */
  public void setDisplayLimit(int i) {
    displayLimit = i;
  }

  /**
   * Getter for lastLogonDate
   *
   * @return lastLogonDate
   */
  public Date getLastLogonDate() {
    return lastLogonDate;
  }

  /**
   * Setter for lastLogonDate
   *
   * @param date lastLogonDate
   */
  public void setLastLogonDate(Date date) {
    lastLogonDate = date;
  }

  /**
   * Getter for lockingVersionUpdateNumber
   *
   * @return lockingVersionUpdateNumber
   */
  public short getLockingVersionUpdateNumber() {
    return lockingVersionUpdateNumber;
  }

  /**
   * Setter for lockingVersionUpdateNumber
   *
   * @param s lockingVersionUpdateNumber
   */
  public void setLockingVersionUpdateNumber(short s) {
    lockingVersionUpdateNumber = s;
  }

  /**
   * Getter for maxRecordCound
   *
   * @return maxRecordCound
   */
  public int getMaxRecordCount() {
    return maxRecordCount;
  }

  /**
   * Setter for maxRecordCount
   *
   * @param i maxRecordCount
   */
  public void setMaxRecordCount(int i) {
    maxRecordCount = i;
  }

  /**
   * Getter for name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   *
   * @param string name
   */
  public void setName(String string) {
    name = string;
  }

  /**
   * Getter for note
   *
   * @return note
   */
  public String getNote() {
    return note;
  }

  /**
   * Setter for note
   *
   * @param string note
   */
  public void setNote(String string) {
    note = string;
  }

  /**
   * Getter for operatingSystemGroupName
   *
   * @return operatingSystemGroupName
   */
  public String getOperatingSystemGroupName() {
    return operatingSystemGroupName;
  }

  /**
   * Setter for operatingSystemGroupName
   *
   * @param string operatingSystemGroupName
   */
  public void setOperatingSystemGroupName(String string) {
    operatingSystemGroupName = string;
  }

  /**
   * Getter for orderDetailNumber
   *
   * @return orderDetailNumber
   */
  public int getOrderDetailNumber() {
    return orderDetailNumber;
  }

  /**
   * Setter for orderDetailNumber
   *
   * @param i orderDetailNumber
   */
  public void setOrderDetailNumber(int i) {
    orderDetailNumber = i;
  }

  /**
   * Getter for orderNumber
   *
   * @return orderNumber
   */
  public int getOrderNumber() {
    return orderNumber;
  }

  /**
   * Setter for orderNumber
   *
   * @param i orderNumber
   */
  public void setOrderNumber(int i) {
    orderNumber = i;
  }

  /**
   * Getter for passwordExpiryDate
   *
   * @return passwordExpiryDate
   */
  public Date getPasswordExpiryDate() {
    return passwordExpiryDate;
  }

  /**
   * Setter for passwordExpiryDate
   *
   * @param date passwordExpiryDate
   */
  public void setPasswordExpiryDate(Date date) {
    passwordExpiryDate = date;
  }

  /**
   * Getter for passwordNumber
   *
   * @return passwordNumber
   */
  public int getPersonNumber() {
    return personNumber;
  }

  /**
   * Setter for personNumber
   *
   * @param i personNumber
   */
  public void setPersonNumber(int i) {
    personNumber = i;
  }

  /**
   * Getter for profileTemplateName
   *
   * @return profileTemplateName
   */
  public String getProfileTemplateName() {
    return profileTemplateName;
  }

  /**
   * Setter for profileTemplateName
   *
   * @param string profileTemplateName
   */
  public void setProfileTemplateName(String string) {
    profileTemplateName = string;
  }

  /**
   * Getter for queryFilter
   *
   * @return queryFilter
   */
  public String getQueryFilter() {
    return queryFilter;
  }

  /**
   * Setter for queryFilter
   *
   * @param string queryFilter
   */
  public void setQueryFilter(String string) {
    queryFilter = string;
  }

  /**
   * Getter for systemKeyNumber
   *
   * @return systemKeyNumber
   */
  public int getSystemKeyNumber() {
    return systemKeyNumber;
  }

  /**
   * Setter for systemKeyNumber
   *
   * @param i systemKeyNumber
   */
  public void setSystemKeyNumber(int i) {
    systemKeyNumber = i;
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /**
   * Getter for accountActiveIndicator
   *
   * @return accountActiveIndicator
   */
  public boolean isAccountActiveIndicator() {
    return accountActiveIndicator;
  }

  /**
   * Setter for accountActiveIndicator
   *
   * @param b accountActiveIndicator
   */
  public void setAccountActiveIndicator(boolean b) {
    accountActiveIndicator = b;
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

  /**
   * Getter for showResultsIndicator
   *
   * @return showResultsIndicator
   */
  public boolean isShowResultsIndicator() {
    return showResultsIndicator;
  }

  /**
   * Setter for showResultsIndicator
   *
   * @param b showResultsIndicator
   */
  public void setShowResultsIndicator(boolean b) {
    showResultsIndicator = b;
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

  public String toString() {
    return persistenceState.toString();
  }

}
