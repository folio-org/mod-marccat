/*
 *
 * 
 * ModelItem.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.persistence.Model;

/**
 * Base class for the record template models
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public abstract class ModelItem implements Persistence, Serializable {

	/** The persistence state used to mark the status of the object */
	private PersistenceState persistenceState = new PersistenceState();

	/** The id from the record. */
	private long item = 0;

	/** The model. */
	private Model model = null;

	/** The String of the record template containing the tags. */
	private String recordFields = null;

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
	public void setRecordFields(String recordFields) {
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
	public void setItem(long id) {
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
	public void setModel(Model model) {
		this.model = model;
	}



	/**
	 * Evict.
	 *
	 * @param obj the obj
	 * @throws DataAccessException the data access exception
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
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
	 * @since 1.0
	 */
	public boolean onDelete(Session session) throws CallbackException {
		return persistenceState.onDelete(session);
	}

	/**
	 * On load.
	 *
	 * @param session the session
	 * @param serializable the serializable
	 * @since 1.0
	 */
	public void onLoad(Session session, Serializable serializable) {
		persistenceState.onLoad(session, serializable);
	}

	/**
	 * On save.
	 *
	 * @param session the session
	 * @return true, if successful
	 * @throws CallbackException the callback exception
	 * @since 1.0
	 */
	public boolean onSave(Session session) throws CallbackException {
		return persistenceState.onSave(session);
	}

	/**
	 * On update.
	 *
	 * @param session the session
	 * @return true, if successful
	 * @throws CallbackException the callback exception
	 * @since 1.0
	 */
	public boolean onUpdate(Session session) throws CallbackException {
		return persistenceState.onUpdate(session);
	}

	/**
	 * Sets the update status.
	 *
	 * @param i the new update status
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

}
