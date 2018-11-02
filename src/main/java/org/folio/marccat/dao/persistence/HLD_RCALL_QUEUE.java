/*
 * (c) LibriCore
 *
 * Created on 27-abr-2005
 *
 * HLD_RCALL_QUEUE.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class HLD_RCALL_QUEUE implements Serializable {
  private HoldingRecallQueueKey key;
  private char holdingRecallQueueTypeCode;


  /**
   * @return Returns the holdingRecallQueueTypeCode.
   * @throws
   * @since 1.0
   */
  public char getHoldingRecallQueueTypeCode() {
    return holdingRecallQueueTypeCode;
  }

  /**
   * @param holdingRecallQueueTypeCode The holdingRecallQueueTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setHoldingRecallQueueTypeCode(
    char holdingRecallQueueTypeCode) {
    this.holdingRecallQueueTypeCode = holdingRecallQueueTypeCode;
  }

  /**
   * @return Returns the key.
   * @throws
   * @since 1.0
   */
  public HoldingRecallQueueKey getKey() {
    return key;
  }

  /**
   * @param key The key to set.
   * @throws
   * @since 1.0
   */
  public void setKey(HoldingRecallQueueKey key) {
    this.key = key;
  }
}
