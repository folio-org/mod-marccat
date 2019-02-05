package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.dao.persistence.T_BIB_HDR;

public class BibliographicHeaderFieldHelper extends HeaderFieldHelper {

  public int getCategory() {
    return 1;
  }

  public Class getHeaderListClass() {
    return T_BIB_HDR.class;
  }

}
