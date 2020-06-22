package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.*;

/**
 * The different cross references for the titles/name names.
 *
 * @author paulm
 * @author carment
 */
public class NME_NME_TTL_REF extends REF {

  /**
   * The DAO.
   */
  private static final NameNameTitleReferencesDAO theDAO = new NameNameTitleReferencesDAO();

  /**
   * The name title heading number.
   */
  private int nameTitleHeadingNumber;

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
   * Gets the name title heading number.
   *
   * @return the name title heading number
   * @since 1.0
   */
  public int getNameTitleHeadingNumber() {
    return nameTitleHeadingNumber;
  }

  /**
   * Sets the name title heading number.
   *
   * @param i the new name title heading number
   * @since 1.0
   */
  public void setNameTitleHeadingNumber(int i) {
    nameTitleHeadingNumber = i;
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
      return new NameTitleDescriptorDAO();
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
      setNameTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("NH");
    } else {
      setNameTitleHeadingNumber(source.getKey().getHeadingNumber());
      setNameHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("MH");
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
      return getNameTitleHeadingNumber();
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
      setNameTitleHeadingNumber(i);
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
      return getNameTitleHeadingNumber();
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
      setNameTitleHeadingNumber(i);
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
    NME_NME_TTL_REF result = (NME_NME_TTL_REF) this.copy();
    if (isSourceName()) {
      result.setSourceHeadingType("MH");
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
    if (obj instanceof NME_NME_TTL_REF) {
      NME_NME_TTL_REF ref = (NME_NME_TTL_REF) obj;
      return this.getNameHeadingNumber() == ref.getNameHeadingNumber()
        && this.getNameTitleHeadingNumber()
        == ref.getNameTitleHeadingNumber()
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
    return getNameHeadingNumber() + 3 * getNameTitleHeadingNumber();
  }

}
