/*
 * (c) LibriCore
 *
 * Created on Oct 25, 2004
 *
 * CataloguingSourceTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.TimePeriodTag;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class TimePeriodOfContentTag extends TimePeriodTag {
  /**
   * Class constructor
   *
   * @since 1.0
   */
  public TimePeriodOfContentTag() {
    super();
    setHeaderType((short) 8);
  }

  public void setItemEntity(ItemEntity bib_itm) {
    super.setItemEntity(bib_itm);
    if (" ".equals(bib_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 8);
    } else if ("0".equals(bib_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 9);
    } else if ("1".equals(bib_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 10);
    } else if ("2".equals(bib_itm.getTypeOfDateTimeCode())) {
      setHeaderType((short) 11);
    }
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1) && ((v.getValue(1) < 8) || (v.getValue(1) > 11));
  }

}
