/*
 * (c) LibriCore
 *
 * Created on 18-jun-2004
 *
 * Table_CPY_ID.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAODiscard;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.util.Date;

public class DiscardCopy implements Persistence, Serializable {

  private int copyIdNumber;
  private int bibItemNumber;
  private int organisationNumber;
  private int branchOrganisationNumber;
  private Date discardDate;
  private int discardCode;
  private short locationNameCode;
  private PersistenceState persistenceState = new PersistenceState();


  public int getBibItemNumber() {
    return bibItemNumber;
  }

  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  public int getBranchOrganisationNumber() {
    return branchOrganisationNumber;
  }

  public void setBranchOrganisationNumber(int i) {
    branchOrganisationNumber = i;
  }

  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  public void setCopyIdNumber(int i) {
    copyIdNumber = i;
  }

  public int getOrganisationNumber() {
    return organisationNumber;
  }

  public void setOrganisationNumber(int i) {
    organisationNumber = i;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }


  public void evict(Object obj) {
    persistenceState.evict(obj);
  }

  public void evict() {
    persistenceState.evict(this);
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

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return new DAODiscard();
  }

  public Date getDiscardDate() {
    return discardDate;
  }

  public void setDiscardDate(Date discardDate) {
    this.discardDate = discardDate;
  }

  public int getDiscardCode() {
    return discardCode;
  }

  public void setDiscardCode(int discardCode) {
    this.discardCode = discardCode;
  }

  public short getLocationNameCode() {
    return locationNameCode;
  }

  public void setLocationNameCode(short locationNameCode) {
    this.locationNameCode = locationNameCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + copyIdNumber;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final DiscardCopy other = (DiscardCopy) obj;
    return copyIdNumber == other.copyIdNumber;
  }


}
