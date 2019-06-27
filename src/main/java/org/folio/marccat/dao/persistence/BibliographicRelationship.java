package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.BibliographicRelationshipDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Collections;
import java.util.List;

/**
 * The Class BibliographicRelationship.
 */
@SuppressWarnings("unused")
public class BibliographicRelationship extends VariableField implements PersistentObjectWithView {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5945143318867728785L;

  /**
   * The Constant bibliographicRelationshipCategory.
   */
  private static final short RELATION_CATEGORY= 8;

  /**
   * The target bib item number.
   */
  private int targetBibItemNumber;

  /**
   * The relation type code.
   */
  private int relationTypeCode;

  /**
   * The relation print note code.
   */
  private int relationPrintNoteCode;

  /**
   * The description.
   */
  private String description = null;

  /**
   * The qualifying description.
   */
  private String qualifyingDescription = null;

  /**
   * The string text.
   */
  private StringText stringText = null;


  /**
   * The material specific text.
   */
  private String materialSpecificText = null;

  /**
   * The reciprocal type.
   */
  private int reciprocalType;

  /**
   * The user view helper.
   */
  private UserViewHelper userViewHelper = new UserViewHelper();

  /**
   * Instantiates a new bibliographic relationship.
   */
  public BibliographicRelationship() {
    super();
    StringText s = new StringText("");
    setStringText(s);
    setReciprocalType(-1);
  }

  /**
   * Instantiates a new bibliographic relationship.
   *
   * @param itemNbr the item nbr
   */
  public BibliographicRelationship(int itemNbr) {
    super(itemNbr);
  }

