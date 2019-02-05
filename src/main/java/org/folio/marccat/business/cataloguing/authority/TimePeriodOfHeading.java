package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.TimePeriodTag;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.shared.CorrelationValues;

public class TimePeriodOfHeading extends TimePeriodTag {

  public TimePeriodOfHeading() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 5);
  }

  public void setItemEntity(ItemEntity aut_itm) {
    super.setItemEntity(aut_itm);
    if (" ".equals(aut_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 5);
    } else if ("0".equals(aut_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 6);
    } else if ("1".equals(aut_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 7);
    } else if ("2".equals(aut_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 8);
    }
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1) && ((v.getValue(1) < 5) || (v.getValue(1) > 8));
  }

}
