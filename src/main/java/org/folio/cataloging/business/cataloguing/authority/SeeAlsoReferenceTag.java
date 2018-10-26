/*
 * (c) LibriCore
 *
 * Created on Jan 6, 2006
 *
 * SeeAlsoReferenceTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class SeeAlsoReferenceTag extends SeeSeeAlsoReference {
  private REF dualReference = null;

  private short dualReferenceIndicator = 0;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public SeeAlsoReferenceTag() {
    super();
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    if (!super.correlationChangeAffectsKey(v)) {
      return !ReferenceType.isSeeAlsoFrom(
        v.getValue(getRefTypeCorrelationPosition()));
    } else {
      return false;
    }
  }

  /**
   * @since 1.0
   */
  public REF getDualReference() {
    return dualReference;
  }

  /**
   * @since 1.0
   */
  public void setDualReference(REF ref) {
    dualReference = ref;
  }

  /**
   * @since 1.0
   */
  public short getDualReferenceIndicator() {
    return dualReferenceIndicator;
  }

  /**
   * @since 1.0
   */
  public void setDualReferenceIndicator(short s) {
    dualReferenceIndicator = s;
  }

  /* (non-Javadoc)
   * @see SeeSeeAlsoReference#getHasDualIndicator()
   */
  public boolean isHasDualIndicator() {
    return true;
  }

}
