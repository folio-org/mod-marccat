package org.folio.marccat.business.cataloguing.authority;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;


public class AuthorityNote extends VariableField implements Persistence, PersistsViaItem {
  private AUT autItm;
  private int noteNumber;
  private int itemNumber;
  private String noteStringText;
  private PersistenceState persistState = new PersistenceState();


  public AuthorityNote() {
    super();
    StringText s = new StringText();
    s.addSubfield(new Subfield("a", ""));
    setStringText(s);
    setPersistenceState(persistState);
  }

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityNote(int itemNumber) {
    super(itemNumber);
    setPersistenceState(persistState);
  }

  /* (non-Javadoc)
   * @see TagInterface#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setNoteNumber(dao.getNextNumber("AN", session));
  }


  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    return 7;
  }

  /* (non-Javadoc)
   * @see VariableField#getDisplayValue()
   */
  public StringText getStringText() {
    return new StringText(getNoteStringText());
  }

  /* (non-Javadoc)
   * @see VariableField#setDisplayValue(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    setNoteStringText(stringText.toString());
  }

  /* (non-Javadoc)
   * @see TagInterface#isNote()
   */
  @Override
  public boolean isNote() {
    return true;
  }

  /**
   * @since 1.0
   */
  public int getNoteNumber() {
    return noteNumber;
  }

  /**
   * @since 1.0
   */
  public void setNoteNumber(int i) {
    noteNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getNoteStringText() {
    return noteStringText;
  }

  /**
   * @since 1.0
   */
  public void setNoteStringText(String string) {
    noteStringText = string;
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#getItemEntity()
   */
  public ItemEntity getItemEntity() {
    return autItm;
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    this.autItm = (AUT) item;
  }

  @Override
  public int getItemNumber() {
    return itemNumber;
  }

  @Override
  public void setItemNumber(int itemNumber) {
    this.itemNumber = itemNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + noteNumber;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    final AuthorityNote other = (AuthorityNote) obj;
    return noteNumber == other.noteNumber;
  }

  @Override
  public CorrelationValues getCorrelationValues() {
    return null;
  }

  /* (non-Javadoc)
   * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setCorrelationValues(v);
  }


}
