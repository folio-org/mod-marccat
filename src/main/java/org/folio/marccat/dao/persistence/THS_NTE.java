/*
 * (c) LibriCore
 *
 * Created on 09-jul-2004
 *
 * HLDG_NTE.java
 */
package org.folio.marccat.dao.persistence;


import java.io.Serializable;

/**
 * @author elena
 * @version $Revision: 1.4 $, $Date: 2004/08/19 14:05:26 $
 * @since 1.0
 */
public class THS_NTE implements Serializable {

  private ThesaurusNoteKey key;
  private String noteText;
  private String language;

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public ThesaurusNoteKey getKey() {
    return key;
  }

  public void setKey(ThesaurusNoteKey key) {
    this.key = key;
  }


  public String getNoteText() {
    return noteText;
  }


  public void setNoteText(String noteText) {
    this.noteText = noteText;
  }

}
