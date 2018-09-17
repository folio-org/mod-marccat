/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicDateOfLastTransactionTag.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.cataloging.business.common.PersistenceState;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicDateOfLastTransactionTag
	extends DateOfLastTransactionTag {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicDateOfLastTransactionTag() {
		super();
		setHeaderType((short)41);
//-->	20100825 inizio: nella modifica del tag non venuva impostato lo stato a modificato del catalogItem  
		setPersistenceState(new PersistenceState());
//-->	20100825 fine
		
	}

}
