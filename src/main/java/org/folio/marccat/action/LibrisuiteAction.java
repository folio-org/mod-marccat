package org.folio.marccat.action;

import org.folio.marccat.dao.DAOCodeTable;

// TODO: REMOVE
public abstract class LibrisuiteAction {
  @Deprecated
  public static DAOCodeTable getDaoCodeTable() {
    throw new IllegalArgumentException("LS Action is going to be removed!");
  }
}
