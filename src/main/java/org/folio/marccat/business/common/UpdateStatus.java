/*
 * (c) LibriCore
 *
 * Created on Aug 19, 2004
 *
 * UpdateStatus.java
 */
package org.folio.marccat.business.common;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/08/24 08:13:22 $
 * @since 1.0
 */
public class UpdateStatus {
  public static final int NEW = 0;
  public static final int CHANGED = 1;
  public static final int DELETED = 2;  // scheduled for deletion but not gone
  public static final int REMOVED = 3;  // has been deleted from database
  public static final int UNCHANGED = 4;
}
