/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicCataloguingSourceTag.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;
import org.folio.cataloging.business.common.PersistenceState;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicCataloguingSourceTag extends CataloguingSourceTag {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicCataloguingSourceTag() {
		super();
		setHeaderType((short) 1);
//-->	20100825 inizio: nella modifica del tag non venuva impostato lo stato a modificato del catalogItem  
		setPersistenceState(new PersistenceState());
//-->	20100825 fine
	}

}
