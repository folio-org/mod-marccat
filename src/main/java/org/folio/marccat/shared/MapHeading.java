package org.folio.marccat.shared;



import org.folio.marccat.resources.domain.Ref;

import java.util.List;

/**
 * Wrapper class for Haeding
 * <p>
 *
 * @author carment
 */
public class MapHeading {

  /**
   * The heading number.
   */
  private Integer headingNumber;

  /**
   * The string text.
   */
  private String stringText;

  /**
   * The count title name documents.
   */
  private Integer countTitleNameDocuments;

  /**
   * The count cross references.
   */
  private Integer countCrossReferences;

  /**
   * The count authorities.
   */
  private Integer countAuthorities;

  /**
   * The count documents.
   */
  private Integer countDocuments;

  /**
   * The cross reference.
   */
  private List<Ref> crossReferences;

  /**
   * The access point language.
   */
  private String accessPointlanguage;

  /**
   * Gets the heading number.
   *
   * @return the heading number
   */
  public Integer getHeadingNumber() {
    return headingNumber;
  }

  /**
   * Sets the heading number.
   *
   * @param headingNumber the new heading number
   */
  public void setHeadingNumber(Integer headingNumber) {
    this.headingNumber = headingNumber;
  }

  /**
   * Gets the string text.
   *
   * @return the string text
   */
  public String getStringText() {
    return stringText;
  }

  /**
   * Sets the string text.
   *
   * @param stringText the new string text
   */
  public void setStringText(String stringText) {
    this.stringText = stringText;
  }

  /**
   * Gets the count title name documents.
   *
   * @return the count title name documents
   */
  public Integer getCountTitleNameDocuments() {
    return countTitleNameDocuments;
  }

  /**
   * Sets the count title name documents.
   *
   * @param countTitleNameDocuments the new count title name documents
   */
  public void setCountTitleNameDocuments(Integer countTitleNameDocuments) {
    this.countTitleNameDocuments = countTitleNameDocuments;
  }

  /**
   * Gets the count cross references.
   *
   * @return the count cross references
   */
  public Integer getCountCrossReferences() {
    return countCrossReferences;
  }

  /**
   * Sets the count cross references.
   *
   * @param countCrossReferences the new count cross references
   */
  public void setCountCrossReferences(Integer countCrossReferences) {
    this.countCrossReferences = countCrossReferences;
  }

  /**
   * Gets the count authorities.
   *
   * @return the count authorities
   */
  public Integer getCountAuthorities() {
    return countAuthorities;
  }

  /**
   * Sets the count authorities.
   *
   * @param countAuthorities the new count authorities
   */
  public void setCountAuthorities(Integer countAuthorities) {
    this.countAuthorities = countAuthorities;
  }

  /**
   * Gets the count documents.
   *
   * @return the count documents
   */
  public Integer getCountDocuments() {
    return countDocuments;
  }

  /**
   * Sets the count documents.
   *
   * @param countDocuments the new count documents
   */
  public void setCountDocuments(Integer countDocuments) {
    this.countDocuments = countDocuments;
  }

  /**
   * Gets the access point language.
   *
   * @return the access point language
   */
  public String getAccessPointlanguage() {
    return accessPointlanguage;
  }

  /**
   * Sets the access point language.
   *
   * @param accessPointlanguage the new access point language
   */
  public void setAccessPointlanguage(String accessPointlanguage) {
    this.accessPointlanguage = accessPointlanguage;
  }

  /**
   * Gets the cross references.
   *
   * @return the list of cross references
   */
  public List<Ref> getCrossReferences() {
    return crossReferences;
  }

  /**
   * Sets the cross references.
   *
   * @param crossReferences the list of cross References
   */
  public void setCrossReferences(List<Ref> crossReferences) {
    this.crossReferences = crossReferences;
  }

}
