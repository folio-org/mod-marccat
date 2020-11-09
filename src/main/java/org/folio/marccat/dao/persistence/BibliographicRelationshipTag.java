package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.BibliographicRelationshipDAO;
import org.folio.marccat.dao.BibliographicRelationshipTagDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import java.util.*;
import static org.folio.marccat.util.F.deepCopy;


/**
 * The Class BibliographicRelationshipTag.
 */
public class BibliographicRelationshipTag extends VariableField implements PersistentObjectWithView {

  /** The source relationship. */
  private BibliographicRelationship sourceRelationship;

  /** The target relationship. */
  private BibliographicRelationship targetRelationship;

  /** The reciprocal string text. */
  private StringText reciprocalStringText = null;

  /** The reciprocal option. */
  private int reciprocalOption = -1;

  /** The original tag. */
  private BibliographicRelationshipTag originalTag = null;

  /**
   * Instantiates a new bibliographic relationship tag.
   */
  public BibliographicRelationshipTag() {
    super();
    setSourceRelationship(new BibliographicRelationship());
    setTargetRelationship(new BibliographicRelationship());
    setReciprocalOption((short) 3);
    setPersistenceState(new PersistenceState());
    setOriginalTag();
  }

  /**
   * Instantiates a new bibliographic relationship tag.
   *
   * @param relationship the relationship
   * @param userView the user view
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  public BibliographicRelationshipTag(final BibliographicRelationship relationship, final int userView, final Session session) throws HibernateException {
    super();
    setSourceRelationship(relationship);
    setTargetRelationship(handleReciprocalRelationship(userView, session));
    setReciprocalOption(getReciprocalOption(userView, session));
    buildReciprocalStringText(userView, session);
    setPersistenceState(new PersistenceState());
    setOriginalTag();
  }

  /**
   * Builds the reciprocal string text.
   *
   * @param userView the user view
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  public void buildReciprocalStringText(final int userView, final Session session) throws HibernateException {
    StringText s = new StringText();
    if (getSourceRelationship().getTargetBibItemNumber() > 0) {
      final BibliographicRelationshipDAO b = new BibliographicRelationshipDAO();
      s = b.buildRelationStringText(sourceRelationship.getTargetBibItemNumber(), userView, session);
    }
    setReciprocalStringText(s);
  }

  /**
   * Creates the target bib item.
   *
   * @param userView the user view
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  public void createTargetBibItem(final int userView, final Session session) throws HibernateException {
    if (getReciprocalOption(userView, session) == 1) {
      BibliographicRelationshipTagDAO relationshipTagDAO = new BibliographicRelationshipTagDAO();
      targetRelationship = new BibliographicRelationship();
      targetRelationship.setItemNumber(getTargetBibItemNumber());
      targetRelationship.setTargetBibItemNumber(sourceRelationship.getItemNumber());
      targetRelationship.setUserViewString(sourceRelationship.getUserViewString());
      targetRelationship.setRelationPrintNoteCode(sourceRelationship.getRelationPrintNoteCode());
      targetRelationship.setRelationTypeCode(relationshipTagDAO.getReciprocalType(sourceRelationship.getRelationTypeCode(), session));
      targetRelationship.markNew();
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
    if (!(obj instanceof BibliographicRelationshipTag))
      return false;

    BibliographicRelationshipTag other = (BibliographicRelationshipTag) obj;
    return (other.getItemNumber() == getItemNumber())
      && (other.getUserViewString().equals(getUserViewString()))
      && (other.getTargetBibItemNumber() == getTargetBibItemNumber())
      && (other.getRelationTypeCode() == getRelationTypeCode());
  }

  /**
   * Generate new blind relationship key.
   *
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  public void generateNewBlindRelationshipKey(final Session session) throws HibernateException {
    final SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setTargetBibItemNumber(-dao.getNextNumber("BR", session));
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  public int getCategory() {
    return sourceRelationship.getCategory();
  }

  /**
   * Gets the correlation.
   *
   * @param i the i
   * @return the correlation
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
    return new BibliographicRelationshipTagDAO();
  }

  /**
   * Gets the item number.
   *
   * @return the item number
   */
  @Override
  public int getItemNumber() {
    return sourceRelationship.getItemNumber();
  }

  /**
   * Sets the item number.
   *
   * @param i the new item number
   */
  @Override
  public void setItemNumber(int i) {
    sourceRelationship.setItemNumber(i);
  }

