/*
 * (c) LibriCore
 *
 * Created on Aug 20, 2004
 *
 * PersistentObject.java
 */
package org.folio.cataloging.business.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Lifecycle;
import net.sf.hibernate.Session;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class PersistenceState implements Lifecycle, Serializable {

	private int updateStatus = UpdateStatus.NEW;
	private Integer committedStatus = null;

	/**
	 *
	 */
	public int getUpdateStatus() {
		return updateStatus;
	}

	/**
		 *
		 */
	public void setUpdateStatus(int i) {
		updateStatus = i;
	}

	public void onLoad(Session arg0, Serializable arg1) {
		// MIKE: do not register for onLoad. All read objects must be marked as unchanged
		// as internal as external of a transaction
		setUpdateStatus(UpdateStatus.UNCHANGED);
	}

	/* (non-Javadoc)
	 * @see net.sf.hibernate.Lifecycle#onDelete(net.sf.hibernate.Session)
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		registerNextStatus(updateStatus, UpdateStatus.REMOVED);
		//setUpdateStatus(UpdateStatus.REMOVED);
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.hibernate.Lifecycle#onSave(net.sf.hibernate.Session)
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		registerNextStatus(updateStatus, UpdateStatus.UNCHANGED);
		//setUpdateStatus(UpdateStatus.UNCHANGED);
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.hibernate.Lifecycle#onUpdate(net.sf.hibernate.Session)
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		registerNextStatus(updateStatus, UpdateStatus.UNCHANGED);
		//setUpdateStatus(UpdateStatus.UNCHANGED);
		return false;
	}

	public boolean isChanged() {
		return updateStatus == UpdateStatus.CHANGED;
	}

	public boolean isDeleted() {
		return updateStatus == UpdateStatus.DELETED;
	}

	public boolean isNew() {
		return updateStatus == UpdateStatus.NEW;
	}

	public boolean isRemoved() {
		return updateStatus == UpdateStatus.REMOVED;
	}

	/**
	 * If object is now UNCHANGED or REMOVED (set via Hibernate)
	 * make it CHANGED (otherwise leave it alone)
	 *
	 */
	public void markChanged() {
		if (updateStatus == UpdateStatus.UNCHANGED
			|| updateStatus == UpdateStatus.REMOVED) {
			setUpdateStatus(UpdateStatus.CHANGED);
		}
	}

	public void markNew() {
		setUpdateStatus(UpdateStatus.NEW);
	}

	public void markUnchanged() {
		setUpdateStatus(UpdateStatus.UNCHANGED);
	}

	public void markDeleted() {
		setUpdateStatus(UpdateStatus.DELETED);
	}

	@Deprecated
	public void evict(Object obj) throws DataAccessException {
	}

	/**
	 * Default implementation for Persistence objects
	 * @since 1.0
	 */
	public AbstractDAO getDAO() {
	  return new AbstractDAO();
	}

	/**
	 * save next status instead to change it immediately
	 * @param fromStatus previous state (unused in this release)
	 * @param toStatus definitive state (assumed after the commit)
	 */
	private void registerNextStatus(int fromStatus, int toStatus){
		if(fromStatus == toStatus) return;
		committedStatus = new Integer(toStatus);
		TransactionalHibernateOperation.register(this);
	}

	/**
	 * Commit the changes
	 *
	 */
	public void confirmChanges() {
		if(committedStatus == null) return;
		updateStatus = committedStatus.intValue();
	}

	/**
	 * rollback the changes
	 *
	 */
	public void cancelChanges() {
		// do nothing
	}

	public String toString() {
		return super.toString()+" "+updateStatus+"->"+committedStatus;
	}


}
