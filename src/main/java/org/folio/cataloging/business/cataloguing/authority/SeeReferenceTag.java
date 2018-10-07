package org.folio.cataloging.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * @author paulm
 * @since 1.0
 */
public class SeeReferenceTag extends SeeSeeAlsoReference {
  private static final Log logger = LogFactory.getLog (SeeReferenceTag.class);

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public SeeReferenceTag() {
    super ( );
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    logger.debug ("does " + v + " affect my key? ");
    if (!super.correlationChangeAffectsKey (v)) {
      logger.debug ("super says no");
      logger.debug ("position is " + getRefTypeCorrelationPosition ( ));
      logger.debug (
        "value is " + v.getValue (getRefTypeCorrelationPosition ( )));
      logger.debug (
        "result is "
          + !ReferenceType.isSeenFrom (
          v.getValue (getRefTypeCorrelationPosition ( ))));
      return !ReferenceType.isSeenFrom (
        v.getValue (getRefTypeCorrelationPosition ( )));
    } else {
      return true;
    }
  }

}
