package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.NME_NME_TTL_REF;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;

import java.util.List;

/**
 * @author paulm
 * @since 1.0
 */
public class DAONameNameTitleReferences extends DAOCrossReferences {

  /* (non-Javadoc)
   * @see DAOCrossReferences#loadReciprocal(REF, int)
   */
  public REF loadReciprocal(REF ref, int cataloguingView)
    throws DataAccessException {

    int reciprocalType = ReferenceType.getReciprocal (ref.getType ( ));

    REF result = null;
    String queryString;
    if (((NME_NME_TTL_REF) ref).isSourceName ( )) {
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
    List l =
      find (
        queryString,
        new Object[]{
          ref.getSource ( ),
          ref.getTarget ( ),
          cataloguingView,
          reciprocalType},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER});
    if (l.size ( ) == 1) {
      result = (REF) l.get (0);
    }

    return result;
  }

}
