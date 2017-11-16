/*
 * (c) LibriCore
 * 
 * Created on Jul 27, 2004
 * 
 * TTL_REF.java
 */
package librisuite.hibernate;

import librisuite.business.descriptor.DAODescriptor;
import librisuite.business.descriptor.DAOTitleDescriptor;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class TTL_REF extends REF {
	/* (non-Javadoc)
	 * @see librisuite.hibernate.REF#getTargetDAO()
	 */
	public DAODescriptor getTargetDAO() {
		return new DAOTitleDescriptor();
	}

}
