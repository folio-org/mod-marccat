/*
 * (c) LibriCore
 * 
 * Created on Dec 3, 2004
 * 
 * DAOBibliographicNote.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOBibliographicNote extends HibernateUtil {

	/* (non-Javadoc)
	 * @see com.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence p)
		throws DataAccessException {
		// TODO handle deletion of overflow
		super.delete(p);
	}

}
