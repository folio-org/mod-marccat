package org.folio.cataloging.bean.cataloguing.copy;

/**
 * @author elena
 * @version $Revision: 1.1 $, $Date: 2004/08/19 08:39:53 $
 * @since 1.0
 */
public class CopyNoteListElement {
  private String noteType;
  private String noteText;
  private int copyNoteNumber;

  /**
   * @since 1.0
   */
  public String getNoteText() {
    return noteText;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setNoteText(String string) {
    noteText = string;
  }

  /**
   * @since 1.0
   */
  public String getNoteType() {
    return noteType;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setNoteType(String string) {
    noteType = string;
  }

  /**
   * @since 1.0
   */
  public int getCopyNoteNumber() {
    return copyNoteNumber;
  }

  /**
   * @param i
   * @since 1.0
   */
  public void setCopyNoteNumber(int i) {
    copyNoteNumber = i;
  }

}
