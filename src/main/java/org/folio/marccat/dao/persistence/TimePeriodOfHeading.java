package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.TimePeriodTag;
import org.folio.marccat.shared.CorrelationValues;

/**
 * 
 * @author elena
 *
 */
public class TimePeriodOfHeading extends TimePeriodTag {

	private static final long serialVersionUID = 1742343024321572937L;

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

	@Override
	public void setItemEntity(ItemEntity autItm) {
		super.setItemEntity(autItm);
		if (" ".equals(autItm.getTypeOfDateTimeCode())) {
			setHeaderType((short)5);
		}
		else if ("0".equals(autItm.getTypeOfDateTimeCode())) {
			setHeaderType((short)6);
		}
		else if ("1".equals(autItm.getTypeOfDateTimeCode())) {
			setHeaderType((short)7);
		}
		else if ("2".equals(autItm.getTypeOfDateTimeCode())) {
			setHeaderType((short)8);
		}
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1) && ((v.getValue(1) < 5) || (v.getValue(1) > 8));
	}

}
