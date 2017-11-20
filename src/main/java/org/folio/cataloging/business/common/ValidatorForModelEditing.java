/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2005
 * 
 * BibliographicValidatorForModelEditing.java
 */
package org.folio.cataloging.business.common;

import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;

import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class ValidatorForModelEditing
	extends Validator {

	public StringText getEditableSubfields(VariableField field) {
			return field.getStringText();
	}

	public StringText getFixedSubfields(VariableField field) {	
			return new StringText();
	}

}