  /**
   * Builds the string text.
   *
   * @param userView the user view
   * @return the string text
   */
  public StringText buildStringText(int userView) throws HibernateException {
    StringText s;
    BibliographicRelationshipDAO b = new BibliographicRelationshipDAO();
    try {
      s = b.buildRelationStringText(this.getTargetBibItemNumber(), userView, b.currentSession());
      s.add(getRelationshipStringText());
      return s;
    } catch (DataAccessException ex) {
      return stringText;
    }
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BibliographicRelationship))
      return false;
    BibliographicRelationship other = (BibliographicRelationship) obj;
    return (other.getItemNumber() == getItemNumber())
      && (other.getUserViewString().equals(getUserViewString()))
      && (other.getTargetBibItemNumber() == getTargetBibItemNumber())
      && (other.getRelationTypeCode() == getRelationTypeCode());
  }

  /**
   * Generate new blind relationship key.
   *
   * @param session the session
   * @throws DataAccessException the data access exception
   * @throws HibernateException  the hibernate exception
   */
  public void generateNewBlindRelationshipKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setTargetBibItemNumber(-dao.getNextNumber("BR", session));
  }

  /**
   * Gets the bib item number.
   *
   * @return the bib item number
   */
  public int getBibItemNumber() {
    return getItemNumber();
  }

  /**
   * Sets the bib item number.
   *
   * @param i the new bib item number
   */
  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
   */
  public int getCategory() {
    return RELATION_CATEGORY;
  }

  /**
   * Gets the correlation.
   *
   * @param i the i
   * @return the correlation
   */
  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelation(int)
   */
  @Override
  public int getCorrelation(int i) {
    switch (i) {
      case 1:
        return getRelationTypeCode();
      case 2:
        return getRelationPrintNoteCode();
      default:
        return -1;
    }
  }

  /**
   * Gets the correlation values.
   *
   * @return the correlation values
   */
  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues()).change(1, getRelationTypeCode()).change(2, getRelationPrintNoteCode());
  }

  /**
   * Sets the correlation values.
   *
   * @param v the new correlation values
   */
  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setRelationTypeCode(v.getValue(1));
    setRelationPrintNoteCode(v.getValue(2));
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new BibliographicRelationshipDAO();
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param text the new description
   */
  public void setDescription(String text) {
    description = text;
  }

  /**
   * Gets the first correlation list.
   *
   * @return the first correlation list
   * @throws DataAccessException the data access exception
   */
  public List getFirstCorrelationList() {
    return Collections.emptyList();
  }

  /**
   * Gets the material specific text.
   *
   * @return the material specific text
   */
  public String getMaterialSpecificText() {
    return materialSpecificText;
  }

  /**
   * Sets the material specific text.
   *
   * @param text the new material specific text
   */
  public void setMaterialSpecificText(String text) {
    materialSpecificText = text;
  }

  /**
   * Gets the qualifying description.
   *
   * @return the qualifying description
   */
  public String getQualifyingDescription() {
    return qualifyingDescription;
  }

  /**
   * Sets the qualifying description.
   *
   * @param text the new qualifying description
   */
  public void setQualifyingDescription(String text) {
    qualifyingDescription = text;
  }

  /**
   * Gets the reciprocal list.
   *
   * @return the reciprocal list
   * @throws DataAccessException the data access exception
   */
  public List getReciprocalList() {
    return Collections.emptyList();
  }

  /**
   * Gets the reciprocal option.
   *
   * @return the reciprocal option
   * @throws DataAccessException the data access exception
   */
  public int getReciprocalOption(final Session session) throws HibernateException {
    if (getReciprocalType() == -1) {
      final BibliographicRelationshipDAO relationshipDAO = new BibliographicRelationshipDAO();
      if (this.getTargetBibItemNumber() < 0) {
        return 3;
      } else {
        try {
          return relationshipDAO.getReciprocalBibItem(this.getTargetBibItemNumber(), this.getItemNumber(), 1, session);
        } catch (DataAccessException ex) {
          return -1;
        }
      }
    } else {
      return getReciprocalType();
    }
  }

  /**
   * Sets the reciprocal option.
   *
   * @param s the new reciprocal option
   */
  public void setReciprocalOption(int s) {
    setReciprocalType(s);
  }

  /**
   * Gets the reciprocal type.
   *
   * @return the reciprocal type
   */
  public int getReciprocalType() {
    return reciprocalType;
  }

  /**
   * Sets the reciprocal type.
   *
   * @param s the new reciprocal type
   */
  public void setReciprocalType(int s) {
    reciprocalType = s;
  }

  /**
   * Gets the relation print note code.
   *
   * @return the relation print note code
   */
  public int getRelationPrintNoteCode() {
    return relationPrintNoteCode;
  }

  /**
   * Sets the relation print note code.
   *
   * @param i the new relation print note code
   */
  public void setRelationPrintNoteCode(int i) {
    relationPrintNoteCode = i;
  }

  /**
   * Gets the relationship string text.
   *
   * @return the relationship string text
   */
  public StringText getRelationshipStringText() {
   final StringText text = new StringText();
    text.parse(getMaterialSpecificText());
    text.parse(getQualifyingDescription());

    if ((getTargetBibItemNumber() > 0)) {
      text.addSubfield(new Subfield("w", "" + getTargetBibItemNumber()));
    }
    return text;
  }

  /**
   * Sets the relationship string text.
   *
   * @param text the new relationship string text
   */
  public void setRelationshipStringText(StringText text) {
    setMaterialSpecificText(text.getSubfieldsWithCodes("3").toString());
    setDescription(text.getSubfieldsWithCodes("ginq4").toString());
    setQualifyingDescription(text.getSubfieldsWithCodes("c").toString());
  }

  /**
   * Gets the relation type code.
   *
   * @return the relation type code
   */
  public int getRelationTypeCode() {
    return relationTypeCode;
  }

  /**
   * Sets the relation type code.
   *
   * @param i the new relation type code
   */
  public void setRelationTypeCode(int i) {
    relationTypeCode = i;
  }

  /**
   * Gets the second correlation list.
   *
   * @param value1 the value 1
   * @return the second correlation list
   * @throws DataAccessException the data access exception
   */
  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
   */
  public List getSecondCorrelationList(short value1) {
    return Collections.emptyList();
  }

  /**
   * Gets the string text.
   *
   * @return the string text
   */
  public StringText getStringText() {
    return stringText;
  }

  /**
   * Sets the string text.
   *
   * @param text the new string text
   */
  public void setStringText(StringText text) {
    stringText = text.getSubfieldsWithoutCodes("cgnw3");
  }

  /**
   * Gets the string text string.
   *
   * @return the string text string
   */
  public String getStringTextString() {
    return getStringText().toString();
  }

  /**
   * Sets the string text string.
   *
   * @param string the new string text string
   */
  public void setStringTextString(String string) {
    if (string != null) {
      setStringText(new StringText(string));
    }
  }

  /**
   * Gets the target bib item number.
   *
   * @return the target bib item number
   */
  public int getTargetBibItemNumber() {
    return targetBibItemNumber;
  }

  /**
   * Sets the target bib item number.
   *
   * @param i the new target bib item number
   */
  public void setTargetBibItemNumber(int i) {
    targetBibItemNumber = i;
  }

  /**
   * Gets the third correlation list.
   *
   * @param value1 the value 1
   * @param value2 the value 2
   * @return the third correlation list
   * @throws DataAccessException the data access exception
   */
  @Override
  public List getThirdCorrelationList(int value1, int value2) {
    return Collections.emptyList();
  }

  /**
   * Gets the user view string.
   *
   * @return the user view string
   */
  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }

  /**
   * Sets the user view string.
   *
   * @param string the new user view string
   */
  public void setUserViewString(String string) {
    userViewHelper.setUserViewString(string);
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode() + targetBibItemNumber + relationTypeCode;
  }

  /**
   * Checks if is browsable.
   *
   * @return true, if is browsable
   */
  @Override
  public boolean isBrowsable() {
    return false;
  }

  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  @Override
  public StringText addPunctuation() {
    final StringText result = new StringText(getStringText().toString());
    try {
      result.addTerminalPunctuation("a", ".?!-)]", ".");
      result.addTerminalPunctuation("b", "/=:;.,", ".");
      result.removePrecedingPunctuation("x", ",");
      return result;
    } catch (Exception e) {
      return result;
    }
  }
}