  /**
   * Gets the reciprocal option.
   *
   * @param userView the user view
   * @param session the session
   * @return the reciprocal option
   */
  public int getReciprocalOption(final int userView, final Session session) {
    if (getReciprocalOption() <= 0) {

      final BibliographicRelationshipDAO relationshipDAO = new BibliographicRelationshipDAO();

      if (this.getTargetBibItemNumber() < 0) {
        return 3;
      } else try {
        return relationshipDAO.getReciprocalBibItem(this.getTargetBibItemNumber(), this.getItemNumber(), userView, session);
      } catch (HibernateException ex) {
        return -1;
      }

    } else {
      return getReciprocalOption();
    }
  }

  /**
   * Gets the reciprocal string text.
   *
   * @return the reciprocal string text
   */
  public StringText getReciprocalStringText() {
    return reciprocalStringText;
  }

  /**
   * Sets the reciprocal string text.
   *
   * @param text the new reciprocal string text
   */
  public void setReciprocalStringText(StringText text) {
    reciprocalStringText = text;
  }

  /**
   * Gets the reciprocal option.
   *
   * @return the reciprocal option
   */
  public int getReciprocalOption() {
    return reciprocalOption;
  }

  /**
   * Sets the reciprocal option.
   *
   * @param s the new reciprocal option
   */
  public void setReciprocalOption(int s) {
    reciprocalOption = s;
    if (!BibliographicRelationReciprocal.isReciprocal(s)) {
      setTargetRelationship(null);
    }
    if (BibliographicRelationReciprocal.isBlind(s) && getTargetBibItemNumber() > 0) {
      setTargetBibItemNumber(0);
    }
  }

  /**
   * Gets the relation print note code.
   *
   * @return the relation print note code
   */
  public int getRelationPrintNoteCode() {
    return sourceRelationship.getRelationPrintNoteCode();
  }

  /**
   * Sets the relation print note code.
   *
   * @param i the new relation print note code
   */
  public void setRelationPrintNoteCode(int i) {
    sourceRelationship.setRelationPrintNoteCode(i);
  }

  /**
   * Gets the relationship string text.
   *
   * @return the relationship string text
   */
  public StringText getRelationshipStringText() {
    sourceRelationship.getStringText().parse(sourceRelationship.getMaterialSpecificText());
    sourceRelationship.getStringText().parse(sourceRelationship.getDescription());
    sourceRelationship.getStringText().parse(sourceRelationship.getQualifyingDescription());

    if ((getTargetBibItemNumber() > 0)) {
      sourceRelationship.getStringText().addSubfield(new Subfield("w", ("" + getTargetBibItemNumber())));
    }
    return sourceRelationship.getStringText();
  }

  /**
   * Sets the relationship string text.
   *
   * @param text the new relationship string text
   */
  public void setRelationshipStringText(StringText text) {
    sourceRelationship.setRelationshipStringText(text);
  }

  /**
   * Gets the relation type code.
   *
   * @return the relation type code
   */
  public int getRelationTypeCode() {
    return sourceRelationship.getRelationTypeCode();
  }

  /**
   * Sets the relation type code.
   *
   * @param i the new relation type code
   */
  public void setRelationTypeCode(int i) {
    sourceRelationship.setRelationTypeCode(i);
  }

  /**
   * Gets the source relationship.
   *
   * @return the source relationship
   */
  public BibliographicRelationship getSourceRelationship() {
    return sourceRelationship;
  }

  /**
   * Sets the source relationship.
   *
   * @param relationship the new source relationship
   */
  public void setSourceRelationship(BibliographicRelationship relationship) {
    sourceRelationship = relationship;
  }

