package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.NME_TO_TTL_REF;
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
public class NameToTitleReferencesDAO extends CrossReferencesDAO {

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
  private REF loadReciprocal(final REF ref, final int cataloguingView, final Session session) throws HibernateException {

    final int reciprocalType = ReferenceType.getReciprocal(ref.getType());
    final String queryString;
    if (((NME_TO_TTL_REF) ref).isSourceName()) {
      queryString =
        "from NME_TO_TTL_REF as ref "
          + " where ref.nameHeadingNumber = ? AND "
          + " ref.titleHeadingNumber = ? AND "
          + " substr(ref.userViewString, ?, 1) = '1' AND "
          + " ref.type = ?";

    } else {
      queryString =
        "from NME_NME_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? AND "
          + " ref.nameHeadingNumber = ? AND "
          + " substr(ref.userViewString, ?, 1) = '1' AND "
          + " ref.type = ?";
    }
    List<REF> listRefs =
      session.find(
        queryString,
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
