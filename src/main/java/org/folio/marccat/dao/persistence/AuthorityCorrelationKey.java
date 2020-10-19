package org.folio.marccat.dao.persistence;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityCorrelationKey extends CorrelationKey {

  private String headingType;

  /**
   * Class constructor
   *
   * 
   * @since 1.0
   */
  public AuthorityCorrelationKey() {
    super();
  }

  public String getHeadingType() {
    return headingType;
  }

  public void setHeadingType(String string) {
    headingType = string;
  }

  @Override
  public boolean equals(Object anObject) {
    if (anObject instanceof AuthorityCorrelationKey) {
      AuthorityCorrelationKey aKey = (AuthorityCorrelationKey) anObject;
      return (super.equals(aKey) && headingType == aKey.getHeadingType());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode() + headingType.hashCode();
  }

}
