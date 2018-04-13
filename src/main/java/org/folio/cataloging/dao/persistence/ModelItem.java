package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;

import java.io.Serializable;

/**
 * Base class for the record template models
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public abstract class ModelItem implements Persistence, Serializable {

	private PersistenceState persistenceState = new PersistenceState();
	private long item;
	private Model model;
	private String recordFields;

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
	 * Generate new key.
	 *
	 * @throws DataAccessException the data access exception
	 */
	public void generateNewKey() throws DataAccessException {
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 * @since 1.0
	 */
	public long getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 *
	 * @param id the new item
	 * @since 1.0
	 */
	public void setItem(final long id) {
		this.item = id;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 * @since 1.0
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 * @since 1.0
	 */
	public void setModel(final Model model) {
		this.model = model;
	}

	/**
	 * Evicts the given object.
	 *
	 * @param obj the object to be evicted.
	 * @throws DataAccessException in case of data access failure.
	 */
	public void evict(final Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	/**
	 * Evicts the this object.
	 *
	 * @throws DataAccessException in case of data access failure.
	 */
	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	/**
	 * Gets the update status.
	 *
	 * @return the update status
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
	 * Checks if is changed.
	 *
	 * @return true, if is changed
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * Checks if is deleted.
	 *
	 * @return true, if is deleted
	 * @since 1.0
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
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * Mark changed.
	 *
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * Mark deleted.
	 *
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * Mark new.
	 *
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * Mark unchanged.
	 *
	 * @since 1.0
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
	 * @param session the session
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

	/**
	 * Sets the update status.
	 *
	 * @param i the new update status
	 */
	public void setUpdateStatus(final int i) {
		persistenceState.setUpdateStatus(i);
	}

}
