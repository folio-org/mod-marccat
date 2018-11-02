package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCollectionRule;

import java.io.Serializable;

public class CLCTN_MST_RULE_RECORD implements Persistence, Comparable {
  private static final long serialVersionUID = 2522128570785338271L;

  static DAOCollectionRule dao = new DAOCollectionRule();

  private Integer ruleId;
  private Long recordId;

  private PersistenceState persistenceState = new PersistenceState();

  public CLCTN_MST_RULE_RECORD() {
    super();
  }

  public CLCTN_MST_RULE_RECORD(Integer id, Long record) {
    this.recordId = record;
    this.ruleId = id;
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
    result = prime * result + ruleId.intValue();
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CLCTN_MST_RULE_RECORD other = (CLCTN_MST_RULE_RECORD) obj;
    return ruleId == other.ruleId;
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

  public Integer getRuleId() {
    return ruleId;
  }

  public void setRuleId(Integer ruleId) {
    this.ruleId = ruleId;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public int compareTo(Object o) {
    CLCTN_MST_RULE_RECORD recordRule = (CLCTN_MST_RULE_RECORD) o;
//		int result = new Long(this.recordId).compareTo(new Long(recordRule.getRecordId()));
    int result = this.recordId.compareTo(recordRule.getRecordId());
    return result;
  }
}
