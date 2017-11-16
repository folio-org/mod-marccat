/*
 * (c) LibriCore
 * 
 * Created on Dec 8, 2005
 * 
 * DAOAuthorityReferenceTag.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.hibernate.REF;
import librisuite.hibernate.ReferenceType;
import librisuite.hibernate.T_DUAL_REF;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class DAOAuthorityReferenceTag extends HibernateUtil {

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence p) throws DataAccessException {
		AuthorityReferenceTag t = (AuthorityReferenceTag)p;
		REF ref = t.getReference();
		ref.getDAO().delete(ref);

		/* we do not delete both sides of a dual 
		 * so comment out the following block */
//		if (t.isHasDualIndicator()) {
//			if (T_DUAL_REF.isDual(t.getDualReferenceIndicator())) {
//				REF dualRef = (REF)ref.clone();
//				dualRef.setType(ReferenceType.getReciprocal(ref.getType()));
//				dualRef.getDAO().delete(dualRef);
//			}
//		}
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#save(librisuite.business.common.Persistence)
	 */
	public void save(Persistence p) throws DataAccessException {
		AuthorityReferenceTag t = (AuthorityReferenceTag)p;
		REF ref = t.getReference();
		ref.getDAO().save(ref);
		if (t.isHasDualIndicator()) {
			if (T_DUAL_REF.isDual(t.getDualReferenceIndicator())) {
				REF dualRef = (REF)ref.clone();
				dualRef.setType(ReferenceType.getReciprocal(ref.getType()));
				dualRef.getDAO().save(dualRef);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#update(librisuite.business.common.Persistence)
	 */
	public void update(Persistence p) throws DataAccessException {
		delete(p);
		save(p);
	}

}
