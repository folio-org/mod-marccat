package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.DB_LIST;
import org.folio.cataloging.dao.DAODatabaseViewList;

/**
 * pm 2011
 * Manages code table behaviour for DB_LIST
 * 
 * @author pmouland
 * @since 1.0
 *
 */
public class DatabaseViewList extends CodeListBean {
	/**
	 * Builds a new {@link DatabaseViewList}.
	 */
	public DatabaseViewList() {
		super(DB_LIST.class);
	}

	/**
	 * Returns the DAO associated with this bean.
	 *
	 * @return the DAO associated with this bean.
	 */
	public DAODatabaseViewList getDAO() {
		return new DAODatabaseViewList();
	}
}