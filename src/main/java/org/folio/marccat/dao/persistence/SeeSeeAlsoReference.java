package org.folio.marccat.dao.persistence;

import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

/**
 * @author elena
 *
 */
public class SeeSeeAlsoReference extends AuthorityReferenceTag{
	private static final String VARIANT_CODES = "weij4";


	  public SeeSeeAlsoReference() {
	    super();
	  }

	  @Override
	  public StringText getStringText() {
	    String subw =
	      ""
	        + getReference().getPrintConstant()
	        + getReference().getAuthorityStructure()
	        + getReference().getEarlierRules()
	        + getReference().getNoteGeneration();
	    StringText result = super.getStringText();
	    result.addSubfield(new Subfield("w", subw));
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
