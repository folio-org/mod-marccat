/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * TimePeriodTag.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.TimePeriodOfContentTag;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class TimePeriodTag extends VariableHeaderUsingItemEntity {

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public TimePeriodTag(int itemNumber) {
		super(itemNumber);
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public TimePeriodTag() {
		super();
	}

	protected static final Log logger = LogFactory.getLog(TimePeriodOfContentTag.class);

	public StringText getStringText() {
		StringText result = null;
		String source = (getItemEntity()).getTimePeriodStringText();
	
		if (source == null) {
			result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
		} else {
			result = new StringText(source);
		}
		return result;
	}

	public void setStringText(StringText st) {
		//TODO need a more definitive way to set to null		
		if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
			getItemEntity().setTimePeriodStringText(null);
			getItemEntity().setTypeOfDateTimeCode(null);
		} else {
			getItemEntity().setTimePeriodStringText(st.toString());
		}
	}

	public void setHeaderType(short s) {
		super.setHeaderType(s);
		if (getItemEntity() != null) {
			try {
				getItemEntity().setTypeOfDateTimeCode(
					String.valueOf(getMarcEncoding().getMarcFirstIndicator()));
			} catch (Exception e) {
				logger.warn("Error setting typeOfDateTimeCode, using null");
				getItemEntity().setTypeOfDateTimeCode(null);
			}
		}
	}


	public void setCorrelationValues(CorrelationValues v) {
		super.setCorrelationValues(v);
		setHeaderType(v.getValue(1));
	}

}
