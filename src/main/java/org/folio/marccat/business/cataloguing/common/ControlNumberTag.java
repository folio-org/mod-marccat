package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.folio.marccat.shared.CorrelationValues;
import java.text.DecimalFormat;


public abstract class ControlNumberTag extends FixedFieldUsingItemEntity {


  public ControlNumberTag() {
    super();
    /*
     * implementers of this class should setHeaderType() in this constructor
     */
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    DecimalFormat df = new DecimalFormat("000000000000");
    String result = null;
    if (getItemEntity().getAmicusNumber() == null) {
      result = df.format(0);
    } else {
      result = df.format(getItemEntity().getAmicusNumber());
    }
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  @Override
  public boolean isAbleToBeDeleted() {
    return false;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
   */
  @Override
  public boolean isEditableHeader() {
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ControlNumberTag) {
      return super.equals(obj);
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1) && (v.getValue(1) != getHeaderType());
  }


}
