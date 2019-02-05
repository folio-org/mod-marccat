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


public class CountryOfPublicationTag extends VariableHeaderUsingItemEntity {
  //TODO saveUndoSave doesn't work
  //TODO subfield a should be validated against code table


  public CountryOfPublicationTag() {
    super();
    setHeaderType((short) 6);
  }

  public StringText getStringText() {
    StringText result = null;
    String source = ((BIB_ITM) getItemEntity()).getCountryStringText();

    if (source == null) {
      result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
    } else {
      result = new StringText(source);
    }
    return result;
  }

  public void setStringText(StringText st) {
//TODO need a more definitive way to set to null		
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      ((BIB_ITM) getItemEntity()).setCountryStringText(null);
    } else {
      ((BIB_ITM) getItemEntity()).setCountryStringText(st.toString());
    }
  }

}
