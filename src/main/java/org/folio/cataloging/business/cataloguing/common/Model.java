package org.folio.cataloging.business.cataloguing.common;

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
 * @since 1.0
 */
public abstract class Model implements Persistence, Serializable {

	protected int id = 0;

	protected String label;

	protected String recordFields;
	
	private Integer wemiFirstGroup;

	protected PersistenceState persistenceState = new PersistenceState();

	public Model() {

	}
	public void setRecordFields(String recordFields) {
		this.recordFields = recordFields;
	}

	public String getRecordFields() {
		return recordFields;
	}

	public Integer getWemiFirstGroup() {
		return wemiFirstGroup;
	}

	public void setWemiFirstGroup(Integer wemiFirstGroup) {
		this.wemiFirstGroup = wemiFirstGroup;
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void generateNewKey() throws DataAccessException {
	}

	/**
	 * @since 1.0
	 */
	public int getId() {
		return id;
	}

	/**
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
	}


	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}



	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session session) throws CallbackException {
		return persistenceState.onDelete(session);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session session, Serializable serializable) {
		persistenceState.onLoad(session, serializable);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session session) throws CallbackException {
		return persistenceState.onSave(session);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session session) throws CallbackException {
		return persistenceState.onUpdate(session);
	}


	/**
	 * @since 1.0
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @since 1.0
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i)
	{
		persistenceState.setUpdateStatus(i);
	}





}