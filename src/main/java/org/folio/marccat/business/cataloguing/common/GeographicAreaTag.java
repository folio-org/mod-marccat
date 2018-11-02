/*
 * (c) LibriCore
 *
 * Created on Oct 25, 2004
 *
 * CataloguingSourceTag.java
 */
package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class GeographicAreaTag extends VariableHeaderUsingItemEntity {
  //TODO saveUndoSave doesn't work
  //TODO subfield a should be validated against code table

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public GeographicAreaTag() {
    super();
  }

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

  public void setStringText(StringText st) {
//TODO need a more definitive way to set to null		
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      getItemEntity().setGeographicAreaStringText(null);
    } else {
      for (int i = 0; i < st.getSubfieldList().size(); i++) {
        Subfield sf = (Subfield) st.getSubfieldList().get(i);
        if (sf != null && !sf.getContent().trim().equals(""))
          if (sf.getCode().equals("a")) {
            if (sf.getContent().length() < 7)
              throw new IllegalArgumentException("Refactor => GeographicAreaTag::59");
            // sf.setContent(ViolinStrings.Strings.leftJustify(sf.getContent(), 7, '-'));
          }
      }

      getItemEntity().setGeographicAreaStringText(st.toString());
    }
  }

}
