/*
 * (c) LibriCore
 * 
 * Created on Nov 2, 2005
 * 
 * DAOAUT.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.RecordNotFoundException;
import librisuite.hibernate.AUT;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class DAOAUT extends HibernateUtil {
//TODO add update to hdg_aut_cnt on add and delete
	public AUT load(int id) throws DataAccessException {
		AUT itm = (AUT) get(AUT.class, new Integer(id));
		if (itm == null) {
			throw new RecordNotFoundException();
		}
		else {
			return itm;
		}
	}
}
