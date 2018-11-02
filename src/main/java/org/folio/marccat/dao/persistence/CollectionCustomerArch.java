package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCollectionCustom;

import java.io.Serializable;
import java.util.Date;

public class CollectionCustomerArch implements Persistence {
  private static final long serialVersionUID = 2522128570785338271L;

  static DAOCollectionCustom dao = new DAOCollectionCustom();

  private Integer idCollection;
  private String customerId;
  private int nameIta;
  private Integer idCollectionMST;
  private Integer statusCode;
  private Date dateCreation;
  private Date dateCancel;
  private Date dateModify;
  private String typologyCode;
  private String upgrade;
  private String userCreate;
  private String userModify;
  private int year;
  private Date dateIniVal;
  private Date dateFinVal;
  private String dateType;
  private Date dateCancelColl;
  private String userCancel;
  private PersistenceState persistenceState = new PersistenceState();

  public CollectionCustomerArch(CollectionCustomer customer, String userCancel) {
    this.idCollection = customer.getIdCollection();
    this.customerId = customer.getCustomerId();
    this.nameIta = customer.getNameIta();
    this.idCollectionMST = customer.getIdCollectionMST();
    this.statusCode = customer.getStatusCode();
    this.dateCreation = customer.getDateCreation();
    this.dateCancel = customer.getDateCancel();
    this.dateModify = customer.getDateModify();
    this.typologyCode = customer.getTypologyCode();
    this.upgrade = customer.getUpgrade();
    this.userCreate = customer.getUserCreate();
    this.userModify = customer.getUserModify();
    this.year = customer.getYear();
    this.dateIniVal = customer.getDateIniVal();
    this.dateFinVal = customer.getDateFinVal();
    this.dateType = customer.getDateType();
    this.dateCancelColl = new Date();
    this.userCancel = userCancel;
  }

  public CollectionCustomerArch() {
    super();
  }

  public Date getDateCancelColl() {
    return dateCancelColl;
  }

  public void setDateCancelColl(Date dateCancelColl) {
    this.dateCancelColl = dateCancelColl;
  }

  public String getUserCancel() {
    return userCancel;
  }

  public void setUserCancel(String userCancel) {
    this.userCancel = userCancel;
  }

  public String getDateType() {
    return dateType;
  }

  public void setDateType(String dateType) {
    this.dateType = dateType;
  }

  public Date getDateIniVal() {
    return dateIniVal;
  }

  public void setDateIniVal(Date dateIniVal) {
    this.dateIniVal = dateIniVal;
  }

  public Date getDateFinVal() {
    return dateFinVal;
  }

  public void setDateFinVal(Date dateFinVal) {
    this.dateFinVal = dateFinVal;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public Date getDateModify() {
    return dateModify;
  }

  public void setDateModify(Date dateModify) {
    this.dateModify = dateModify;
  }

  public String getUserCreate() {
    return userCreate;
  }

  public void setUserCreate(String userCreate) {
    this.userCreate = userCreate;
  }

  public String getUserModify() {
    return userModify;
  }

  public void setUserModify(String userModify) {
    this.userModify = userModify;
  }

  public Integer getIdCollection() {
    return idCollection;
  }

  public void setIdCollection(Integer idCollection) {
    this.idCollection = idCollection;
  }

  public int getNameIta() {
    return nameIta;
  }

  public void setNameIta(int nameIta) {
    this.nameIta = nameIta;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public Date getDateCreation() {
    return dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Date getDateCancel() {
    return dateCancel;
  }

  public void setDateCancel(Date dateCancel) {
    this.dateCancel = dateCancel;
  }

  public String getTypologyCode() {
    return typologyCode;
  }

  public void setTypologyCode(String typologyCode) {
    this.typologyCode = typologyCode;
  }

  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    evict(this);
  }

  public AbstractDAO getDAO() {
    return dao;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idCollection.intValue();
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CollectionCustomerArch other = (CollectionCustomerArch) obj;
    return idCollection == other.idCollection;
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

  public void generateNewKey() throws DataAccessException {
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public Integer getIdCollectionMST() {
    return idCollectionMST;
  }

  public void setIdCollectionMST(Integer idCollectionMST) {
    this.idCollectionMST = idCollectionMST;
  }

  public String getUpgrade() {
    return upgrade;
  }

  public void setUpgrade(String upgrade) {
    this.upgrade = upgrade;
  }
}
