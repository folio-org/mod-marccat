package org.folio.marccat.dao.persistence;

import org.apache.commons.validator.GenericValidator;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

/**
 * @author elena
 *
 */
public class SeeSeeAlsoReference extends AuthorityReferenceTag{

  private static final long serialVersionUID = 1L;
  private static final String VARIANT_CODES = "weij4";

	  public SeeSeeAlsoReference() {
	    super();
	  }


  public StringText getSubfieldW() {
    String subw = "" +
        ((getReference().getPrintConstant() == Global.NOT_APLICABLE) ? "" : String.valueOf(getReference().getPrintConstant())) +
        (getReference().getAuthorityStructure() == Global.NOT_APLICABLE ? "" : String.valueOf(getReference().getAuthorityStructure())) +
        (getReference().getEarlierRules() == Global.NOT_APLICABLE ? "" : String.valueOf(getReference().getEarlierRules())) +
        (getReference().getNoteGeneration() == Global.NOT_APLICABLE ? "" : String.valueOf(getReference().getNoteGeneration()));
    StringText result = new StringText();
    if (!GenericValidator.isBlankOrNull(subw)) {
      subw = "" +
          getReference().getPrintConstant() +
          getReference().getAuthorityStructure() +
          getReference().getEarlierRules() +
          getReference().getNoteGeneration();
      result.addSubfield(new Subfield("w", subw));
    }
    return result;
  }

  @Override
  public StringText getStringText() {
    StringText result = getSubfieldW();
    result.parse(getTargetDescriptor().getStringText());
    return result;
  }

  public boolean isHasSubfieldW() {
	    return true;
	  }

	  @Override
	  public String getVariantCodes() {
	    return VARIANT_CODES;
	  }

}
