package org.folio.marccat.business.common;

/**
 * The class that manages the states of hibernate objects.
 *
 * @author paulm
 * @author carment
 */
public class UpdateStatus {

  /** The Constant NEW. */
  public static final int NEW = 0;

  /** The Constant CHANGED. */
  public static final int CHANGED = 1;

  /** The Constant DELETED. */
  public static final int DELETED = 2;

  /** The Constant REMOVED. */
  public static final int REMOVED = 3;

  /** The Constant UNCHANGED. */
  public static final int UNCHANGED = 4;
}
