package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.PUBL_TAG;

public class DAOPublTag extends DAODescriptor {

  public Class getPersistentClass() {
    return PUBL_TAG.class;
  }

}
