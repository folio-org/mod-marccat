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
public class ProjectedPublicationDateTag extends VariableHeaderUsingItemEntity {
	//TODO subfield a should be validated for yyyymm or - for unknown
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public ProjectedPublicationDateTag() {
		super();
		setHeaderType((short) 38);
	}

	public StringText getStringText() {
		StringText result = null;
		String source = ((BIB_ITM)getItemEntity()).getProjectedPublicationDateCode();
		
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
			((BIB_ITM)getItemEntity()).setProjectedPublicationDateCode(null);
		}
		else {
			((BIB_ITM)getItemEntity()).setProjectedPublicationDateCode(st.toString());
		}
	}

}
