package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author elena
 *
 */
public class AuthorityNote extends RecordNote {
  private String noteStringText;
  /**
   * 
   */
  private static final long serialVersionUID = 5041286137447344231L;

  public AuthorityNote() {
    super();
  }

  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setNoteNbr(dao.getNextNumber("AN", session));
  }

  @Override
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues().change(1, getNoteType());
  }

  @Override
  public StringText getStringText() {
    return new StringText(getNoteStringText());
  }

  @Override
  public void setStringText(StringText stringText) {
    setNoteStringText(stringText.toString());
  }

  public String getNoteStringText() {
    return noteStringText;
  }

  public void setNoteStringText(String string) {
    noteStringText = string;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof AuthorityNote)) {
      return false;
    } else {
      AuthorityNote aNote = (AuthorityNote) obj;
      return aNote.getItemNumber() == this.getItemNumber() && aNote.getNoteNbr() == this.getNoteNbr();
    }
  }

  @Override
  public int hashCode() {
    return getItemNumber() + getNoteNbr();
  }
}
