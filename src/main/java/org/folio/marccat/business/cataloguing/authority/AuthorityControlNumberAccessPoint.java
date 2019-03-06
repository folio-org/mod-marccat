package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.dao.persistence.CNTL_NBR;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Collections;
import java.util.List;


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


  public Descriptor getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = (CNTL_NBR) descriptor;
  }

  public StringText getStringText() {
    StringText s = super.getStringText();
    if (getValidationCode() != 'a') {
      s.getSubfield(0).setCode(String.valueOf(getValidationCode()));
    }
    return s;
  }


  public List getFirstCorrelationList() throws DataAccessException {
    return Collections.emptyList();
  }


  private char getValidationCode() {
    return validationCode;
  }


  public void setValidationCode(char c) {
    validationCode = c;
  }


  public void setDescriptorStringText(StringText tagStringText) {
    getDescriptor().setStringText(tagStringText.toString());
  }

}
