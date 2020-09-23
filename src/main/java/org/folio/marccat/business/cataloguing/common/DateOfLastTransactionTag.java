package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public abstract class DateOfLastTransactionTag extends FixedFieldUsingItemEntity {

  private String formatDat = "yyyyMMddHHmmss.S";

  public DateOfLastTransactionTag() {
    super();
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    SimpleDateFormat df = new SimpleDateFormat(formatDat);
    return df.format(getItemEntity().getDateOfLastTransaction());
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
   */
  @Override
  public boolean isEditableHeader() {
    return false;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  @Override
  public boolean isAbleToBeDeleted() {
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DateOfLastTransactionTag) {
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




  @Override
  public void setContentFromMarcString(String s) {
    SimpleDateFormat df = new SimpleDateFormat(formatDat);
    try {
      getItemEntity().setDateOfLastTransaction(df.parse(s));
    } catch (ParseException e) {
      // date not set if parse error
    }
  }
}
