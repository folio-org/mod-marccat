/*
 * (c) LibriCore
 *
 * Created on Jul 28, 2005
 *
 * DAOSortCriteriaDetails.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/28 11:41:23 $
 * @since 1.0
 */
public class DAOSortCriteriaDetails extends HibernateUtil {

  public List getDetails(int sortCriteria) throws DataAccessException {
    return find(
      "from SortCriteriaDetails as c "
        + " where c.code = ? order by c.sequence",
      new Object[]{new Integer(sortCriteria)},
      new Type[]{Hibernate.INTEGER});
  }
}
