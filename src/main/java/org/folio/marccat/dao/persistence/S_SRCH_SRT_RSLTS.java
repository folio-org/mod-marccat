/*
 * (c) LibriCore
 *
 * Created on Jul 28, 2005
 *
 * S_SRT_CRTRIA_DTL.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/28 11:41:23 $
 * @since 1.0
 */
public class S_SRCH_SRT_RSLTS implements Serializable {

  private short itemNbr;


  public S_SRCH_SRT_RSLTS() {
    super();
    // TODO Auto-generated constructor stub
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + itemNbr;
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final S_SRCH_SRT_RSLTS other = (S_SRCH_SRT_RSLTS) obj;
    return itemNbr == other.itemNbr;
  }


}
