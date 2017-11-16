/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * CataloguingSourceTag.java
 */
package librisuite.business.cataloguing.bibliographic;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class SpecialCodedDatesTag extends VariableHeaderUsingItemEntity {
	//TODO saveUndoSave doesn't work
	//TODO subfield a should be validated against code table 
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public SpecialCodedDatesTag() {
		super();
		setHeaderType((short) 12);
	}

	public StringText getStringText() {
		StringText result = null;
		String source = ((BIB_ITM)getItemEntity()).getSpecialCodedDatesStringText();
		
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
			((BIB_ITM)getItemEntity()).setSpecialCodedDatesStringText(null);
		}
		else {
			((BIB_ITM)getItemEntity()).setSpecialCodedDatesStringText(st.toString());
		}
	}

}
