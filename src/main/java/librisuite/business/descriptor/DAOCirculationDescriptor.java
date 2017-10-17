/*
 * (c) LibriCore
 * 
 * Created on 02-feb-2005
 * 
 * DAOCirculationDescriptor.java
 */
package librisuite.business.descriptor;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.SortFormException;
import librisuite.hibernate.PRSN;


/**
 * @author Elena
 * @version $Revision: 1.3 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class DAOCirculationDescriptor extends DAODescriptor {
	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return PRSN.class;
	}
	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getMatchingHeading(librisuite.hibernate.Descriptor)
	 */
	public Descriptor getMatchingHeading(Descriptor d)
		throws DataAccessException, SortFormException {
		// TODO implement matching
		return null;
	}
}	