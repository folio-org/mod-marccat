/*
 * Created on 20-jul-2004
 *
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOBibliographicNote;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.ArrayList;
import java.util.List;

/**
 * Class comment
 *
 * @author janick
 */
public class BibliographicNote extends VariableField implements PersistentObjectWithView, OrderedTag {
  private static final short bibliographicNoteCategory = 7;
  public List<BibliographicNoteOverflow> overflowList = new ArrayList<>();
  /**
   * The content is all or a part of the stringText.toString() result.
   */
  private String content = null;
  private int noteType;
  private int noteNbr = -1;
  private char overflowIndicator = '0';
  private UserViewHelper userViewHelper = new UserViewHelper();
  private Integer sequenceNumber;

  public BibliographicNote() {
    super();
    setPersistenceState(new PersistenceState());
  }

  /**
   *
   */
  public BibliographicNote(final int itemNbr) {
    super(itemNbr);
  }

  @Override
  public boolean isBrowsable() {
    return false;
  }

  /**
   *
   */
  public int getNoteNbr() {
    return noteNbr;
  }

  /**
   * @param i
   */
  public void setNoteNbr(final int i) {
    noteNbr = i;
  }

  /**
   *
   */
  public String getStringTextString() {
    return content;
  }

  /**
   * This value is used by Hibernate
   * It must preserve the $a value
   */
  public void setStringTextString(final String st) {
    this.content = st;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof BibliographicNote)) {
      return false;
    } else {
      BibliographicNote aNote = (BibliographicNote) obj;
      return aNote.getBibItemNumber() == this.getBibItemNumber() &&
        aNote.getUserViewString().equals(this.getUserViewString()) &&
        aNote.getNoteNbr() == this.getNoteNbr();
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getNoteNbr();
  }

  public StringText getStringText() {
    return new StringText(content);
  }

  /**
   * @param text should not ends with Subfield separator
   */
  public void setStringText(final StringText text) {
    content = text.toString();
  }

  public int getCategory() {
    return bibliographicNoteCategory;
  }

  @Override
  @Deprecated
  public CorrelationValues getCorrelationValues() {
    return null;
  }

  @Deprecated
  public void setCorrelationValues(final CorrelationValues v) {
    setNoteType(v.getValue(1));
  }

  /**
   *
   */
  public int getNoteType() {
    return noteType;
  }

  /**
   *
   */
  public void setNoteType(final int s) {
    noteType = s;
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
    return new DAOBibliographicNote();
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

  /**
   * @deprecated
   */
  @Deprecated
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    if (v.getValue(1) == Global.PUBLISHER_DEFAULT_NOTE_TYPE) {
      return true;
    } else if (v.getValue(1) == 381) {
      return true;
    } else return v.getValue(1) == 382;
  }


  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }


  public void setUserViewString(String string) {
    userViewHelper.setUserViewString(string);
  }


  public int getBibItemNumber() {
    return getItemNumber();
  }


  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

  public void setContent(String text) {
    this.content = text;
  }

  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer integer) {
    sequenceNumber = integer;
  }


}
