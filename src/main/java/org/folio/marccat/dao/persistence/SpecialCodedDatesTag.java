/*
 * (c) LibriCore
 *
 * Created on Oct 25, 2004
 *
 * CataloguingSourceTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;


public class SpecialCodedDatesTag extends VariableHeaderUsingItemEntity {

  public SpecialCodedDatesTag() {
    super();
    setHeaderType((short) 12);
  }

  public StringText getStringText() {
    StringText result;
    String source = ((BIB_ITM) getItemEntity()).getSpecialCodedDatesStringText();

    if (source == null) {
      result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
    } else {
      result = new StringText(source);
    }
    return result;
  }

  public void setStringText(StringText st) {
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      ((BIB_ITM) getItemEntity()).setSpecialCodedDatesStringText(null);
    } else {
      ((BIB_ITM) getItemEntity()).setSpecialCodedDatesStringText(st.toString());
    }
  }

}
