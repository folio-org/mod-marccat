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


public class FormOfMusicalCompositionTag extends VariableHeaderUsingItemEntity {

  public FormOfMusicalCompositionTag() {
    super();
    setHeaderType((short) 13);
  }

  @Override
  public StringText getStringText() {
    StringText result = null;
    String source = ((BIB_ITM) getItemEntity()).getFormOfMusicStringText();

    if (source == null) {
      result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");
    } else {
      result = new StringText(source);
    }
    return result;
  }

  @Override
  public void setStringText(StringText st) {
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      ((BIB_ITM) getItemEntity()).setFormOfMusicStringText(null);
    } else {
      ((BIB_ITM) getItemEntity()).setFormOfMusicStringText(st.toString());
    }
  }

  private void setBibItm(BIB_ITM bib_itm) {
    int sourceCode = -1;
    super.setItemEntity(bib_itm);
    if (((BIB_ITM) getItemEntity()).getFormOfMusicStringText() != null)
      sourceCode = ((BIB_ITM) getItemEntity()).getFormOfMusicStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2");
    if (sourceCode != -1) {
      setHeaderType((short) 53);
    } else {
      setHeaderType((short) 13);
    }
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  @Override
  public void setItemEntity(ItemEntity item) {
    setBibItm((BIB_ITM) item);
  }
}
