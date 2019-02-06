package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.shared.CorrelationValues;

public class SeeAlsoReferenceTag extends SeeSeeAlsoReference {
  private REF dualReference = null;

  private short dualReferenceIndicator = 0;


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
