package org.folio.marccat.business.cataloguing.authority;

import java.util.List;

import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.persistence.NME_HDG;
import org.folio.marccat.dao.persistence.NameSubType;
import org.folio.marccat.shared.CorrelationValues;


public class AuthorityNameHeadingTag extends AuthorityHeadingTag {


  public AuthorityNameHeadingTag() {
    super(new NME_HDG());
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  @Override
  public int getCategory() {
    return 2;
  }


  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
   */
  public List getSecondCorrelationList(short value1) {
    DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
    return dao.getSecondCorrelationList(
      getCategory(),
      getHeadingType(),
      value1,
      NameSubType.class);
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    /*
     * if v3 is defined then we are a Reference tag and not a heading
     */
    return v.isValueDefined(3);
  }

}
