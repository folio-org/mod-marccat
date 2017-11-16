/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2005
 * 
 * DAOAuthorityModel.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.common.DAOModel;
import librisuite.business.cataloguing.common.DAOModelItem;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class DAOAuthorityModel extends DAOModel {
	private static final DAOAuthorityModelItem theItemDAO = new DAOAuthorityModelItem();

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.DAOModel#getModelItemDAO()
	 */
	protected DAOModelItem getModelItemDAO() {
		return theItemDAO;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.DAOModel#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModel.class;
	}

}
