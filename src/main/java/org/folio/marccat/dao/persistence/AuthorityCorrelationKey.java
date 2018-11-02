/*
 * (c) LibriCore
 *
 * Created on Nov 17, 2005
 *
 * AuthorityCorrelationKey.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityCorrelationKey extends CorrelationKey {

  private String headingType;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityCorrelationKey() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param marcTag
   * @param headingType
   * @param marcTag1
   * @param marcTag2
   * @param marcTagCategory
   * @since 1.0
   */
  public AuthorityCorrelationKey(
    String marcTag,
    String headingType,
    char marcTag1,
    char marcTag2,
    short marcTagCategory) {
    super(marcTag, marcTag1, marcTag2, marcTagCategory);
    this.headingType = headingType;
  }

  /**
   * @since 1.0
   */
  public String getHeadingType() {
    return headingType;
  }

  /**
   * @since 1.0
   */
  public void setHeadingType(String string) {
    headingType = string;
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof CorrelationKey) {
      AuthorityCorrelationKey aKey =
        (AuthorityCorrelationKey) anObject;
      return (
        getMarcTag().equals(aKey.getMarcTag())
          && getHeadingType().equals(aKey.getHeadingType())
          && getMarcFirstIndicator() == aKey.getMarcFirstIndicator()
          && getMarcSecondIndicator() == aKey.getMarcSecondIndicator()
          && getMarcTagCategoryCode() == aKey.getMarcTagCategoryCode());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return getMarcTag().hashCode()
      + getHeadingType().hashCode()
      + (3 * getMarcFirstIndicator())
      + (5 * getMarcSecondIndicator())
      + (7 * getMarcTagCategoryCode());
  }

}
