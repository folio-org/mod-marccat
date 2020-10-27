package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.dao.persistence.BibliographicRelationReciprocal;
import org.folio.marccat.dao.persistence.BibliographicRelationship;
import org.folio.marccat.dao.persistence.BibliographicRelationshipTag;
import org.folio.marccat.dao.persistence.RelationReciprocal;

import java.util.List;

/**
 * Data access object to relation.
 *
 * @author carment
 */

public class BibliographicRelationshipTagDAO extends AbstractDAO {

  /**
   * Delete the tag of relation.
   *
   * @param session the hibernate session
   * @param po the persistent object po
   * @throws HibernateException the hibernate exception
   */
  public void delete(final Session session, final Persistence po) throws HibernateException {
    final BibliographicRelationshipTag aRelation = ((BibliographicRelationshipTag) po).getOriginalTag();
        aRelation.getSourceRelationship().markDeleted();
    super.delete(aRelation.getSourceRelationship(), session);
    if (aRelation.getTargetRelationship() != null) { aRelation.getTargetRelationship().markDeleted();
      super.delete(aRelation.getTargetRelationship(), session);
    }
    aRelation.setUpdateStatus(UpdateStatus.REMOVED);
  }

  /**
   * Save a tag of relation.
   *
   * @param po the persistent object po
   * @param session the hibernate session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void save(final Persistence po, final Session session)  throws HibernateException {
    Transaction tx = getTransaction(session);
    final BibliographicRelationshipTag aRelation = (BibliographicRelationshipTag) po;
    final BibliographicRelationship aTarget =  aRelation.getOriginalTag().getTargetRelationship();
    if (aTarget != null) {
      session.delete(
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
    final BibliographicRelationship aSource = aRelation.getOriginalTag().getSourceRelationship();
    if (aSource != null && !aSource.isNew()) {
      session.delete(
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

    session.flush();
    aRelation.getSourceRelationship().markNew();
    if (BibliographicRelationReciprocal.isBlind(aRelation.getReciprocalOption())) {
      aRelation.getSourceRelationship().generateNewBlindRelationshipKey(session);
    }
    persistByStatus(aRelation.getSourceRelationship(), session);

    if ((aRelation.getTargetRelationship() != null) && (BibliographicRelationReciprocal.isReciprocal(aRelation.getReciprocalOption()))) {
      aRelation.getTargetRelationship().markNew();
      persistByStatus(aRelation.getTargetRelationship(), session);
    }
    aRelation.markUnchanged();
    aRelation.setOriginalTag();
    tx.commit();
  }


  /**
   * Update a tag of ralation
   *
   * @param p the persistent object po
   * @param session the hibernate session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void update(final Persistence p, final Session session) throws HibernateException {
    save(p, session);
  }

  /**
   * Gets the reciprocal type.
   *
   * @param relationTypeCode the relation type code
   * @param session the hibernate session
   * @return the reciprocal type
   * @throws HibernateException the hibernate exception
   */
  public int getReciprocalType(final int relationTypeCode, final Session session) throws HibernateException{
    final List result = session.find(
      "select rcpcl.relationshipReciprocalTypeCode from RelationReciprocal as rcpcl where rcpcl.relationshipTypeCode = ?",
      new Object[]{relationTypeCode},
      new Type[]{Hibernate.INTEGER});
    return !result.isEmpty() ? (Integer)result.get(0) : -1;

  }


}
