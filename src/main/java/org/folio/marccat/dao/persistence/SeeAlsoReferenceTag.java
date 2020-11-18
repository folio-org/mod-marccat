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

  @Override
  public boolean isHasDualIndicator() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj != null && obj.getClass().equals(this.getClass())) {
      SeeAlsoReferenceTag aReference = (SeeAlsoReferenceTag) obj;
      return (super.equals(aReference) && aReference.getDualReference().equals(this.getDualReference()));
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return getReference().hashCode() + getDualReference().hashCode();
  }
}
