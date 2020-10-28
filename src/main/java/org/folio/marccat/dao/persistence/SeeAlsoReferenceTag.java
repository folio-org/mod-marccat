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

  public SeeAlsoReferenceTag() {
    super();
  }

  public REF getDualReference() {
    return dualReference;
  }

  public void setDualReference(REF ref) {
    dualReference = ref;
  }

  /*
   * (non-Javadoc)
   * 
   * @see SeeSeeAlsoReference#getHasDualIndicator()
   */
  @Override
  public boolean isHasDualIndicator() {
    return true;
  }

}
