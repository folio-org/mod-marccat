/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * CataloguingSourceTag.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class AuthenticationCodeTag extends VariableHeaderUsingItemEntity {
	//TODO saveUndoSave doesn't work
	//TODO subfield a should be validated against code table 
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthenticationCodeTag() {
		super();
	}

	public StringText getStringText() {
		StringText result = null;
		String source = getItemEntity().getAuthenticationCenterStringText();
		
		if (source == null) {
			result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
		}
		else {
			result = new StringText(source);
		}
		return result;
	}

	public void setStringText(StringText st) {
		if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
			getItemEntity().setAuthenticationCenterStringText(null);
		}
		else {
			getItemEntity().setAuthenticationCenterStringText(st.toString());
		}
	}
	
}
