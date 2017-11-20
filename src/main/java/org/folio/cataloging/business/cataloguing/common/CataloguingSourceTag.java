/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * CataloguingSourceTag.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;

import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class CataloguingSourceTag extends VariableHeaderUsingItemEntity {
//TODO saveUndoSave doesn't work
//TODO subfields b and e should be validated against code tables 
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CataloguingSourceTag() {
		super();
	}
	
	public StringText getStringText() {
		int bPosition;	
		StringText stringText =
			new StringText(getItemEntity().getCataloguingSourceStringText());
		bPosition = 0;			
		if (stringText.getNumberOfSubfields() > 0) {
			if (((Subfield)stringText.getSubfield(0)).getCode().equals("a")) {
				bPosition = 1;
			}
		}
		String languageOfCataloguing = getItemEntity().getLanguageOfCataloguing().trim();
		if (languageOfCataloguing!=null && !languageOfCataloguing.equals("")&& !stringText.containsSubfield(new Subfield("b", languageOfCataloguing)))
			stringText.addSubfield(bPosition,new Subfield("b", languageOfCataloguing));
		return stringText;
	}	
	
	public void setStringText(StringText st) {
		for (int i = 0; i < st.getSubfieldList().size(); i++) {
			Subfield sf = (Subfield) st.getSubfieldList().get(i);
					
			if (sf!=null && !sf.getContent().trim().equals(""))
				if (sf.getCode().equals("b")) {
					getItemEntity().setLanguageOfCataloguing(sf.getContent());
					//st.removeSubfield(i);
					break;
				}
				else getItemEntity().setLanguageOfCataloguing("   ");
		}
		getItemEntity().setCataloguingSourceStringText(st.toString());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}


}
