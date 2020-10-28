package org.folio.marccat.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class SeeReferenceTag extends SeeSeeAlsoReference {
  private static final Log logger = LogFactory.getLog(SeeReferenceTag.class);

  public SeeReferenceTag() {
    super();
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    logger.debug("does " + v + " affect my key? ");
    if (!super.correlationChangeAffectsKey(v)) {
      return !ReferenceType.isSeenFrom(v.getValue(getRefTypeCorrelationPosition()));
    } else {
      return true;
    }
  }
}
