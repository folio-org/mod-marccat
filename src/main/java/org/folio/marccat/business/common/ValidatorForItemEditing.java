/*
 * (c) LibriCore
 *
 * Created on Jun 15, 2005
 *
 * BibliographicValidatorForItemEditing.java
 */
package org.folio.marccat.business.common;

import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.dao.persistence.BibliographicRelationshipTag;
import org.folio.marccat.util.StringText;

import java.util.Set;

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
   * @see librisuite.business.common.Validator#computeRemainingValidSubfields(VariableField)
   */
  public Set computeRemainingValidSubfields(VariableField f)
    throws DataAccessException {
    Set remaining = super.computeRemainingValidSubfields(f);

    if (f instanceof Browsable) {
      remaining.retainAll(((Browsable) f).getValidEditableSubfields());
    } else if (f instanceof BibliographicRelationshipTag) {
      remaining.retainAll(((BibliographicRelationshipTag) f).getValidEditableSubfields());
    }
    return remaining;
  }

}
