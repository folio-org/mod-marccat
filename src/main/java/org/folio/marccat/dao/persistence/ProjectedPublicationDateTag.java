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


public class ProjectedPublicationDateTag extends VariableHeaderUsingItemEntity {
  //TODO subfield a should be validated for yyyymm or - for unknown


  public ProjectedPublicationDateTag() {
    super();
    setHeaderType((short) 38);
  }

  public StringText getStringText() {
    StringText result = null;
    String source = "" + ((BIB_ITM) getItemEntity()).getProjectedPublicationDateCode();

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
      ((BIB_ITM) getItemEntity()).setProjectedPublicationDateCode(null);
    } else {
      ((BIB_ITM) getItemEntity()).setProjectedPublicationDateCode(st.toString());
    }
  }

}
