/*
 * (c) LibriCore
 *
 * Created on Dec 5, 2005
 *
 * TimePeriodTag.java
 */
package org.folio.marccat.business.cataloguing.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.dao.persistence.TimePeriodOfContentTag;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;


public abstract class TimePeriodTag extends VariableHeaderUsingItemEntity {

  protected static final Log logger = LogFactory.getLog(TimePeriodOfContentTag.class);

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public TimePeriodTag(int itemNumber) {
    super(itemNumber);
  }

  public TimePeriodTag() {
    super();
  }
 @Override
  public StringText getStringText() {
    StringText result = null;
    String source = (getItemEntity()).getTimePeriodStringText();

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
      getItemEntity().setTimePeriodStringText(null);
      getItemEntity().setTypeOfDateTimeCode(null);
    } else {
      getItemEntity().setTimePeriodStringText(st.toString());
    }
  }

  public void setHeaderType(short s) {
    super.setHeaderType(s);
    if (getItemEntity() != null) {
      try {
        getItemEntity().setTypeOfDateTimeCode(
          String.valueOf(getMarcEncoding().getMarcFirstIndicator()));
      } catch (Exception e) {
        logger.warn("ErrorCollection setting typeOfDateTimeCode, using null");
        getItemEntity().setTypeOfDateTimeCode(null);
      }
    }
  }

@Override
  public void setCorrelationValues(CorrelationValues v) {
    super.setCorrelationValues(v);
    setHeaderType(v.getValue(1));
  }

}
