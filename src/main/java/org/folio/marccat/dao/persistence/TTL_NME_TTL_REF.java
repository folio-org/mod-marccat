package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.*;

/**
 * The different cross references for the titles/name.
 *
 * @author paulm
 * @author carment
 */
public class TTL_NME_TTL_REF extends REF {

  /** The DAO. */
  private static final TitleNameTitleReferencesDAO theDAO = new TitleNameTitleReferencesDAO();

  /** The name title heading number. */
  private int nameTitleHeadingNumber;

  /** The title heading number. */
  private int titleHeadingNumber;

  /** The source heading type. */
  private String sourceHeadingType;

  /**
   * Checks if is source title.
   *
   * @return true, if is source title
   */
  public boolean isSourceTitle() {
    return getSourceHeadingType().equals("TH");
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

  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    if (isSourceTitle()) {
      return new NameTitleDescriptorDAO();
    } else {
      return new TitleDescriptorDAO();
    }

  }

  /**
   * Inits the.
   *
   * @param source the source
   * @param target the target
   * @param referenceType the reference type
   * @param cataloguingView the cataloguing view
   */
  /* (non-Javadoc)
   * @see REF#init(Descriptor, Descriptor, short, int)
   */
  public void init(
    Descriptor source,
    Descriptor target,
    short referenceType,
    int cataloguingView) {

    super.init(source, target, referenceType, cataloguingView);
    if (source instanceof TTL_HDG) {
      setTitleHeadingNumber(source.getKey().getHeadingNumber());
      setNameTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("TH");
    } else {
      setNameTitleHeadingNumber(source.getKey().getHeadingNumber());
      setTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("MH");
    }
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

  /* (non-Javadoc)
   * @see REF#getSource()
   */
  @Override
  public int getSource() {
    if (isSourceTitle()) {
      return getTitleHeadingNumber();
    } else {
      return getNameTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setSource(int)
   */
  @Override
  public void setSource(int i) {
    if (isSourceTitle()) {
      setTitleHeadingNumber(i);
    } else {
      setNameTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#getTarget()
   */
  @Override
  public int getTarget() {
    if (isSourceTitle()) {
      return getNameTitleHeadingNumber();
    } else {
      return getTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setTarget(int)
   */
  @Override
  public void setTarget(int i) {
    if (isSourceTitle()) {
      setNameTitleHeadingNumber(i);
    } else {
      setTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#createReciprocal()
   */
  @Override
  public REF createReciprocal() {
    TTL_NME_TTL_REF result = (TTL_NME_TTL_REF) this.clone();
    if (isSourceTitle()) {
      result.setSourceHeadingType("MH");
    } else {
      result.setSourceHeadingType("TH");
    }
    result.setType(ReferenceType.getReciprocal(getType()));
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.PersistenceState#getDAO()
   */
  @Override
  public AbstractDAO getDAO() {
    return theDAO;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TTL_NME_TTL_REF) {
      TTL_NME_TTL_REF ref = (TTL_NME_TTL_REF) obj;
      return this.getTitleHeadingNumber() == ref.getTitleHeadingNumber()
        && this.getNameTitleHeadingNumber()
        == ref.getNameTitleHeadingNumber()
        && this.getType() == ref.getType()
        && this.getSourceHeadingType().equals(ref.getSourceHeadingType())
        && this.getUserViewString().equals(ref.getUserViewString());
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getTitleHeadingNumber() + 3 * getNameTitleHeadingNumber();
  }

}
