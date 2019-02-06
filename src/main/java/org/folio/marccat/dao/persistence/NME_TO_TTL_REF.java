package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.*;

/**
 * The different cross references for the titles/name.
 *
 * @author paulm
 * @author carment
 */
public class NME_TO_TTL_REF extends REF {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The DAO.
   */
  private static final NameToTitleReferencesDAO theDAO = new NameToTitleReferencesDAO();

  /**
   * The title heading number.
   */
  private int titleHeadingNumber;

  /**
   * The name heading number.
   */
  private int nameHeadingNumber;

  /**
   * The source heading type.
   */
  private String sourceHeadingType;

  /**
   * Checks if is source name.
   *
   * @return true, if is source name
   */
  public boolean isSourceName() {
    return getSourceHeadingType().equals("NH");
  }

  /**
   * Gets the name heading number.
   *
   * @return the name heading number
   * @since 1.0
   */
  public int getNameHeadingNumber() {
    return nameHeadingNumber;
  }

  /**
   * Sets the name heading number.
   *
   * @param i the new name heading number
   * @since 1.0
   */
  public void setNameHeadingNumber(int i) {
    nameHeadingNumber = i;
  }

  /**
   * Gets the title heading number.
   *
   * @return the title heading number
   * @since 1.0
   */
  public int getTitleHeadingNumber() {
    return titleHeadingNumber;
  }

  /**
   * Sets the title heading number.
   *
   * @param i the new title heading number
   * @since 1.0
   */
  public void setTitleHeadingNumber(int i) {
    titleHeadingNumber = i;
  }

  /**
   * Gets the source heading type.
   *
   * @return the source heading type
   * @since 1.0
   */
  public String getSourceHeadingType() {
    return sourceHeadingType;
  }

  /**
   * Sets the source heading type.
   *
   * @param string the new source heading type
   * @since 1.0
   */
  public void setSourceHeadingType(String string) {
    sourceHeadingType = string;
  }


  /**
   * Gets the target DAO.
   *
   * @return the target DAO
   */
  public DAODescriptor getTargetDAO() {
    if (isSourceName()) {
      return new TitleDescriptorDAO();
    } else {
      return new NameDescriptorDAO();
    }

  }


  /**
   * Inits the.
   *
   * @param source          the source
   * @param target          the target
   * @param referenceType   the reference type
   * @param cataloguingView the cataloguing view
   */
  public void init(
    Descriptor source,
    Descriptor target,
    short referenceType,
    int cataloguingView) {

    super.init(source, target, referenceType, cataloguingView);
    if (source instanceof NME_HDG) {
      setNameHeadingNumber(source.getKey().getHeadingNumber());
      setTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("NH");
    } else {
      setTitleHeadingNumber(source.getKey().getHeadingNumber());
      setNameHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("TH");
    }
  }


  /**
   * Gets the source.
   *
   * @return the source
   */
  @Override
  public int getSource() {
    if (isSourceName()) {
      return getNameHeadingNumber();
    } else {
      return getTitleHeadingNumber();
    }
  }

  /**
   * Sets the source.
   *
   * @param i the new source
   */
  @Override
  public void setSource(int i) {
    if (isSourceName()) {
      setNameHeadingNumber(i);
    } else {
      setTitleHeadingNumber(i);
    }
  }

  /**
   * Gets the target.
   *
   * @return the target
   */
  @Override
  public int getTarget() {
    if (isSourceName()) {
      return getTitleHeadingNumber();
    } else {
      return getNameHeadingNumber();
    }
  }


  /**
   * Sets the target.
   *
   * @param i the new target
   */
  @Override
  public void setTarget(int i) {
    if (isSourceName()) {
      setTitleHeadingNumber(i);
    } else {
      setNameHeadingNumber(i);
    }
  }


  /**
   * Creates the reciprocal.
   *
   * @return the ref
   */
  @Override
  public REF createReciprocal() {
    NME_TO_TTL_REF result = (NME_TO_TTL_REF) this.clone();
    if (isSourceName()) {
      result.setSourceHeadingType("TH");
    } else {
      result.setSourceHeadingType("NH");
    }
    result.setType(ReferenceType.getReciprocal(getType()));
    return result;
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  @Override
  public AbstractDAO getDAO() {
    return theDAO;
  }


  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NME_TO_TTL_REF) {
      NME_TO_TTL_REF ref = (NME_TO_TTL_REF) obj;
      return this.getNameHeadingNumber() == ref.getNameHeadingNumber()
        && this.getTitleHeadingNumber()
        == ref.getTitleHeadingNumber()
        && this.getType() == ref.getType()
        && this.getSourceHeadingType().equals(ref.getSourceHeadingType())
        && this.getUserViewString().equals(ref.getUserViewString());
    } else {
      return false;
    }
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return getNameHeadingNumber() + 3 * getTitleHeadingNumber();
  }

}
