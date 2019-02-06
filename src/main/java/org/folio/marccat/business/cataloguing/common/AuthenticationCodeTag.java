package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;


public abstract class AuthenticationCodeTag extends VariableHeaderUsingItemEntity {


  public AuthenticationCodeTag() {
    super();
  }

  public StringText getStringText() {
    StringText result;
    String source = getItemEntity().getAuthenticationCenterStringText();

    if (source == null) {
      result = new StringText(Subfield.SUBFIELD_DELIMITER.concat("a"));
    } else {
      result = new StringText(source);
    }
    return result;
  }

  public void setStringText(StringText st) {
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER.concat("a"))) {
      getItemEntity().setAuthenticationCenterStringText(null);
    } else {
      getItemEntity().setAuthenticationCenterStringText(st.toString());
    }
  }

}
