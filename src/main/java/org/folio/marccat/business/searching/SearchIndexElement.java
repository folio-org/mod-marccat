package org.folio.marccat.business.searching;

public class SearchIndexElement {
  private int key;
  private int subKey;
  private String value;
  private String label;

  public SearchIndexElement() {
  }

  public SearchIndexElement(int key, int subKey, String value, String label) {
    setKey(key);
    setSubKey(subKey);
    setValue(value);
    setLabel(label);
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getKey() {
    return key;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setKey(int i) {
    key = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setLabel(String string) {
    label = string;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getSubKey() {
    return subKey;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setSubKey(int i) {
    subKey = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getValue() {
    return value;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setValue(String string) {
    value = string;
  }

}
