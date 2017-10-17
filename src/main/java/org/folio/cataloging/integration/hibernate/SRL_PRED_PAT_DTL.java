package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.common.ChronologyConfigurationException;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

public class SRL_PRED_PAT_DTL implements Persistence, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(SRL_PRED_PAT_DTL.class);

	private int predictionPatternNumber;
	private int sequenceNumber;
	private short partType;
	private int patternClass;
	private Integer ordinal = new Integer(1);
	private Integer timeField = new Integer(1);
	private Integer stepCount = new Integer(1);
	private Integer month = new Integer(0);
	private Integer day = new Integer(1);
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;

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
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the patternType
	 */
	public short getPartType() {
		return partType;
	}

	/**
	 * @param patternType
	 *            the patternType to set
	 */
	public void setPartType(short partType) {
		this.partType = partType;
	}

	/**
	 * @return the patternClass
	 */
	public int getPatternClass() {
		return patternClass;
	}

	/**
	 * @param patternClass
	 *            the patternClass to set
	 */
	public void setPatternClass(int patternClass) {
		this.patternClass = patternClass;
	}

	/**
	 * @return the ordinal
	 */
	public Integer getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal
	 *            the ordinal to set
	 */
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return the timeField
	 */
	public Integer getTimeField() {
		return timeField;
	}

	/**
	 * @param timeField
	 *            the timeField to set
	 */
	public void setTimeField(Integer timeField) {
		this.timeField = timeField;
	}

	/**
	 * @return the stepCount
	 */
	public Integer getStepCount() {
		return stepCount;
	}

	/**
	 * @param stepCount
	 *            the stepCount to set
	 */
	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}

	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @return the day
	 */
	public Integer getDay() {
		logger.debug("getDay()=" + day);
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(Integer day) {
		logger.debug("setDay(" + day + ")");
		this.day = day;
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
		if (obj instanceof SRL_PRED_PAT_DTL) {
			SRL_PRED_PAT_DTL o = (SRL_PRED_PAT_DTL) obj;
			return o.getPredictionPatternNumber() == this
					.getPredictionPatternNumber()
					&& o.getSequenceNumber() == this.getSequenceNumber();
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
		return persistenceState.getDAO();
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
		return getPredictionPatternNumber() * getSequenceNumber();
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
		// TODO Auto-generated method stub

	}

	/**
	 * @return the monday
	 */
	public boolean isMonday() {
		return monday;
	}

	/**
	 * @param monday
	 *            the monday to set
	 */
	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	/**
	 * @return the tuesday
	 */
	public boolean isTuesday() {
		return tuesday;
	}

	/**
	 * @param tuesday
	 *            the tuesday to set
	 */
	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public boolean isWednesday() {
		return wednesday;
	}

	/**
	 * @param wednesday
	 *            the wednesday to set
	 */
	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * @return the thursday
	 */
	public boolean isThursday() {
		return thursday;
	}

	/**
	 * @param thursday
	 *            the thursday to set
	 */
	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	/**
	 * @return the friday
	 */
	public boolean isFriday() {
		return friday;
	}

	/**
	 * @param friday
	 *            the friday to set
	 */
	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public boolean isSaturday() {
		return saturday;
	}

	/**
	 * @param saturday
	 *            the saturday to set
	 */
	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	/**
	 * @return the sunday
	 */
	public boolean isSunday() {
		return sunday;
	}

	/**
	 * @param sunday
	 *            the sunday to set
	 */
	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	/**
	 * Returns the broader grouping of patternClass (daily, weekly, monthly,
	 * yearly)
	 * 
	 * @return
	 */
	public int getRecurType() {
		switch (getPatternClass()) {
		case 0:
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
		case 4:
			return 3;
		case 5:
		case 6:
			return 4;
		default:
			return 0;
		}
	}

	/**
	 * Validates that the state of this object is suitable for saving to the
	 * database (mandatory fields are present for the given patternClass)
	 * 
	 * @throws ChronologyConfigurationException
	 */
	public void validate() throws ChronologyConfigurationException {
		switch (getPatternClass()) {
		case 0:
			if (getStepCount() == null || getStepCount().intValue() <= 0) {
				throw new ChronologyConfigurationException();
			}
			break;
		case 1:
			break;
		case 2:
			if (getStepCount() == null || getStepCount().intValue() <= 0) {
				throw new ChronologyConfigurationException();
			}
			if (!(isMonday() || isTuesday() || isWednesday() || isThursday()
					|| isFriday() || isSaturday() || isSunday())) {
				throw new ChronologyConfigurationException();
			}
			break;
		case 3:
			if (getDay() == null || getDay().intValue() <= 0) {
				throw new ChronologyConfigurationException();
			}
			break;
		case 4:
			if (getOrdinal() == null) {
				throw new ChronologyConfigurationException();
			}
			if (getTimeField() == null) {
				throw new ChronologyConfigurationException();
			}
			if (getStepCount() == null || getStepCount().intValue() <= 0) {
				throw new ChronologyConfigurationException();
			}
			break;
		case 5:
			if (getMonth() == null) {
				throw new ChronologyConfigurationException();
			}
			if (getDay() == null || getDay().intValue() <= 0 || getDay().intValue() > 31) {
				throw new ChronologyConfigurationException();
			}
			break;
		case 6:
			if (getOrdinal() == null) {
				throw new ChronologyConfigurationException();
			}
			if (getTimeField() == null) {
				throw new ChronologyConfigurationException();
			}
			if (getMonth() == null) {
				throw new ChronologyConfigurationException();
			}
			break;
		default:
			throw new ChronologyConfigurationException();
		}
	}

	public String getPartTypeText(Locale locale) {
		try {
			return new DAOCodeTable().load(T_SRL_PRT_TYP.class, getPartType(),locale).getLongText();
		} catch (DataAccessException e) {
			return "";
		}
	}
}
