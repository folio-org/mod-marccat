package org.folio.cataloging.action;

import org.folio.cataloging.dao.DAOCodeTable;

// TODO: REMOVE
public abstract class LibrisuiteAction {
  @Deprecated
  public static DAOCodeTable getDaoCodeTable() {
    throw new IllegalArgumentException ("LS Action is going to be removed!");
  }
}
