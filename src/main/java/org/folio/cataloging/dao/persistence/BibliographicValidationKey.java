/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * BibliographicValidationKey.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.ValidationKey;

/**
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class BibliographicValidationKey extends ValidationKey {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicValidationKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param marcTag
	 * @param marcTagCategory
	 * @since 1.0
	 */
	public BibliographicValidationKey(String marcTag, short marcTagCategory) {
		super(marcTag, marcTagCategory);
		// TODO Auto-generated constructor stub
	}

}
