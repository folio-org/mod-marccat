/*
 * (c) LibriCore
 *
 * Created on Dec 5, 2005
 *
 * AuthorityNote.java
 */
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
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/04/27 12:56:53 $
 * @since 1.0
 */
public class AuthorityNote extends VariableField implements Persistence, PersistsViaItem {
  private AUT autItm;
  private int noteNumber;
  private int itemNumber;
  //  private int noteType = Defaults.getShort("authorityNote.noteType");
  private String noteStringText;
  private PersistenceState persistenceState = new PersistenceState();

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityNote() {
    super();
    StringText s = new StringText();
    s.addSubfield(new Subfield("a", ""));
    setStringText(s);
    setPersistenceState(persistenceState);
  }

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityNote(int itemNumber) {
    super(itemNumber);
    setPersistenceState(persistenceState);
  }

  /* (non-Javadoc)
   * @see TagInterface#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
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
   * @see TagInterface#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
//    return new CorrelationValues().change(1, getNoteType());
    return null;
  }

  /* (non-Javadoc)
   * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
  }

  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
    // return getDaoCodeTable().getList(T_AUT_NTE_TYP.class,true);
    return null;
  }

  /* (non-Javadoc)
   * @see VariableField#getStringText()
   */
  public StringText getStringText() {
    return new StringText(getNoteStringText());
  }

  /* (non-Javadoc)
   * @see VariableField#setStringText(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    setNoteStringText(stringText.toString());
  }

  /* (non-Javadoc)
   * @see TagInterface#isNote()
   */
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

//  /**
//   * @since 1.0
//   */
//  public int getNoteType() {
//    return noteType;
//  }
//
//  /**
//   * @since 1.0
//   */
//  public void setNoteType(int s) {
//    noteType = s;
//  }

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

  public int getItemNumber() {
    return itemNumber;
  }

  public void setItemNumber(int itemNumber) {
    this.itemNumber = itemNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    //result = prime * result + itemNumber;
    result = prime * result + noteNumber;
    //result = prime * result + noteType;
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
		/*if (itemNumber != other.itemNumber)
			return false;*/
    return noteNumber == other.noteNumber;
  }


}
