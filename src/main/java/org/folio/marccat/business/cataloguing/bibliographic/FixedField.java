package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.common.HeaderField;
import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.shared.CorrelationValues;

public abstract class FixedField extends Tag implements HeaderField {
  private HeaderFieldHelper headerField = new BibliographicHeaderFieldHelper();

  protected FixedField() {
  }

  protected FixedField(int itemNumber) {
    super(itemNumber);
  }

  public boolean isBrowsable() {
    return false;
  }

  public abstract String getDisplayString();

  public boolean isFixedField() {
    return true;
  }

  public boolean isEditableHeader() {
    return true;
  }

  public boolean isAbleToBeDeleted() {
    return false; //default implementation
  }


  public HeaderFieldHelper getHeaderField() {
    return headerField;
  }


  public int getCategory() {
    return headerField.getCategory();
  }


  public boolean isHeaderField() {
    return headerField.isHeaderField();
  }


  public void setHeaderField(HeaderFieldHelper helper) {
    headerField = helper;
  }

  @Override
  public CorrelationKey getMarcEncoding(final Session session) {
    CorrelationKey key = super.getMarcEncoding(session);
    return new CorrelationKey(
      key.getMarcTag(),
      ' ',
      ' ',
      key.getMarcTagCategoryCode());
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
   */
  @Override
  public String getRequiredEditPermission() {
    return "editHeader";
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
   * @see librisuite.business.cataloguing.bibliographic.Tag#isWorksheetEditable()
   */
  public boolean isWorksheetEditable() {
    return false;
  }


  public int getHeaderType() {
    return headerField.getHeaderType();
  }


  public void setHeaderType(int s) {
    headerField.setHeaderType(s);
  }

  public void setContentFromMarcString(String s) {
    //default implementation does nothing
  }
}
