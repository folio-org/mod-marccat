package org.folio.marccat.shared;

/**
 * Wrapper class for Diacritics
 * <p>
 */
public class MapDiacritic {

  private Long code;
  private String description;
  private String character;
  private String characterSet;
  private String unicode;


  /**
   * @return The code
   */
  public Long getCode() {
    return code;
  }

  /**
   * @param code The code
   */
  public void setCode(Long code) {
    this.code = code;
  }


  /**
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description The description
   */

  public void setDescription(String description) {
    this.description = description;
  }


  /**
   * @return The character
   */
  public String getCharacter() {
    return character;
  }

  /**
   * @param character The character
   */
  public void setCharacter(String character) {
    this.character = character;
  }


  /**
   * @return The characterSet
   */
  public String getCharacterSet() {
    return characterSet;
  }

  /**
   * @param characterSet The characterSet
   */
  public void setCharacterSet(String characterSet) {
    this.characterSet = characterSet;
  }


  /**
   * @return The unicode
   */

  public String getUnicode() {
    return unicode;
  }

  /**
   * @param unicode The unicode
   */

  public void setUnicode(String unicode) {
    this.unicode = unicode;
  }


}
