package org.folio.marccat.business.cataloguing.bibliographic;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class NameTitleComponent extends BibliographicAccessPoint {

  protected int nameTitleHeadingNumber = 0;

  public NameTitleComponent() {
  }

  /**
   * Class constructor
   *
   * @param itemNbr
   * @since 1.0
   */
  public NameTitleComponent(final int itemNbr) {
    super(itemNbr);
  }

  public int getNameTitleHeadingNumber() {
    return nameTitleHeadingNumber;
  }

  public void setNameTitleHeadingNumber(final int i) {
    nameTitleHeadingNumber = i;
  }

  public boolean isPartOfNameTitle() {
    return getNameTitleHeadingNumber() != 0;
  }
}
