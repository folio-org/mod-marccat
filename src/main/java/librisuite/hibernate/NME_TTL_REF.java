/*
 * (c) LibriCore
 * 
 * Created on Dec 12, 2005
 * 
 * NME_TTL_REF.java
 */
package librisuite.hibernate;

import librisuite.business.descriptor.DAODescriptor;
import librisuite.business.descriptor.DAONameTitleDescriptor;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class NME_TTL_REF extends REF {
	/* (non-Javadoc)
	 * @see librisuite.hibernate.REF#getTargetDAO()
	 */
	public DAODescriptor getTargetDAO() {
		return new DAONameTitleDescriptor();
	}

}
