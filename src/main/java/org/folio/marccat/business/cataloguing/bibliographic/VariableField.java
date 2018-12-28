package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Collections;
import java.util.List;
import static java.util.Optional.ofNullable;


/**
 * Manages the variable tag field.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public abstract class VariableField extends Tag {

  /**
   * Class constructor.
   *
   * @since 1.0
   */
  public VariableField() {
    super();
  }

  /**
   * Class constructor.
   *
   * @param itemNumber the item number
   * @since 1.0
   */
  public VariableField(final int itemNumber) {
    super(itemNumber);
  }


  /**
   * Return default value for variable field.
   *
   * @return false.
   */
  public boolean isBrowsable() {
    return false;
  }

  /**
   * Sets default value for second correlation.
   *
   * @param value1 the first correlation value
   * @return null.
   * @throws DataAccessException in case of data access exception.
   */
  public List getSecondCorrelationList(final int value1) throws DataAccessException {
    return Collections.emptyList();
  }

  /**
   * Sets default value for third correlation.
   *
   * @param value1 the first correlation value.
   * @param value2 the second correlation value.
   * @return null.
   * @throws DataAccessException in case of data access exception.
   */
  public List getThirdCorrelationList(final int value1, final int value2) throws DataAccessException {
    return Collections.emptyList();
  }

  /**
   * Gets the string text.
   *
   * @return the string text
   */
  public abstract StringText getStringText();

  /**
   * Sets the string text.
   *
   * @param stringText the new string text
   */
  public abstract void setStringText(final StringText stringText);

  /**
   * Sets default implementation.
   *
   * @return true.
   */
  public boolean isAbleToBeDeleted() {
    return true;
  }

  /**
   * Sets default implementation.
   *
   * @return false.
   */
  public boolean isEditableHeader() {
    return false;
  }

  /**
   * Sets default implementation.
   *
   * @return false.
   */
  public final boolean isFixedField() {
    return false;
  }

  /**
   * Sets default implementation.
   *
   * @return false.
   */
  public boolean isHeaderField() {
    return false;
  }

  /**
   * Sets default implementation.
   *
   * @return true.
   */
  public boolean isWorksheetEditable() {
    return true;
  }

  /**
   * Generates an element content from document.
   *
   * @param xmlDocument -- the xml document.
   * @return an element content.
   */
  public Element generateModelXmlElementContent(final Document xmlDocument) {
    return ofNullable(xmlDocument).map(content -> {
      return getStringText().generateModelXmlElementContent(xmlDocument);
    }).orElse(null);
  }

  /**
   * Converts an xml element to string text.
   *
   * @param xmlElement -- the xml element content.
   */
  public void parseModelXmlElementContent(final Element xmlElement) {
    setStringText(StringText.parseModelXmlElementContent(xmlElement));
  }

  /**
   * Checks if string text is null or empty.
   *
   * @return boolean.
   */
  public boolean isEmpty() {
    return getStringText() == null || getStringText().isEmpty();
  }

  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  @Override
  public StringText addPunctuation()throws Exception{
    return getStringText();
  }

}

