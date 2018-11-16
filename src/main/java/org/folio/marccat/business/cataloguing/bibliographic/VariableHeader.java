/*
 * (c) LibriCore
 *
 * Created on Nov 4, 2004
 *
 * VariableHeader.java
 */
package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.HeaderField;
import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public abstract class VariableHeader extends VariableField implements HeaderField {
  private HeaderFieldHelper headerField = new BibliographicHeaderFieldHelper();

  public VariableHeader(int itemNumber) {
    super(itemNumber);
  }


  public VariableHeader() {
    super();
  }

  /* (non-Javadoc)
   * @see VariableField#getStringText()
   */
  public StringText getStringText() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see VariableField#setStringText(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    // TODO Auto-generated method stub

  }

  public String getDisplayString() {
    return getStringText().getMarcDisplayString("$");
  }

  /**
   * @since 1.0
   */
  public HeaderFieldHelper getHeaderField() {
    return headerField;
  }

  /**
   * @since 1.0
   */
  public int getHeaderType() {
    return headerField.getHeaderType();
  }

  /**
   * @since 1.0
   */
  public void setHeaderType(int s) {
    headerField.setHeaderType(s);
  }

  /* (non-Javadoc)
   * @see VariableField#getCategory()
   */
  public int getCategory() {
    return headerField.getCategory();
  }

  /* (non-Javadoc)
   * @see VariableField#getFirstCorrelationList()
   */
  public List getFirstCorrelationList() throws DataAccessException {
    return headerField.getFirstCorrelationList();
  }

  /* (non-Javadoc)
   * @see VariableField#getSecondCorrelationList(short)
   */
  public List getSecondCorrelationList(int value1)
    throws DataAccessException {
    return headerField.getSecondCorrelationList(value1);
  }

  /* (non-Javadoc)
   * @see VariableField#getThirdCorrelationList(short, short)
   */
  public List getThirdCorrelationList(int value1, int value2)
    throws DataAccessException {
    return headerField.getThirdCorrelationList(value1, value2);
  }

  /* (non-Javadoc)
   * @see VariableField#isHeaderField()
   */
  public boolean isHeaderField() {
    return headerField.isHeaderField();
  }

  /**
   * @since 1.0
   */
  public void setHeaderField(HeaderFieldHelper helper) {
    headerField = helper;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return headerField.getCorrelationValues();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    headerField.setCorrelationValues(v);
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
   */
  public String getRequiredEditPermission() {
    return "editHeader";
  }

}
