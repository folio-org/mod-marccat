package org.folio.cataloging.dao;

import java.util.List;

import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;

public class DAOOrderNames extends HibernateUtil {

	private static final Log logger = LogFactory.getLog(DAOOrderNames.class);

	public List getOrderNames(final Session session) throws DataAccessException {
		List list = find(session, "from librisuite.hibernate.OrderNames t");
		return list;
	}

}