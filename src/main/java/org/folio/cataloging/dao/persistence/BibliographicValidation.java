/*
 * (c) LibriCore
 * 
 * Created on 28-jul-2004
 * 
 * S_BIB_MARC_TAG_VLDTN.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.Validation;
import org.folio.cataloging.shared.ValidationKey;

import java.io.Serializable;

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
