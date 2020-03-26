package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

/**
 * Class representing the Projected Publication Date Tag
 *
 * @author paulm
 * @author carment
 */
public class ProjectedPublicationDateTag extends VariableHeaderUsingItemEntity {


  public ProjectedPublicationDateTag() {
    super();
    setHeaderType((short) 38);
  }

  @Override
  public StringText getStringText() {
    String source = ((BIB_ITM) getItemEntity()).getProjectedPublicationDateCode();
    if (source == null) {
      return new StringText(Subfield.SUBFIELD_DELIMITER + "a");
    } else {
      return new StringText(source);
    }
  }

  @Override
  public void setStringText(StringText st) {    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      ((BIB_ITM) getItemEntity()).setProjectedPublicationDateCode(null);
    } else {
      ((BIB_ITM) getItemEntity()).setProjectedPublicationDateCode(st.toString());
    }
  }

}
