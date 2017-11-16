/*
 * (c) LibriCore
 * 
 * Created on Jun 22, 2004
 * 
 * SBJCT_REF.java
 */
package librisuite.hibernate;

import librisuite.business.descriptor.DAODescriptor;
import librisuite.business.descriptor.DAOSubjectDescriptor;
import librisuite.business.descriptor.DAOThesaurusDescriptor;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class THS_ATRIB extends REF {
	/* (non-Javadoc)
	 * @see librisuite.hibernate.REF#getTargetDAO()
	 */
	public DAODescriptor getTargetDAO() {
		return new DAOThesaurusDescriptor();
	}

}
