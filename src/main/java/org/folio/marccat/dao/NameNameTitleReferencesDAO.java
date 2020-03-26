package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.NME_NME_TTL_REF;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;

import java.util.List;
import java.util.Objects;

/**
 * Class representing the Cross References for a heading of the name/titles
 *
 * @author paulm
 * @author carment
 */
public class NameNameTitleReferencesDAO extends CrossReferencesDAO {

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
  @Override
  public REF loadReciprocal(final REF ref, final int cataloguingView, final Session session)
    throws HibernateException {
    final int reciprocalType = ReferenceType.getReciprocal(ref.getType());
    final String queryString;
    if (((NME_NME_TTL_REF) ref).isSourceName()) {
      queryString =
        "from NME_NME_TTL_REF as ref "
          + " where ref.nameHeadingNumber = ? AND "
          + " ref.nameTitleHeadingNumber = ? AND "
          + " ref.sourceHeadingType = 'MH' AND "
          + " substr(ref.userViewString, ?, 1) = '1' AND "
          + " ref.type = ?";

    } else {
      queryString =
        "from NME_NME_TTL_REF as ref "
          + " where ref.nameTitleHeadingNumber = ? AND "
          + " ref.nameHeadingNumber = ? AND "
          + " ref.sourceHeadingType = 'NH' AND "
          + " substr(ref.userViewString, ?, 1) = '1' AND "
          + " ref.type = ?";
    }
    List<REF> listRefs =
      session.find(queryString,
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
    return listRefs.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

}
