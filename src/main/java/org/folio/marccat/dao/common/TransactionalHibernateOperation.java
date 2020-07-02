package org.folio.marccat.dao.common;


import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.config.log.Log;


public abstract class TransactionalHibernateOperation {

  private static final Log logger = new Log(TransactionalHibernateOperation.class);

  private static ThreadLocal nestingLevel = new ThreadLocal() {
    @Override
    protected synchronized Object initialValue() {
      return Integer.valueOf(0);
    }
  };

  private static ThreadLocal stateManager = new ThreadLocal() {
    @Override
    protected synchronized Object initialValue() {
      return new PersistentStateManager();
    }
  };

  public static int getNestingLevel() {
    return ((Integer) nestingLevel.get()).intValue();
  }

  public static void setNestingLevel(int i) {
    nestingLevel.set(Integer.valueOf(i));
  }

  private static PersistentStateManager getPersistentStateManager() {
    return (PersistentStateManager) stateManager.get();
  }

  public static void register(PersistenceState newPersistenceState) {

    getPersistentStateManager().register(newPersistenceState);
  }


}
