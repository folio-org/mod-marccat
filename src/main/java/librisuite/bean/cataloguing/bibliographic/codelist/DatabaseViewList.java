package librisuite.bean.cataloguing.bibliographic.codelist;

import java.util.List;
import java.util.Locale;

import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.DB_LIST;
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
	 * Class constructor
	 *
	 * @since 1.0
	 */
	public DatabaseViewList() {
		super(DB_LIST.class);
	}
	/* (non-Javadoc)
	 * @see librisuite.bean.cataloguing.bibliographic.codelist.CodeListBean#getDAO()
	 */
	public DAODatabaseViewList getDAO() {
		return new DAODatabaseViewList();
	}


}
