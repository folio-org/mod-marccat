package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOBibliographicRelationship;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.List;

@SuppressWarnings("unused")
public class BibliographicRelationship extends VariableField implements PersistentObjectWithView {
  private static final long serialVersionUID = -5945143318867728785L;
  //private static final Log logger = LogFactory.getLog(BibliographicRelationship.class);
  private static final short bibliographicRelationshipCategory = 8;
  private int targetBibItemNumber;
  private int relationTypeCode;
  private int relationPrintNoteCode;
  private String description = null;
  private String qualifyingDescription = null;
  private StringText stringText = null;
  private String stringTextString = null;
  private String materialSpecificText = null;
  private int reciprocalType;
  private UserViewHelper userViewHelper = new UserViewHelper();

  public BibliographicRelationship() {
    super();
    StringText s = new StringText("");
    setStringText(s);
    setReciprocalType((short) -1);
    //setRelationTypeCode(Defaults.getShort("bibliographicRelationship.relationTypeCode"));
    //setRelationPrintNoteCode(Defaults.getShort("bibliographicRelationship.relationPrintNoteCode"));
    //setPersistenceState(new PersistenceState());
  }

  public BibliographicRelationship(int itemNbr) {
    super(itemNbr);
  }

  public StringText BuildStringText(int userView) {
    /* stringtext can be in table or should be build from other tables */
    StringText s = new StringText();
    DAOBibliographicRelationship b = new DAOBibliographicRelationship();
    try {
      s = b.buildRelationStringText(this.getTargetBibItemNumber(), userView);
      s.add(getRelationshipStringText());
      return s;
    } catch (DataAccessException ex) {
      return stringText;
    }
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof BibliographicRelationship))
      return false;
    BibliographicRelationship other = (BibliographicRelationship) obj;
    return (other.getItemNumber() == getItemNumber())
      && (other.getUserViewString().equals(getUserViewString()))
      && (other.getTargetBibItemNumber() == getTargetBibItemNumber())
      && (other.getRelationTypeCode() == getRelationTypeCode());
  }

  public void generateNewBlindRelationshipKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setTargetBibItemNumber(-dao.getNextNumber("BR", session));
  }

  public int getBibItemNumber() {
    return getItemNumber();
  }

  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
   */
  public int getCategory() {
    return bibliographicRelationshipCategory;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelation(int)
   */
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

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setRelationTypeCode(v.getValue(1));
    setRelationPrintNoteCode(v.getValue(2));
  }

  public AbstractDAO getDAO() {
    return new DAOBibliographicRelationship();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String text) {
    description = text;
  }

  public List getFirstCorrelationList() throws DataAccessException {
    /* return getDaoCodeTable().getList(BibliographicRelationType.class,true); */
    return null;
  }

  public String getMaterialSpecificText() {
    return materialSpecificText;
  }

  public void setMaterialSpecificText(String text) {
    materialSpecificText = text;
  }

  public String getQualifyingDescription() {
    return qualifyingDescription;
  }

  public void setQualifyingDescription(String text) {
    qualifyingDescription = text;
  }

  public List getReciprocalList() throws DataAccessException {
    /*return getDaoCodeTable().getList(BibliographicRelationReciprocal.class,true);*/
    return null;
  }

  public int getReciprocalOption() throws DataAccessException {
    if (getReciprocalType() == -1) {
      DAOBibliographicRelationship b = new DAOBibliographicRelationship();
      if (this.getTargetBibItemNumber() < 0) {
        return 3;
      } else {
        try {
          /* TODO pass the correct userview*/
          return b.getReciprocalBibItem(this.getTargetBibItemNumber(), this.getItemNumber(), 1);
        } catch (DataAccessException ex) {
          return -1;
        }
      }
    } else {
      return getReciprocalType();
    }
  }

  public void setReciprocalOption(int s) {
    setReciprocalType(s);
  }

  public int getReciprocalType() {
    return reciprocalType;
  }

  public void setReciprocalType(int s) {
    reciprocalType = s;
  }

  public int getRelationPrintNoteCode() {
    return relationPrintNoteCode;
  }

  public void setRelationPrintNoteCode(int i) {
    relationPrintNoteCode = i;
  }

  public StringText getRelationshipStringText() {
    StringText text = new StringText();
    text.parse(getMaterialSpecificText());
    text.parse(getQualifyingDescription());

    if ((getTargetBibItemNumber() > 0)) {
      text.addSubfield(new Subfield("w", new String("" + getTargetBibItemNumber())));
    }
    return text;
  }

  public void setRelationshipStringText(StringText text) {
    setMaterialSpecificText(text.getSubfieldsWithCodes("3").toString());
    setDescription(text.getSubfieldsWithCodes("ginq4").toString());
    setQualifyingDescription(text.getSubfieldsWithCodes("c").toString());
  }

  public int getRelationTypeCode() {
    return relationTypeCode;
  }

  public void setRelationTypeCode(int i) {
    relationTypeCode = i;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
   */
  public List getSecondCorrelationList(short value1) throws DataAccessException {
    /* return getDaoCodeTable().getList(BibliographicRelationPrintNote.class,true); */
    return null;
  }

  public StringText getStringText() {
    return stringText;
  }

  public void setStringText(StringText text) {
    stringText = text.getSubfieldsWithoutCodes("cgnw3");
  }

  public String getStringTextString() {
    return getStringText().toString();
  }

  public void setStringTextString(String string) {
    if (string != null) {
      stringTextString = string;
      setStringText(new StringText(string));
    }
  }

  public int getTargetBibItemNumber() {
    return targetBibItemNumber;
  }

  public void setTargetBibItemNumber(int i) {
    targetBibItemNumber = i;
  }

  public List getThirdCorrelationList(int value1, int value2)
    throws DataAccessException {
    return null;
  }

  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }

  public void setUserViewString(String string) {
    userViewHelper.setUserViewString(string);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return super.hashCode() + targetBibItemNumber + relationTypeCode;
  }

  public boolean isBrowsable() {
    return false;
  }
}
