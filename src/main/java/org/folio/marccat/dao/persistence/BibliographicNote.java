/*
 * Created on 20-jul-2004
 *
 */
package org.folio.marccat.dao.persistence;

import java.util.ArrayList;
import java.util.List;

import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.BibliographicNoteDAO;

/**
 * Class comment
 *
 * @author janick
 */
public class BibliographicNote extends RecordNote {
  /**
   * 
   */
  private static final long serialVersionUID = 8857348340760744618L;

  private List<BibliographicNoteOverflow> overflowList = new ArrayList<>();
  /**
   * The content is all or a part of the stringText.toString() result.
   */
  private char overflowIndicator = '0';
  private UserViewHelper userViewHelper = new UserViewHelper();

  public BibliographicNote() {
    super();
  }

  @Override
  public int hashCode() {
    return getNoteNbr();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof BibliographicNote)) {
      return false;
    } else {
      BibliographicNote aNote = (BibliographicNote) obj;
      return aNote.getBibItemNumber() == this.getBibItemNumber()
          && aNote.getUserViewString().equals(this.getUserViewString()) && aNote.getNoteNbr() == this.getNoteNbr();
    }
  }

  /**
   *
   */
  public char getOverflowIndicator() {
    return overflowIndicator;
  }

  /**
   *
   */
  public void setOverflowIndicator(final char c) {
    overflowIndicator = c;
  }

  @Override
  public AbstractDAO getDAO() {
    return new BibliographicNoteDAO();
  }

  /**
   * @return
   */
  public List<BibliographicNoteOverflow> getOverflowList() {
    return overflowList;
  }

  /**
   * @param list
   */
  public void setOverflowList(List<BibliographicNoteOverflow> list) {
    overflowList = list;
  }

  @Override
  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }

  @Override
  public void setUserViewString(String string) {
    userViewHelper.setUserViewString(string);
  }

  public int getBibItemNumber() {
    return getItemNumber();
  }

  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

}
