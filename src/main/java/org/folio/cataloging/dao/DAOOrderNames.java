package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;

public class DAOOrderNames extends HibernateUtil {

	private static final Log logger = LogFactory.getLog(DAOOrderNames.class);

	public List getOrderNames() throws DataAccessException {
		List list = find("from librisuite.hibernate.OrderNames t");
		return list;
	}

}