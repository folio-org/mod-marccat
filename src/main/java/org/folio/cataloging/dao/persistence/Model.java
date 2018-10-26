package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;

import java.io.Serializable;

/**
 * Base class for template models
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public abstract class Model implements Persistence, Serializable {

  private int id;
  private String label;
  private String recordFields;
  private Integer frbrFirstGroup;
  private PersistenceState persistenceState = new PersistenceState();

  /**
   * Gets the record fields.
   *
   * @return the record fields
   */
  public String getRecordFields() {
    return recordFields;
  }

  /**
   * Sets the record fields.
   *
   * @param recordFields the new record fields
   */
  public void setRecordFields(final String recordFields) {
    this.recordFields = recordFields;
  }

  /**
   * Gets the frbr first group.
   *
   * @return the frbr first group
   */
  public Integer getFrbrFirstGroup() {
    return frbrFirstGroup;
  }

  /**
   * Sets the frbr first group.
   *
   * @param frbrFirstGroup the new frbr first group
   */
  public void setFrbrFirstGroup(final Integer frbrFirstGroup) {
    this.frbrFirstGroup = frbrFirstGroup;
  }

  /**
   * Evict.
   *
   * @throws DataAccessException the data access exception
   */
  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }

  /**
   * Evicts.
   *
   * @param obj the obj
   * @throws DataAccessException the data access exception
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  /**
   * Generate new key.
   *
   * @throws DataAccessException the data access exception
   */
  public void generateNewKey() throws DataAccessException {
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(final int id) {
    this.id = id;
  }

  /**
   * Gets the label.
   *
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label the new label
   */
  public void setLabel(final String label) {
    this.label = label;
  }

  /**
   * Gets the update status.
   *
   * @return the update status
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(final int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * Checks if is new.
   *
   * @return true, if is new
   * @since 1.0
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * Marks changed.
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * Marks deleted.
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * Marks as new.
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * Marks unchanged.
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * On delete.
   *
   * @param session the session
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onDelete(final Session session) throws CallbackException {
    return persistenceState.onDelete(session);
  }

  /**
   * On load.
   *
   * @param session      the session
   * @param serializable the serializable
   */
  public void onLoad(final Session session, final Serializable serializable) {
    persistenceState.onLoad(session, serializable);
  }

  /**
   * On save.
   *
   * @param session the session
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onSave(final Session session) throws CallbackException {
    return persistenceState.onSave(session);
  }

  /**
   * On update.
   *
   * @param session the session
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onUpdate(final Session session) throws CallbackException {
    return persistenceState.onUpdate(session);
  }
}
