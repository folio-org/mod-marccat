/*
 * (c) LibriCore
 *
 * Created on Dec 5, 2005
 *
 * AuthorityClassificationAccessPoint.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.Defaults;
import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.persistence.CLSTN;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.T_AUT_CLSTN_FNCTN;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Iterator;
import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/04/27 12:56:53 $
 * @since 1.0
 */
public class AuthorityClassificationAccessPoint extends AuthorityAccessPoint {

  private static final Log logger = LogFactory.getLog(AuthorityClassificationAccessPoint.class);
  private static final String VARIANT_CODES = "d";

  private CLSTN descriptor = new CLSTN();

  private String volumeDate;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityClassificationAccessPoint() {
    super();
    descriptor.setTypeCode(Defaults.getShort("authority.classification.type"));
    setFunctionCode(Defaults.getShort("authority.classification.function"));
  }

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityClassificationAccessPoint(int itemNumber) {
    super(itemNumber);
    descriptor.setTypeCode(Defaults.getShort("authority.classification.type"));
    setFunctionCode(Defaults.getShort("authority.classification.function"));
  }

  /* (non-Javadoc)
   * @see AccessPoint#getAccessPointStringText()
   */
  public StringText getAccessPointStringText() {
    return new StringText(getVolumeDate());
  }

  /* (non-Javadoc)
   * @see AccessPoint#setAccessPointStringText(org.folio.marccat.util.StringText)
   */
  public void setAccessPointStringText(StringText stringText) {
    setVolumeDate(stringText.getSubfieldsWithCodes("d").toString());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return (short) 6;
  }

  /* (non-Javadoc)
   * @see TagInterface#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    CorrelationValues v = getDescriptor().getCorrelationValues();
    return v.change(2, getFunctionCode());
  }

  /* (non-Javadoc)
   * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setFunctionCode(v.getValue(2));
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * @since 1.0
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /* (non-Javadoc)
   * @see Browsable#setDescriptor(librisuite.hibernate.Descriptor)
   */
  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = (CLSTN) descriptor;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
   */
  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
    //return getDaoCodeTable().getList(T_AUT_CLSTN_TYP.class,false);
    return null;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short)
   */
  public List getSecondCorrelationList(short value1)
    throws DataAccessException {
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

  /* (non-Javadoc)
   * @see VariableField#getStringText()
   */
  public StringText getStringText() {
    StringText s = super.getStringText();
    s.parse(getVolumeDate());
    return s;
  }

  /**
   * @since 1.0
   */
  public String getVolumeDate() {
    return volumeDate;
  }

  /**
   * @since 1.0
   */
  public void setVolumeDate(String string) {
    volumeDate = string;
  }

  /* (non-Javadoc)
   * @see AccessPoint#setDescriptorStringText(org.folio.marccat.util.StringText)
   */
  public void setDescriptorStringText(StringText tagStringText) {
    getDescriptor().setStringText(
      tagStringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
  }

  public String getVariantCodes() {
    return VARIANT_CODES;
  }

}
