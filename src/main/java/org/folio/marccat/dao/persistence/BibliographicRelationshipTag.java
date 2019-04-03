package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.SubfieldCodeComparator;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOBibliographicRelationship;
import org.folio.marccat.dao.DAOBibliographicRelationshipTag;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.folio.marccat.util.F.deepCopy;

public class BibliographicRelationshipTag extends VariableField implements PersistentObjectWithView {
  private static final Log logger = LogFactory.getLog(BibliographicRelationshipTag.class);
  public BibliographicRelationship sourceRelationship;
  public BibliographicRelationship targetRelationship;
  private StringText reciprocalStringText = null;
  private int reciprocalOption = -1; // stringValue -1 triggers calculation on construction
  private BibliographicRelationshipTag originalTag = null;

  public BibliographicRelationshipTag() {
    super();
    setSourceRelationship(new BibliographicRelationship());
    setTargetRelationship(new BibliographicRelationship());
    setReciprocalOption((short) 3);
    setPersistenceState(new PersistenceState());
    setOriginalTag();
  }

  public BibliographicRelationshipTag(BibliographicRelationship relationship, int userView, Session session) {
    super();
    setSourceRelationship(relationship);
    setTargetRelationship(handleReciprocalRelationship(userView, session));
    setReciprocalOption(getReciprocalOption(userView, session));
    buildReciprocalStringText(userView, session);
    setPersistenceState(new PersistenceState());
    setOriginalTag();
  }

  public void buildReciprocalStringText(int userView, Session session) {
    StringText s = new StringText();
    if (getSourceRelationship().getTargetBibItemNumber() > 0) {
      DAOBibliographicRelationship b = new DAOBibliographicRelationship();
      s = b.buildRelationStringText(sourceRelationship.getTargetBibItemNumber(), userView, session);
    }
    setReciprocalStringText(s);
  }

  public void createTargetBibItem(int userView, Session session) {
    if (getReciprocalOption(userView, session) == 1) {
      /* create the reciprocal relationship */
      logger.debug("create reciprocal relationship");
      DAOBibliographicRelationshipTag b = new DAOBibliographicRelationshipTag();

      if (targetRelationship != null) {
        targetRelationship.evict();
      }
      targetRelationship = new BibliographicRelationship();
      targetRelationship.setItemNumber(getTargetBibItemNumber());
      targetRelationship.setTargetBibItemNumber(sourceRelationship.getItemNumber());
      targetRelationship.setUserViewString(sourceRelationship.getUserViewString());
      targetRelationship.setRelationTypeCode(b.getReciprocalType(sourceRelationship.getRelationTypeCode()));
      targetRelationship.markNew();
    }
  }

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

  public void generateNewBlindRelationshipKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setTargetBibItemNumber(-dao.getNextNumber("BR", session));
  }

  public int getCategory() {
    return sourceRelationship.getCategory();
  }

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

  public CorrelationValues getCorrelationValues() {
    return (new CorrelationValues()).change(1, getRelationTypeCode()).change(2, getRelationPrintNoteCode());
  }

  public void setCorrelationValues(CorrelationValues v) {
    setRelationTypeCode(v.getValue(1));
    setRelationPrintNoteCode(v.getValue(2));
  }

  public AbstractDAO getDAO() {
    return new DAOBibliographicRelationshipTag();
  }

  @Deprecated
  public List getFirstCorrelationList() {
    return Collections.emptyList();
  }

  @Override
  public int getItemNumber() {
    return sourceRelationship.getItemNumber();
  }

  @Override
  public void setItemNumber(int i) {
    sourceRelationship.setItemNumber(i);
  }

  @Deprecated
  public List getReciprocalList() {
    return Collections.emptyList();
  }

  public int getReciprocalOption(int userView, Session session) {
    logger.debug("running getReciprocalOption(with a view)");
    logger.debug("starting option is " + getReciprocalOption());
    if (getReciprocalOption() <= 0) {

      DAOBibliographicRelationship b = new DAOBibliographicRelationship();

      if (this.getTargetBibItemNumber() < 0) {
        return 3;
      } else {
        try {
          return b.getReciprocalBibItem(
            this.getTargetBibItemNumber(),
            this.getItemNumber(), userView, session);
        } catch (DataAccessException ex) {
          return -1;
        }
      }

    } else {
      return getReciprocalOption();
    }
  }

  public StringText getReciprocalStringText() {
    return reciprocalStringText;
  }

  public void setReciprocalStringText(StringText text) {
    reciprocalStringText = text;
  }

  public int getReciprocalOption() {
    return reciprocalOption;
  }

  public void setReciprocalOption(int s) {
    logger.debug("Setting reciprocalOption to " + s);
    reciprocalOption = s;
    if (!BibliographicRelationReciprocal.isReciprocal(s)) {
      setTargetRelationship(null);
    }
    if (BibliographicRelationReciprocal.isBlind(s) && getTargetBibItemNumber() > 0) {
        setTargetBibItemNumber(0);
    }
  }

  public int getRelationPrintNoteCode() {
    return sourceRelationship.getRelationPrintNoteCode();
  }

  public void setRelationPrintNoteCode(int i) {
    sourceRelationship.setRelationPrintNoteCode(i);
  }

  public StringText getRelationshipStringText() {
    sourceRelationship.getStringText().parse(sourceRelationship.getMaterialSpecificText());
    sourceRelationship.getStringText().parse(sourceRelationship.getDescription());
    sourceRelationship.getStringText().parse(sourceRelationship.getQualifyingDescription());

    if ((getTargetBibItemNumber() > 0)) {
      sourceRelationship.getStringText().addSubfield(new Subfield("w", ("" + getTargetBibItemNumber())));
    }
    return sourceRelationship.getStringText();
  }

  public void setRelationshipStringText(StringText text) {
    sourceRelationship.setRelationshipStringText(text);
  }

  public int getRelationTypeCode() {
    return sourceRelationship.getRelationTypeCode();
  }

  public void setRelationTypeCode(int i) {
    sourceRelationship.setRelationTypeCode(i);
  }


  @Deprecated
  public List getSecondCorrelationList(short value1) {
    return Collections.emptyList();
  }

  public BibliographicRelationship getSourceRelationship() {
    return sourceRelationship;
  }

  public void setSourceRelationship(BibliographicRelationship relationship) {
    sourceRelationship = relationship;
  }

  public StringText getStringText() {
    StringText text = new StringText();
    /*Bug 4122 4157 inizio */
    text.parse(sourceRelationship.getDescription());
    /*Bug 4122 4157 fine */

    if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
      text.add(sourceRelationship.getStringText());
    } else {
      text.add(getReciprocalStringText());
    }
    text.add(sourceRelationship.getRelationshipStringText());
    text.orderSubfields("astpbcmdefknruxzygw");
    return text;
  }

  public void setStringText(StringText text) {
    sourceRelationship.setRelationshipStringText(text);
    if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
      sourceRelationship.setStringText(text);
    }
  }

  public String getStringTextString() {
    return getStringText().toString();
  }

  private void setStringTextString(String string) {
    if (string != null) {
      setStringText(new StringText(string));
    }
  }

  public int getTargetBibItemNumber() {
    return sourceRelationship.getTargetBibItemNumber();
  }

  public void setTargetBibItemNumber(int i) {
    sourceRelationship.setTargetBibItemNumber(i);
  }

  public BibliographicRelationship getTargetRelationship() {
    return targetRelationship;
  }

  public void setTargetRelationship(BibliographicRelationship relationship) {
    targetRelationship = relationship;
  }

  @Override
  public List getThirdCorrelationList(int value1, int value2) {
    return Collections.emptyList();
  }

  public String getUserViewString() {
    return sourceRelationship.getUserViewString();
  }

  public void setUserViewString(String string) {
    sourceRelationship.setUserViewString(string);
    if (targetRelationship != null) {
      targetRelationship.setUserViewString(string);
    }
  }

  public BibliographicRelationship handleReciprocalRelationship(int userView, Session session) {
    DAOBibliographicRelationship b = new DAOBibliographicRelationship();
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode() + getTargetBibItemNumber() + getRelationTypeCode();
  }

  @Override
  public boolean isBrowsable() {
    return false;
  }

  @Override
  public boolean isRelationship() {
    return true;
  }

  public void changeReciprocalOption(short s) {
    setReciprocalOption(s);
    markChanged();
  }

  public void replaceTargetRelationship(int amicusNumber, int cataloguingView, Session session) {
    getSourceRelationship().evict();
    setSourceRelationship((BibliographicRelationship) deepCopy(getSourceRelationship()));
    getSourceRelationship().markNew();
    setTargetBibItemNumber(amicusNumber);
    buildReciprocalStringText(cataloguingView, session);
    createTargetBibItem(cataloguingView, session);
    markChanged();
  }

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

  public Set getValidEditableSubfields() {
    Set set = new TreeSet(new SubfieldCodeComparator());
    if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
      set.addAll(Arrays.asList("a", "s", "t", "p", "b",
        "c", "m", "d", "e", "f", "k", "n", "q", "r", "u", "x", "z", "y",
        "g", "w", "i", "3", "4"));
    } else {
      /* Bug 4122 */
      set.addAll(Arrays.asList("c", "g", "i", "n", "q", "3", "4"));
    }
    return set;
  }

  /**
   * Converts any reciprocal relationships to blind and removes any
   * originalTag objects from the copied view.
   */
  public void copyFromAnotherItem() {
    String s = getStringTextString();
    changeReciprocalOption(BibliographicRelationReciprocal.BLIND);
    setStringTextString(s);
    setOriginalTag(); // backs up the current tag as "up-to-date" in the database
  }
}
