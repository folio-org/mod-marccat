package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;


public abstract class GeographicAreaTag extends VariableHeaderUsingItemEntity {

  public GeographicAreaTag() {
    super();
  }

  @Override
  public StringText getStringText() {
    StringText result = null;
    String source = getItemEntity().getGeographicAreaStringText();

    if (source == null) {
      result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
    } else {

      result = new StringText(source);
    }
    return result;
  }

  @Override
  public void setStringText(StringText st) {
//TODO need a more definitive way to set to null		
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      getItemEntity().setGeographicAreaStringText(null);
    } else {
      for (int i = 0; i < st.getSubfieldList().size(); i++) {
        Subfield sf = (Subfield) st.getSubfieldList().get(i);
            if (sf != null && !sf.getContent().trim().equals("") && sf.getCode().equals("a") && sf.getContent().length() < 7)
              throw new IllegalArgumentException("Refactor => GeographicAreaTag::59");
      }

      getItemEntity().setGeographicAreaStringText(st.toString());
    }
  }

}
