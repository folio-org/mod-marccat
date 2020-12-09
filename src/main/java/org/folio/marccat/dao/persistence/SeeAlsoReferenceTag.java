package org.folio.marccat.dao.persistence;

/**
 * @author elena
 *
 */
public class SeeAlsoReferenceTag extends SeeSeeAlsoReference {
  /**
   * 
   */
  private static final long serialVersionUID = -8743809120329442785L;

  private REF dualReference = null;
  private short dualReferenceIndicator = 0;

  public SeeAlsoReferenceTag() {
    super();
  }

  public REF getDualReference() {
    return dualReference;
  }

  public void setDualReference(REF ref) {
    dualReference = ref;
  }

  @Override
  public boolean isHasDualIndicator() {
    return true;
  }

  @Override
  public short getDualReferenceIndicator() {
    return dualReferenceIndicator;
  }

  public void setDualReferenceIndicator(short s) {
    dualReferenceIndicator = s;
  }


}
