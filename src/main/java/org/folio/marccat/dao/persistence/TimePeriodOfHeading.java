package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.TimePeriodTag;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class TimePeriodOfHeading extends TimePeriodTag {

	public TimePeriodOfHeading() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 5);
	}

	@Override
	public void setItemEntity(ItemEntity autItem) {
		super.setItemEntity(autItem);
		if (" ".equals(autItem.getTypeOfDateTimeCode())) {
			setHeaderType((short)5);
		}
		else if ("0".equals(autItem.getTypeOfDateTimeCode())) {
			setHeaderType((short)6);
		}
		else if ("1".equals(autItem.getTypeOfDateTimeCode())) {
			setHeaderType((short)7);
		}
		else if ("2".equals(autItem.getTypeOfDateTimeCode())) {
			setHeaderType((short)8);
		}
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1) && ((v.getValue(1) < 5) || (v.getValue(1) > 8));
	}
}
