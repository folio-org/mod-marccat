package org.folio.marccat.exception;

/**


 */
public class ReferentialIntegrityException extends DataAccessException {

  private String fromTable;
  private String toTable;

  /**
   * Class constructor
   *
   * @param fromTable the type holding the reference
   * @param toTable   the type being deleted
   * @since 1.0
   */
  public ReferentialIntegrityException(String fromTable, String toTable) {
    super();
    setFromTable(fromTable);
    setToTable(toTable);
  }

  /**
   * @since 1.0
   */
  public void setFromTable(String string) {
    fromTable = string;
  }

  /**
   * @since 1.0
   */
  public void setToTable(String string) {
    toTable = string;
  }

}
