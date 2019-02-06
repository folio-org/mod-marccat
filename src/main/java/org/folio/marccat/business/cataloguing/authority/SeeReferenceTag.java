package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.shared.CorrelationValues;

public class SeeReferenceTag extends SeeSeeAlsoReference {
  private static final Log logger = LogFactory.getLog(SeeReferenceTag.class);


  public SeeReferenceTag() {
    super();
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    logger.debug("does " + v + " affect my key? ");
    if (!super.correlationChangeAffectsKey(v)) {
      logger.debug("super says no");
      logger.debug("position is " + getRefTypeCorrelationPosition());
      logger.debug(
        "value is " + v.getValue(getRefTypeCorrelationPosition()));
      logger.debug(
        "result is "
          + !ReferenceType.isSeenFrom(
          v.getValue(getRefTypeCorrelationPosition())));
      return !ReferenceType.isSeenFrom(
        v.getValue(getRefTypeCorrelationPosition()));
    } else {
      return true;
    }
  }

}
