package org.folio.marccat.dao.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.PersistenceState;
import java.util.ArrayList;
import java.util.List;

public class PersistentStateManager {
  private static final Log logger =
    LogFactory.getLog(PersistentStateManager.class);
  boolean acceptRegistrations = false;
  /**
   * Using a list keep can track multiple instances of the same state but only the
   * last is committed (because the PersistentState do not track the
   * discarded/intermediate changes)
   * use Hashtable for a fast index search and to keep only a instance of state
   */
  private List/*<PersistenceState>*/ states = null;

  public PersistentStateManager() {
    super();
    logger.debug("PersistentStateManager creation for this thread");
    states = new ArrayList/*<PersistenceState>*/();
  }


  /**
   * Register the time shifted persistent state of an object
   *
   * @param newPersistenceState
   */
  public void register(PersistenceState newPersistenceState) {
    if (acceptRegistrations) {
      logger.debug("change persistent status: " + newPersistenceState.toString());
      states.add(newPersistenceState);
    } else newPersistenceState.confirmChanges();
  }



}
