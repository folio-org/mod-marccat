package org.folio.marccat.action.cataloguing.bibliographic;

import org.folio.marccat.exception.ModCatalogingException;

public class SaveTagException extends ModCatalogingException {
  protected int tagIndex = 0;
  private String tagNumber;

  public SaveTagException() {
    super();
  }

  public SaveTagException(String message, Throwable cause) {
    super(message, cause);
  }

  public SaveTagException(String message) {
    super(message);
    setTagNumber(message);
  }

  public SaveTagException(Throwable cause) {
    super(cause);
  }

  /**
   * @since 1.0
   */
  public String getTagNumber() {
    return tagNumber;
  }

  /**
   * @since 1.0
   */
  public void setTagNumber(String string) {
    tagNumber = string;
  }

  /**
   * @since 1.0
   */
  public int getTagIndex() {
    return tagIndex;
  }

  /**
   * @since 1.0
   */
  public void setTagIndex(int i) {
    tagIndex = i;
  }


}
