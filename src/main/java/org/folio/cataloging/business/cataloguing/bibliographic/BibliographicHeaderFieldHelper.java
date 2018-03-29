/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicHeaderFieldHelper.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.HeaderFieldHelper;
import org.folio.cataloging.dao.persistence.T_BIB_HDR;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicHeaderFieldHelper extends HeaderFieldHelper {

	/* (non-Javadoc)
	 * @see HeaderField#getCategory()
	 */
	public int getCategory() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see HeaderFieldHelper#getHeaderListClass()
	 */
	public Class getHeaderListClass() {
		return T_BIB_HDR.class;
	}

}
