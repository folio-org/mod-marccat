package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

import java.io.Serializable;

public class SRL_PRED_PAT implements Persistence, Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private int predictionPatternNumber;
  private int amicusNumber;
  private String label;
  private String caption;
  private PersistenceState persistenceState = new PersistenceState ( );

  public SRL_PRED_PAT() {
  }

  public SRL_PRED_PAT(int amicusNumber, int cataloguingView) {
    this.amicusNumber = amicusNumber;
    try {
      Cache cache = (Cache) new HibernateUtil ( )
        .load (Cache.class, new Cache (
          amicusNumber, (short) cataloguingView));
      if (cache != null) {
        this.label = cache.getTitleHeadingMainStringText ( );
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
   * @param predictionPatternNumber the predictionPatternNumber to set
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
   * @param amicusNumber the amicusNumber to set
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
   * @param label the label to set
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
   * @param caption the caption to set
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * @see PersistenceState#cancelChanges()
   */
  public void cancelChanges() {
    persistenceState.cancelChanges ( );
  }

  /**
   * @see PersistenceState#confirmChanges()
   */
  public void confirmChanges() {
    persistenceState.confirmChanges ( );
  }

  /**
   * @param obj
   * @return
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (obj instanceof SRL_PRED_PAT) {
      SRL_PRED_PAT o = (SRL_PRED_PAT) obj;
      return o.getPredictionPatternNumber ( ) == this
        .getPredictionPatternNumber ( );
    } else {
      return false;
    }
  }

  /**
   * @param obj
   * @throws DataAccessException
   * @see PersistenceState#evict(Object)
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict (obj);
  }

  /**
   * @return
   * @see PersistenceState#getDAO()
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO ( );
  }

  /**
   * @return
   * @see PersistenceState#getUpdateStatus()
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus ( );
  }

  /**
   * @param i
   * @see PersistenceState#setUpdateStatus(int)
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus (i);
  }

  /**
   * @return
   * @see Object#hashCode()
   */
  public int hashCode() {
    return getPredictionPatternNumber ( );
  }

  /**
   * @return
   * @see PersistenceState#isChanged()
   */
  public boolean isChanged() {
    return persistenceState.isChanged ( );
  }

  /**
   * @return
   * @see PersistenceState#isDeleted()
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted ( );
  }

  /**
   * @return
   * @see PersistenceState#isNew()
   */
  public boolean isNew() {
    return persistenceState.isNew ( );
  }

  /**
   * @return
   * @see PersistenceState#isRemoved()
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved ( );
  }

  /**
   * @see PersistenceState#markChanged()
   */
  public void markChanged() {
    persistenceState.markChanged ( );
  }

  /**
   * @see PersistenceState#markDeleted()
   */
  public void markDeleted() {
    persistenceState.markDeleted ( );
  }

  /**
   * @see PersistenceState#markNew()
   */
  public void markNew() {
    persistenceState.markNew ( );
  }

  /**
   * @see PersistenceState#markUnchanged()
   */
  public void markUnchanged() {
    persistenceState.markUnchanged ( );
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onDelete(Session)
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete (arg0);
  }

  /**
   * @param arg0
   * @param arg1
   * @see PersistenceState#onLoad(Session,
   * Serializable)
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad (arg0, arg1);
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onSave(Session)
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave (arg0);
  }

  /**
   * @param arg0
   * @return
   * @throws CallbackException
   * @see PersistenceState#onUpdate(Session)
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate (arg0);
  }

  /**
   * @return
   * @see PersistenceState#toString()
   */
  public String toString() {
    return persistenceState.toString ( );
  }

  public void evict() throws DataAccessException {
    persistenceState.evict (this);
  }

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO ( );
    setPredictionPatternNumber (dao.getNextNumber ("EP", session));
  }
}
