/*
 * (c) LibriCore
 *
 * Created on Jan 2, 2006
 *
 * DAOTitleNameTitleReferences.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.dao.persistence.TTL_NME_TTL_REF;
import java.util.List;
import java.util.Objects;


/**
 * Class representing the Cross References for a heading of the name/titles
 *
 * @author paulm
 * @author carment
 */
public class TitleNameTitleReferencesDAO extends CrossReferencesDAO {

  /* (non-Javadoc)
   * @see DAOCrossReferences#loadReciprocal(REF, int)
   */
  public REF loadReciprocal(REF ref, int cataloguingView, final Session session) throws HibernateException {

    final int reciprocalType = ReferenceType.getReciprocal(ref.getType());

    final String queryString;
    if (((TTL_NME_TTL_REF) ref).isSourceTitle()) {
      queryString =
        "from TTL_NME_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? AND "
          + " ref.nameTitleHeadingNumber = ? AND "
          + " ref.sourceHeadingType = 'MH' AND "
          + " substr(ref.userViewString, ?, 1) = '1' AND "
          + " ref.type = ?";

    } else {
      queryString =
        "from TTL_NME_TTL_REF as ref "
          + " where ref.nameTitleHeadingNumber = ? AND "
          + " ref.titleHeadingNumber = ? AND "
          + " ref.sourceHeadingType = 'TH' AND "
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
