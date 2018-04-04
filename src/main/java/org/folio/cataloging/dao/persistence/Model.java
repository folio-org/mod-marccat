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

	/** The model id. */
	private int id;

	/** The model label. */
	private String label;

	/** The String of the record template containing the tags. */
	private String recordFields;

	/** The frbr first group. This group is related to the FRBR entity: Works Expressions and Manifestations. */
	private Integer frbrFirstGroup;

	/** The persistence state used to mark the status of the object */
	private PersistenceState persistenceState = new PersistenceState();

	/**
	 * Instantiates a new model.
	 */
	public Model() {

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
	 * Gets the record fields.
	 *
	 * @return the record fields
	 */
	public String getRecordFields() {
		return recordFields;
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
	 * Evict.
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
	 * @since 1.0
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
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
	public boolean onDelete(final Session session) throws CallbackException {
		return persistenceState.onDelete(session);
	}

	/**
	 * On load.
	 *
	 * @param session the session
	 * @param serializable the serializable
	 * @since 1.0
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
	 * @since 1.0
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
	 * @since 1.0
	 */
	public boolean onUpdate(final Session session) throws CallbackException {
		return persistenceState.onUpdate(session);
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 * @since 1.0
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 * @since 1.0
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Sets the update status.
	 *
	 * @param i the new update status
	 * @since 1.0
	 */
	public void setUpdateStatus(final int i)
	{
		persistenceState.setUpdateStatus(i);
	}
}