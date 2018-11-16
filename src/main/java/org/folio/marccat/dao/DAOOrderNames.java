package org.folio.marccat.dao;

import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;

import java.util.List;

public class DAOOrderNames extends HibernateUtil {

  private static final Log logger = LogFactory.getLog(DAOOrderNames.class);

  public List getOrderNames(final Session session) throws DataAccessException {
    List list = find(session, "from librisuite.hibernate.OrderNames t");
    return list;
  }

}