  /**
   * Gets the string text.
   *
   * @return the string text
   */
  public StringText getStringText() {
    final  StringText text = new StringText();
    text.parse(sourceRelationship.getDescription());
    if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
      text.add(sourceRelationship.getStringText());
    } else {
      text.add(getReciprocalStringText());
    }
    text.add(sourceRelationship.getRelationshipStringText());
    text.orderSubfields("astpbcmdefknruxzygw");
    return text;
  }

  /**
   * Sets the string text.
   *
   * @param text the new string text
   */
  public void setStringText(StringText text) {
    sourceRelationship.setRelationshipStringText(text);
    if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
      sourceRelationship.setStringText(text);
    }
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
  private void setStringTextString(String string) {
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
    return sourceRelationship.getTargetBibItemNumber();
  }

  /**
   * Sets the target bib item number.
   *
   * @param i the new target bib item number
   */
  public void setTargetBibItemNumber(int i) {
    sourceRelationship.setTargetBibItemNumber(i);
  }

  /**
   * Gets the target relationship.
   *
   * @return the target relationship
   */
  public BibliographicRelationship getTargetRelationship() {
    return targetRelationship;
  }

  /**
   * Sets the target relationship.
   *
   * @param relationship the new target relationship
   */
  public void setTargetRelationship(BibliographicRelationship relationship) {
    targetRelationship = relationship;
  }

  /**
   * Gets the third correlation list.
   *
   * @param value1 the value 1
   * @param value2 the value 2
   * @return the third correlation list
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
    return sourceRelationship.getUserViewString();
  }

  /**
   * Sets the user view string.
   *
   * @param string the new user view string
   */
  public void setUserViewString(String string) {
    sourceRelationship.setUserViewString(string);
    if (targetRelationship != null) {
      targetRelationship.setUserViewString(string);
    }
  }

  /**
   * Handle reciprocal relationship.
   *
   * @param userView the user view
   * @param session the session
   * @return the bibliographic relationship
   * @throws HibernateException the hibernate exception
   */
  public BibliographicRelationship handleReciprocalRelationship(final int userView, final Session session) throws HibernateException {
    final BibliographicRelationshipDAO b = new BibliographicRelationshipDAO();
    BibliographicRelationship relationship = new BibliographicRelationship();

    try {
      if (getTargetBibItemNumber() > 0) {
        relationship = b.loadReciprocalBibItem(getTargetBibItemNumber(), getItemNumber(), userView, session);
      }
    } catch (DataAccessException de) {
      return null;
    }
    return relationship;
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode() + getTargetBibItemNumber() + getRelationTypeCode();
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
   * Checks if is relationship.
   *
   * @return true, if is relationship
   */
  @Override
  public boolean isRelationship() {
    return true;
  }

  /**
   * Change reciprocal option.
   *
   * @param s the s
   */
  public void changeReciprocalOption(short s) {
    setReciprocalOption(s);
    markChanged();
  }

  /**
   * Replace target relationship.
   *
   * @param amicusNumber the amicus number
   * @param cataloguingView the cataloguing view
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  public void replaceTargetRelationship(final int amicusNumber, final int cataloguingView, final Session session) throws HibernateException {
    setSourceRelationship((BibliographicRelationship) deepCopy(getSourceRelationship()));
    getSourceRelationship().markNew();
    setTargetBibItemNumber(amicusNumber);
    buildReciprocalStringText(cataloguingView, session);
    createTargetBibItem(cataloguingView, session);
    markChanged();
  }

  /**
   * Gets the original tag.
   *
   * @return the original tag
   */
  public BibliographicRelationshipTag getOriginalTag() {
    return originalTag;
  }

  /**
   * original tag archives the last saved values of source and target. These
   * values are needed when saving to determine how to affect the database.
   * For example, if a previously reciprocal relationship is modified to
   * become one-way then the reciprocal needs to be deleted, etc...
   *
   * @since 1.0
   */
  public void setOriginalTag() {
    originalTag = null;
    originalTag = (BibliographicRelationshipTag) deepCopy(this);
  }

  /**
   * Mark changed.
   */
  /*
   * (non-Javadoc)
   *
   * @see TagInterface#markChanged()
   */
  public void markChanged() {
    super.markChanged();
    getSourceRelationship().markChanged();
    if (getTargetRelationship() != null) {
      getTargetRelationship().markChanged();
    }
  }

  /**
   * Mark deleted.
   */
  /*
   * (non-Javadoc)
   *
   * @see TagInterface#markDeleted()
   */
  public void markDeleted() {
    super.markDeleted();
    getSourceRelationship().markDeleted();
    if (getTargetRelationship() != null) {
      getTargetRelationship().markDeleted();
    }
  }

  /**
   * Mark new.
   */
  /*
   * (non-Javadoc)
   *
   * @see TagInterface#markNew()
   */
  public void markNew() {
    super.markNew();
    getSourceRelationship().markNew();
    if (getTargetRelationship() != null) {
      getTargetRelationship().markNew();
    }
  }

  /**
   * Mark unchanged.
   */
  /*
   * (non-Javadoc)
   *
   * @see TagInterface#markUnchanged()
   */
  public void markUnchanged() {
    super.markUnchanged();
    getSourceRelationship().markUnchanged();
    if (getTargetRelationship() != null) {
      getTargetRelationship().markUnchanged();
    }
  }

  /**
   * Reinstate deleted tag.
   */
  /*
   * (non-Javadoc)
   *
   * @see TagInterface#reinstateDeletedTag()
   */
  @Override
  public void reinstateDeletedTag() {
    super.reinstateDeletedTag();
    setOriginalTag();
  }



  /**
   * Converts any reciprocal relationships to blind and removes any
   * originalTag objects from the copied view.
   */
  public void copyFromAnotherItem() {
    String s = getStringTextString();
    changeReciprocalOption(BibliographicRelationReciprocal.BLIND);
    setStringTextString(s);
    setOriginalTag();
  }
}
