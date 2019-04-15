/*
 * (c) LibriCore
 *
 * Created on Apr 25, 2006
 *
 * T_AUT_SHLF_LIST_TYP.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/05/10 10:02:58 $
 * @since 1.0
 */
public class T_AUT_SHLF_LIST_TYP extends T_SINGLE_CHAR {

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */

  /*
   * getCodeString is overridden to return the (short) stringValue of the character
   * code.  This codetable is used in the context of a correlationList which
   * expects short values.
   */
  @Override
  public String getCodeString() {
    return String.valueOf((short) getCode());
  }

}
