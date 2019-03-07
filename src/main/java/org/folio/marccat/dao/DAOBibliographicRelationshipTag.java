/*
 * (c) LibriCore
 *
 * Created on Dec 22, 2004
 *
 * DAOPublisherTag.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.BibliographicRelationReciprocal;
import org.folio.marccat.dao.persistence.BibliographicRelationship;
import org.folio.marccat.dao.persistence.BibliographicRelationshipTag;
import org.folio.marccat.exception.DataAccessException;

import java.util.List;

/**
 * @author hansv
 * @version $Revision: 1.6 $ $Date: 2007/04/09 09:58:07 $
 * @since 1.0
 */
public class DAOBibliographicRelationshipTag extends AbstractDAO {
  private static final Log logger =
    LogFactory.getLog(BibliographicRelationshipTag.class);

  /* (non-Javadoc)
   * @see HibernateUtil#delete(librisuite.business.common.Persistence)
   */
  public void delete(final Session session, final Persistence po) throws DataAccessException {
    if (!(po instanceof BibliographicRelationshipTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicRelationshipTag objects");
    }
    /*
     * If we want to delete a relationship tag, it is the last saved values that
     * need to be deleted from the database (not the current values).  Hence we
     * get the originalTag values from the tag.
     */
    BibliographicRelationshipTag aRelation =
      ((BibliographicRelationshipTag) po).getOriginalTag();
    evictAny(session, aRelation.getSourceRelationship());
    aRelation.getSourceRelationship().markDeleted();
    super.delete(aRelation.getSourceRelationship());
    if (aRelation.getTargetRelationship() != null) {
      evictAny(session, aRelation.getTargetRelationship());
      aRelation.getTargetRelationship().markDeleted();
      super.delete(aRelation.getTargetRelationship());
    }

    aRelation.setUpdateStatus(UpdateStatus.REMOVED);
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  @Override
  public void save(final Persistence po, final Session session) throws DataAccessException {
    if (!(po instanceof BibliographicRelationshipTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicRelationshipTag objects");
    }
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException, DataAccessException {
        BibliographicRelationshipTag aRelation =
          (BibliographicRelationshipTag) po;


        logger.debug("save bibliographicRelationshipTag");
        logger.debug("reciprocalOption is " + aRelation.getReciprocalOption());
        logger.debug("target is " + aRelation.getTargetRelationship() == null ? "" : "not " + "null");

        /* first evict all objects from Hibernate cache
         * Then delete any pre-existing RLTSP rows
         */
        BibliographicRelationship aTarget =
          aRelation.getOriginalTag().getTargetRelationship();
        if (aTarget != null) {
          s.delete(
            "from BibliographicRelationship as rltsp "
              + " where rltsp.bibItemNumber = ? "
              + " and rltsp.targetBibItemNumber = ? "
              + " and rltsp.relationTypeCode = ? "
              + " and rltsp.userViewString = ? ",
            new Object[]{
              aTarget.getBibItemNumber(),
              aTarget.getTargetBibItemNumber(),
              aTarget.getRelationTypeCode(),
              aTarget.getUserViewString()},
            new Type[]{
              Hibernate.INTEGER,
              Hibernate.INTEGER,
              Hibernate.SHORT,
              Hibernate.STRING});
        }
        BibliographicRelationship aSource =
          aRelation.getOriginalTag().getSourceRelationship();
        if (aSource != null && !aSource.isNew()) {
          s.delete(
            "from BibliographicRelationship as rltsp "
              + " where rltsp.bibItemNumber = ? "
              + " and rltsp.targetBibItemNumber = ? "
              + " and rltsp.relationTypeCode = ? "
              + " and rltsp.userViewString = ? ",
            new Object[]{
              aSource.getBibItemNumber(),
              aSource.getTargetBibItemNumber(),
              aSource.getRelationTypeCode(),
              aSource.getUserViewString()},
            new Type[]{
              Hibernate.INTEGER,
              Hibernate.INTEGER,
              Hibernate.INTEGER,
              Hibernate.STRING});
        }

        /* now flush the session to ensure that deletes are completed
         * otherwise, it seems, duplicate key will be raised
         */

        s.flush();

        /* now add the current values as new rows */

        aRelation.getSourceRelationship().markNew();
        if (BibliographicRelationReciprocal.isBlind(aRelation.getReciprocalOption())) {
          logger.debug("Reciprocal option is blind -- generating new key");
          aRelation.getSourceRelationship().generateNewBlindRelationshipKey(s);
        } else {
          logger.debug("Reciprocal option is not blind -- using existing target bib_itm_nbr");
        }
        persistByStatus(aRelation.getSourceRelationship());

        if ((aRelation.getTargetRelationship() != null)
          && (BibliographicRelationReciprocal.isReciprocal(aRelation.getReciprocalOption()))) {
          aRelation.getTargetRelationship().markNew();
          persistByStatus(aRelation.getTargetRelationship());
        }
        aRelation.markUnchanged();
        aRelation.setOriginalTag();
      }
    }
      .execute();

  }

  /* (non-Javadoc)
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  @Override
  public void update(Persistence p) throws DataAccessException {
    /*
     * Since we are deleting and re-adding, save and update are the same
     */
    logger.debug("update bibliographicRelationshipTag");

    save(p);
  }

  public short getReciprocalType(int relationTypeCode)
    throws DataAccessException {

    try {
      List result =
        currentSession().find(
          "select rcpcl.relationshipReciprocalTypeCode from RelationReciprocal rcpcl where rcpcl.relationshipTypeCode = ?",
          new Object[]{relationTypeCode},
          new Type[]{Hibernate.INTEGER});
      return ((Integer) result.get(0)).shortValue();
    } catch (HibernateException he) {
      logAndWrap(he);
    }

    return -1;
  }

  private void evictAny(final Session session, final BibliographicRelationship aRelation) throws DataAccessException {
    BibliographicRelationship inCache = (BibliographicRelationship)
      get(session, BibliographicRelationship.class, aRelation);
    if (inCache != null) {
      inCache.evict();
    }
  }
}
