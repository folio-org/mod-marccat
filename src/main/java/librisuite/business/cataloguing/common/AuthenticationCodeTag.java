/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * CataloguingSourceTag.java
 */
package librisuite.business.cataloguing.common;

import librisuite.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

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
//TODO need a more definitive way to set to null		
		if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
			getItemEntity().setAuthenticationCenterStringText(null);
		}
		else {
			getItemEntity().setAuthenticationCenterStringText(st.toString());
		}
	}
	
}
