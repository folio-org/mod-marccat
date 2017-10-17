/*
 * (c) LibriCore
 * 
 * Created on 28-jul-2004
 * 
 * S_BIB_MARC_TAG_VLDTN.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.cataloguing.common.Validation;

/**
 * @author elena
 * @version $Revision: 1.8 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class BibliographicValidation extends Validation implements Serializable {

	private BibliographicValidationKey key;
	public ValidationKey getKey() {
		return key;
	}

	private void setKey(BibliographicValidationKey validation) {
		key = validation;
	}

}
