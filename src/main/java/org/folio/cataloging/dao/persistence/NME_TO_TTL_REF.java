/*
 * (c) LibriCore
 *
 * Created on Dec 29, 2014
 *
 * NME_TO_TTL_REF.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.*;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class NME_TO_TTL_REF extends REF {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private static final DAONameToTitleReferences theDAO =
    new DAONameToTitleReferences();
  private int titleHeadingNumber;
  private int nameHeadingNumber;
  private String sourceHeadingType;

  public boolean isSourceName() {
    return getSourceHeadingType().equals("NH");
  }

  /**
   * @since 1.0
   */
  public int getNameHeadingNumber() {
    return nameHeadingNumber;
  }

  /**
   * @since 1.0
   */
  public void setNameHeadingNumber(int i) {
    nameHeadingNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getTitleHeadingNumber() {
    return titleHeadingNumber;
  }

  /**
   * @since 1.0
   */
  public void setTitleHeadingNumber(int i) {
    titleHeadingNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getSourceHeadingType() {
    return sourceHeadingType;
  }

  /**
   * @since 1.0
   */
  public void setSourceHeadingType(String string) {
    sourceHeadingType = string;
  }

  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    if (isSourceName()) {
      return new TitleDescriptorDAO();
    } else {
      return new NameDescriptorDAO();
    }

  }

  /* (non-Javadoc)
   * @see REF#init(Descriptor, Descriptor, short, int)
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

  /* (non-Javadoc)
   * @see REF#getSource()
   */
  public int getSource() {
    if (isSourceName()) {
      return getNameHeadingNumber();
    } else {
      return getTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setSource(int)
   */
  public void setSource(int i) {
    if (isSourceName()) {
      setNameHeadingNumber(i);
    } else {
      setTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#getTarget()
   */
  public int getTarget() {
    if (isSourceName()) {
      return getTitleHeadingNumber();
    } else {
      return getNameHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setTarget(int)
   */
  public void setTarget(int i) {
    if (isSourceName()) {
      setTitleHeadingNumber(i);
    } else {
      setNameHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#createReciprocal()
   */
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

  /* (non-Javadoc)
   * @see librisuite.business.common.PersistenceState#getDAO()
   */
  public AbstractDAO getDAO() {
    return theDAO;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
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

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getNameHeadingNumber() + 3 * getTitleHeadingNumber();
  }

}
