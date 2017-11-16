/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * AuthorityHeaderFieldHelper.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.common.HeaderFieldHelper;
import librisuite.hibernate.T_AUT_HDR;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class AuthorityHeaderFieldHelper extends HeaderFieldHelper {

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.HeaderField#getCategory()
	 */
	public short getCategory() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.HeaderFieldHelper#getHeaderListClass()
	 */
	public Class getHeaderListClass() {
		return T_AUT_HDR.class;
	}

}
