/*
 * (c) LibriCore
 *
 * Created on 18-jun-2004
 *
 * Table_CPY_ID.java
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAODiscard;

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

  /**
   * @since 1.0
   */
  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    persistenceState.evict(this);
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
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return new DAODiscard();
  }

	/*
	@Override
	public void generateNewKey() throws DataAccessException {
	}*/

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
