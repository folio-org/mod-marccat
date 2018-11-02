/*
 * (c) LibriCore
 *
 * Created on Jan 2, 2014
 *
 * DAONameToTitleReferences.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.persistence.NME_TO_TTL_REF;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class DAONameToTitleReferences extends DAOCrossReferences {

  /* (non-Javadoc)
   * @see DAOCrossReferences#loadReciprocal(REF, int)
   */
  public REF loadReciprocal(REF ref, int cataloguingView)
    throws DataAccessException {

    int reciprocalType = ReferenceType.getReciprocal(ref.getType());

    REF result = null;
    String queryString;
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
    List l =
      find(
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
    if (l.size() == 1) {
      result = (REF) l.get(0);
    }

    return result;
  }

}
