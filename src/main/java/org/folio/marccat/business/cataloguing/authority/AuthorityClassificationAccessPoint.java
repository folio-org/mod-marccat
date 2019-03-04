package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.persistence.CLSTN;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.T_AUT_CLSTN_FNCTN;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Iterator;
import java.util.List;

public class AuthorityClassificationAccessPoint extends AuthorityAccessPoint {

  private static final Log logger = LogFactory.getLog(AuthorityClassificationAccessPoint.class);
  private static final String VARIANT_CODES = "d";

  private CLSTN descriptor = new CLSTN();

  private String volumeDate;




  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityClassificationAccessPoint(int itemNumber) {
    super(itemNumber);
  }

  public StringText getAccessPointStringText() {
    return new StringText(getVolumeDate());
  }

  public void setAccessPointStringText(StringText stringText) {
    setVolumeDate(stringText.getSubfieldsWithCodes("d").toString());
  }

  @Override
  public int getCategory() {
    return (short) 6;
  }

  public CorrelationValues getCorrelationValues() {
    CorrelationValues v = getDescriptor().getCorrelationValues();
    return v.change(2, getFunctionCode());
  }

  public void setCorrelationValues(CorrelationValues v) {
    setFunctionCode(v.getValue(2));
    getDescriptor().setCorrelationValues(v);
  }


  public Descriptor getDescriptor() {
    return descriptor;
  }


  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = (CLSTN) descriptor;
  }

  public List getSecondCorrelationList(short value1)
    throws RuntimeException {
    DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
    List l = dao.getSecondCorrelationList(
      getCategory(),
      getHeadingType(),
      value1,
      T_AUT_CLSTN_FNCTN.class);
    Iterator iter = l.iterator();
    logger.debug("cat " + getCategory() + " type " + getHeadingType() + " val1 " + value1);
    while (iter.hasNext()) {
      T_AUT_CLSTN_FNCTN f = (T_AUT_CLSTN_FNCTN) iter.next();
      logger.debug("2nd corr: " + f.getCode() + " -- " + f.getLongText());
    }
    return l;
  }


  @Override
  public StringText getStringText() {
    StringText s = super.getStringText();
    s.parse(getVolumeDate());
    return s;
  }


  private String getVolumeDate() {
    return volumeDate;
  }


  private void setVolumeDate(String string) {
    volumeDate = string;
  }

  public void setDescriptorStringText(StringText tagStringText) {
    getDescriptor().setStringText(
      tagStringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
  }

  @Override
  public String getVariantCodes() {
    return VARIANT_CODES;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
