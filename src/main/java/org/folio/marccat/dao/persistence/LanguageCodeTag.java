package org.folio.marccat.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

public class LanguageCodeTag extends VariableHeaderUsingItemEntity {
  private static final long serialVersionUID = -8659240725828529186L;

  private static final Log logger = LogFactory.getLog(LanguageCodeTag.class);

  public LanguageCodeTag() {
    super();
    setHeaderType((short) 2);
  }

  @Override
  public StringText getStringText() {
    StringText result = null;
    String source = ((BIB_ITM) getItemEntity()).getLanguageStringText();

    if (source == null) {
      /* Bug 4115 inizio modifiche Carmen */
      result = new StringText(Subfield.SUBFIELD_DELIMITER + "a" + ((BIB_ITM) getItemEntity()).getLanguageCode());
      /* Bug 4115 fine modifiche Carmen */
    } else {
      result = new StringText(source);
    }
    return result;
  }

@Override  
  public void setStringText(StringText st) {
    if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
      ((BIB_ITM) getItemEntity()).setLanguageStringText(null);
      ((BIB_ITM) getItemEntity()).setTranslationCode(null);
    } else {
      ((BIB_ITM) getItemEntity()).setLanguageStringText(st.toString());
    }
  }

  /* (non-Javadoc)
   * @see VariableHeader#setBibHeader(short)
   */
  public void setHeaderType(short s) {
    super.setHeaderType(s);
    if (getItemEntity() != null) {
      try {
        ((BIB_ITM) getItemEntity()).setTranslationCode(String.valueOf(getMarcEncoding().getMarcFirstIndicator()));
      } catch (Exception e) {
        logger.warn("ErrorCollection setting bib_itm.translationCode, using null");
        ((BIB_ITM) getItemEntity()).setTranslationCode(null);
      }
    }
  }

  private void setBibItm(BIB_ITM bib_itm) {
    super.setItemEntity(bib_itm);
    if ("0".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") == -1) {
      setHeaderType((short) 2);
    } else if ("1".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") == -1) {
      setHeaderType((short) 3);
    } else if (" ".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") == -1) {
      setHeaderType((short) 49);
    } else if ("0".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") != -1) {
      setHeaderType((short) 50);
    } else if ("1".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") != -1) {
      setHeaderType((short) 52);
    } else if (" ".equals(bib_itm.getTranslationCode()) && ((BIB_ITM) getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2") != -1) {
      setHeaderType((short) 51);
    }
    /* Bug 4115 fine modifiche Carmen */
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    /* Bug 4115 inizio modifiche Carmen */
    return v.isValueDefined(1) && (v.getValue(1) != 2) && (v.getValue(1) != 3) && (v.getValue(1) != 49) && (v.getValue(1) != 50) && (v.getValue(1) != 51) && (v.getValue(1) != 52);
    /* Bug 4115 fine modifiche Carmen */
  }


  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  @Override
  public void setCorrelationValues(CorrelationValues v) {
    super.setCorrelationValues(v);
    setHeaderType(v.getValue(1));
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  @Override
  public void setItemEntity(ItemEntity item) {
    setBibItm((BIB_ITM) item);
  }
}
