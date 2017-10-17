package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;

public class SRL_PRED_PAT implements Persistence, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int predictionPatternNumber;
	private int amicusNumber;
	private String label;
	private String caption;

	public SRL_PRED_PAT() {
	}

	public SRL_PRED_PAT(int amicusNumber, int cataloguingView) {
		this.amicusNumber = amicusNumber;
		try {
			Cache cache = (Cache) new HibernateUtil()
					.load(Cache.class, new Cache(
							amicusNumber, (short)cataloguingView));
			if (cache != null) {
				this.label = cache.getTitleHeadingMainStringText();
			}
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * @return the predictionPatternNumber
	 */
	public int getPredictionPatternNumber() {
		return predictionPatternNumber;
	}

	/**
	 * @param predictionPatternNumber
	 *            the predictionPatternNumber to set
	 */
	public void setPredictionPatternNumber(int predictionPatternNumber) {
		this.predictionPatternNumber = predictionPatternNumber;
	}

	/**
	 * @return the amicusNumber
	 */
	public int getAmicusNumber() {
		return amicusNumber;
	}

	/**
	 * @param amicusNumber
	 *            the amicusNumber to set
	 */
	public void setAmicusNumber(int amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	private PersistenceState persistenceState = new PersistenceState();

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#cancelChanges()
	 */
	public void cancelChanges() {
		persistenceState.cancelChanges();
	}

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#confirmChanges()
	 */
	public void confirmChanges() {
		persistenceState.confirmChanges();
	}

	/**
	 * @param obj
	 * @return
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof SRL_PRED_PAT) {
			SRL_PRED_PAT o = (SRL_PRED_PAT) obj;
			return o.getPredictionPatternNumber() == this
					.getPredictionPatternNumber();
		} else {
			return false;
		}
	}

	/**
	 * @param obj
	 * @throws DataAccessException
	 * @see librisuite.business.common.PersistenceState#evict(Object)
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new HibernateUtil();
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#getUpdateStatus()
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
	 * @return
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return getPredictionPatternNumber();
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#isChanged()
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#isDeleted()
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#isNew()
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#isRemoved()
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#markChanged()
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#markDeleted()
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#markNew()
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @see librisuite.business.common.PersistenceState#markUnchanged()
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws CallbackException
	 * @see librisuite.business.common.PersistenceState#onDelete(Session)
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see librisuite.business.common.PersistenceState#onLoad(Session,
	 *      Serializable)
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws CallbackException
	 * @see librisuite.business.common.PersistenceState#onSave(Session)
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws CallbackException
	 * @see librisuite.business.common.PersistenceState#onUpdate(Session)
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * @param i
	 * @see librisuite.business.common.PersistenceState#setUpdateStatus(int)
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/**
	 * @return
	 * @see librisuite.business.common.PersistenceState#toString()
	 */
	public String toString() {
		return persistenceState.toString();
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setPredictionPatternNumber(dao.getNextNumber("EP"));
	}
}
