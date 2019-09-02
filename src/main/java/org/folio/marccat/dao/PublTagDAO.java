package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.PUBL_TAG;

public class PublTagDAO extends DescriptorDAO {

  public Class getPersistentClass() {
    return PUBL_TAG.class;
  }

}
