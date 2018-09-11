/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * CataloguingSourceTag.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class CountryOfPublicationTag extends VariableHeaderUsingItemEntity {
	//TODO saveUndoSave doesn't work
	//TODO subfield a should be validated against code table 
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CountryOfPublicationTag() {
		super();
		setHeaderType((short) 6);
	}

	public StringText getStringText() {
		StringText result = null;
		String source = ((BIB_ITM)getItemEntity()).getCountryStringText();
		
		if (source == null) {
			result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
		}
		else {
			result = new StringText(source);
		}
		return result;
	}

	public void setStringText(StringText st) {
//TODO need a more definitive way to set to null		
		if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
			((BIB_ITM)getItemEntity()).setCountryStringText(null);
		}
		else {
			((BIB_ITM)getItemEntity()).setCountryStringText(st.toString());
		}
	}

}
