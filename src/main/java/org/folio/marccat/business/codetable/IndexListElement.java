package org.folio.marccat.business.codetable;

/**
 * Holds a single entry for IDX_LIST lists
 */
public class IndexListElement {
  private String value;
  private String label;
  private String key;

  public IndexListElement() {
  }

  /**
   * Class constructor
   *
   * @param value
   * @param label
   * @param key
   * @since 1.0
   */
  public IndexListElement(String value, String label, String key) {
    setValue(value);
    setLabel(label);
    setKey(key);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String string) {
    label = string;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String string) {
    value = string;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String s) {
    key = s;
  }

}
