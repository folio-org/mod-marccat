/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * TimePeriodOfHeading.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.business.cataloguing.common.TimePeriodTag;
import org.folio.cataloging.business.common.CorrelationValues;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class TimePeriodOfHeading extends TimePeriodTag {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public TimePeriodOfHeading() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 5);
	}

	public void setItemEntity(ItemEntity aut_itm) {
		super.setItemEntity(aut_itm);
		if (" ".equals(aut_itm.getTypeOfDateTimeCode())) {
			setHeaderType((short)5);
		}
		else if ("0".equals(aut_itm.getTypeOfDateTimeCode())) {
			setHeaderType((short)6);
		}
		else if ("1".equals(aut_itm.getTypeOfDateTimeCode())) {
			setHeaderType((short)7);
		}
		else if ("2".equals(aut_itm.getTypeOfDateTimeCode())) {
			setHeaderType((short)8);
		}
	}

	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1) && ((v.getValue(1) < 5) || (v.getValue(1) > 8));
	}

}
