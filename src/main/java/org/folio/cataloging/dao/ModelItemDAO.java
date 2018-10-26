package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import org.folio.cataloging.dao.persistence.ModelItem;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class for common implementations of ModelItemsDAO (Bib and Auth).
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public abstract class ModelItemDAO extends AbstractDAO {

  /**
   * Delete a model item.
   *
   * @param modelItem the model item
   * @param session   the hibernate session
   * @throws ReferentialIntegrityException the referential integrity exception
   */
  public void delete(final ModelItem modelItem, final Session session) throws HibernateException {
    session.delete(modelItem);
  }

  /**
   * Return a model item by id.
   *
   * @param id      the id of the model item
   * @param session the hibernate session
   * @return a model item.
   * @throws DataAccessException in case of data access failure
   */
  @SuppressWarnings("unchecked")
  public ModelItem load(final int id, final Session session) throws HibernateException {
    final List <ModelItem> list = session.find(
      "from "
        + getPersistentClass().getName()
        + " as itm where itm.item = ? ",
      new Object[]{id},
      new Type[]{Hibernate.LONG});
    return list.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

  protected abstract Class getPersistentClass();

  /**
   * Return true if the given model is used by an item.
   *
   * @param id the id of the model item
   * @return true if the given model is used by an item
   * @throws HibernateException in case of data access failure
   */
  @SuppressWarnings("unchecked")
  public boolean getModelUsage(final int id, final Session session) throws HibernateException {
    final List <Integer> list =
      session.find(
        "select count(*) from "
          + getPersistentClass().getName()
          + " as b"
          + " where b.model.id = ?",
        new Object[]{id},
        new Type[]{Hibernate.INTEGER});

    return list.stream().filter(Objects::nonNull).anyMatch(count -> count > 0);
  }
}
