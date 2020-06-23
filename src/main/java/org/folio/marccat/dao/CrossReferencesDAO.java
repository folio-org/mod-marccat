package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;

import java.util.List;
import java.util.Objects;

/**
 * Class representing the Cross References for a single heading
 *
 * @author paulm
 * @author carment
 */
public class CrossReferencesDAO extends AbstractDAO {

  /**
   * Save a cross reference.
   *
   * @param p       the cross reference
   * @param session the hibernate session
   * @throws HibernateException in case of data access failure
   */
  @Override
  public void save(final Persistence p, final Session session) throws HibernateException {
    final REF ref = (REF) p;
    final Transaction transaction = getTransaction(session);
    session.save(ref);
    final REF reciprocal = ref.createReciprocal();
    session.save(reciprocal);
    transaction.commit();
  }

  /**
   * Delete a cross reference.
   *
   * @param p       the cross reference
   * @param session the hibernate session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void delete(final Persistence p, final Session session) throws HibernateException {
    final REF ref = (REF) p;
    final REF reciprocal = loadReciprocal(ref, View.toIntView(ref.getUserViewString()), session);
    final Transaction transaction = getTransaction(session);
    session.delete(ref);
    session.delete(reciprocal);
    transaction.commit();

  }

  /**
   * Load reciprocal reference by ref.
   *
   * @param ref             the source
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the ref
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public REF loadReciprocal(final REF ref, final int cataloguingView, final Session session) throws HibernateException {
    final int reciprocalType = ReferenceType.getReciprocal(ref.getType());
    final List<REF> list =
      session.find(
        "from "
          + ref.getClass().getName()
          + " as ref "
          + " where ref.key.target = ? AND "
          + " ref.key.source = ? AND "
          + " substr(ref.key.userViewString, ?, 1) = '1' AND "
          + " ref.key.type = ?",
        new Object[]{
          ref.getSource(),
          ref.getTarget(),
          cataloguingView,
          reciprocalType},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER});
    return list.stream().filter(Objects::nonNull).findFirst().orElse(null);

  }

  /**
   * Load reference by source and target.
   *
   * @param source          the source
   * @param target          the target
   * @param referenceType   the reference type
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the ref
   * @throws HibernateException the hibernate exception
   */
  public REF load(
    final Descriptor source,
    final Descriptor target,
    final short referenceType,
    final int cataloguingView,
    final Session session)
    throws HibernateException {

    return ((DescriptorDAO) source.getDAO()).loadReference(
      source,
      target,
      referenceType,
      cataloguingView, session);
  }
}
