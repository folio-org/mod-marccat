/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2005
 * 
 * BibliographicValidatorForItemEditing.java
 */
package librisuite.business.common;

import java.util.Set;

import librisuite.business.cataloguing.bibliographic.BibliographicRelationshipTag;
import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.cataloguing.common.Browsable;

import com.libricore.librisuite.common.StringText;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2007/04/09 09:58:07 $
 * @since 1.0
 */
public class ValidatorForItemEditing
	extends Validator {

	public StringText getEditableSubfields(VariableField field) {
		if (field instanceof Browsable) {
			return ((Browsable) field).getEditableSubfields();
		} else {
			return field.getStringText();
		}
	}

	public StringText getFixedSubfields(VariableField field) {

		if (field instanceof Browsable) {
			return new StringText(
				((Browsable) field).getDescriptor().getStringText());
		} else {
			return new StringText();
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Validator#computeRemainingValidSubfields(librisuite.business.cataloguing.bibliographic.VariableField)
	 */
	public Set computeRemainingValidSubfields(VariableField f)
		throws MarcCorrelationException, DataAccessException {
		Set remaining = super.computeRemainingValidSubfields(f);

		if (f instanceof Browsable) {
			remaining.retainAll(((Browsable)f).getValidEditableSubfields());
		}
		else if (f instanceof BibliographicRelationshipTag) {
			remaining.retainAll(((BibliographicRelationshipTag)f).getValidEditableSubfields());
		}
		return remaining;
	}

}
