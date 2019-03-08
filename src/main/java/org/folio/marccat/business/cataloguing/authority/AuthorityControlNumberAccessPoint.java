package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.dao.persistence.CNTL_NBR;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;


public class AuthorityControlNumberAccessPoint extends AuthorityAccessPoint {
  private CNTL_NBR descriptor = new CNTL_NBR();
  private char validationCode = 'a';


  public AuthorityControlNumberAccessPoint() {
    super();
  }

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityControlNumberAccessPoint(int itemNumber) {
    super(itemNumber);
  }

  /* (non-Javadoc)
   * @see AccessPoint#getAccessPointStringText()
   */
  public StringText getAccessPointStringText() {
    return new StringText();
  }

  /* (non-Javadoc)
   * @see AccessPoint#setAccessPointStringText(org.folio.marccat.util.StringText)
   */
  public void setAccessPointStringText(StringText stringText) {
    // do nothing
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  @Override
  public int getCategory() {
    return 5;
  }

  /* (non-Javadoc)
   * @see TagInterface#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor().getCorrelationValues();
  }

  /* (non-Javadoc)
   * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * @since 1.0
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /* (non-Javadoc)
   * @see Browsable#setDescriptor(librisuite.hibernate.Descriptor)
   */
  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = (CNTL_NBR) descriptor;
  }

  /* (non-Javadoc)
   * @see VariableField#getStringText()
   */
@Override
  public StringText getStringText() {
    StringText s = super.getStringText();
    if (getValidationCode() != 'a') {
      s.getSubfield(0).setCode(String.valueOf(getValidationCode()));
    }
    return s;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
   */

  /**
   * @since 1.0
   */
  public char getValidationCode() {
    return validationCode;
  }

  /**
   * @since 1.0
   */
  public void setValidationCode(char c) {
    validationCode = c;
  }

  /* (non-Javadoc)
   * @see AccessPoint#setDescriptorStringText(org.folio.marccat.util.StringText)
   */
  public void setDescriptorStringText(StringText tagStringText) {
    getDescriptor().setStringText(tagStringText.toString());
  }

}
