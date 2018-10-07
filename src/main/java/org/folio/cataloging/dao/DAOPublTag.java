package org.folio.cataloging.dao;

import org.folio.cataloging.dao.persistence.PUBL_TAG;

public class DAOPublTag extends DAODescriptor {

  public Class getPersistentClass() {
    return PUBL_TAG.class;
  }

}
