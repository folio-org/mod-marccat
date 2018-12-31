package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.FixedField;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.T_SINGLE;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ValidationException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static org.folio.marccat.util.F.deepCopy;

/**
 * The Class Tag.
 */
public abstract class Tag implements Serializable, Cloneable, TagInterface {

  /** The Constant PHYSICAL_MATERIAL. */
  public static final int PHYSICAL_MATERIAL = 1;

  /** The logger. */
  private static Log logger = LogFactory.getLog(Tag.class);

  /** The persistence state. */
  protected PersistenceState persistenceState;

  /** The tag impl. */
  private TagImpl tagImpl;

  /** The item number. */
  private int itemNumber = -1;

  /** The correlation key. */
  private CorrelationKey correlationKey;


  /** The validation. */
  private Validation validation;

  /** The new subfield content. */
  private String newSubfieldContent;

  /**
   * Instantiates a new tag.
   */
  public Tag() {
    /*
     * This default implementation can be overridden either in individual tag
     * constructors or when the tag is added to a CatalogItem
     */
    setTagImpl(TagImplFactory.getDefaultImplementation());
  }

  /**
   * Instantiates a new tag.
   *
   * @param itemNumber the item number
   */
  public Tag(int itemNumber) {
    this();
    setItemNumber(itemNumber);
  }

  /**
   * Gets the heading type.
   *
   * @return the heading type
   */
  public String getHeadingType() {
    return tagImpl.getHeadingType(this);
  }

  /**
   * Gets the catalog.
   *
   * @return the catalog
   */
  public Catalog getCatalog() {
    return tagImpl.getCatalog();
  }

  /**
   * indicates whether the proposed change in correlation values would result in a
   * new persistent key for this tag.  Values of -1 are ignored.
   *
   * @param value1 the value 1
   * @param value2 the value 2
   * @param value3 the value 3
   * @return true, if successful
   */
  final public boolean correlationChangeAffectsKey(
    int value1,
    int value2,
    int value3) {
    return correlationChangeAffectsKey(
      new CorrelationValues(value1, value2, value3));
  }

  /**
   * Correlation change affects key.
   *
   * @param v the v
   * @return true, if successful
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return false;
  }

  /**
   * Checks if is publisher.
   *
   * @return true if tag is a publisher (260)
   */
  public boolean isPublisher() {
    return false;
  }

  /**
   * return true if tag is a note.
   *
   * @return true, if is note
   */
  public boolean isNote() {
    return false;
  }

  /**
   * return true if tag is a relationship.
   *
   * @return true, if is relationship
   */
  public boolean isRelationship() {
    return false;
  }

  /**
   * Gets the required edit permission.
   *
   * @return the name of the permission that is required if this tag is
   * allowed to be edited -- to be overridden in concrete classes where needed
   */
  public String getRequiredEditPermission() {
    return "basicCataloguing";
  }

  /**
   * Gets the correlation.
   *
   * @param i - the correlation stringValue wanted (1, 2, or 3)
   * @return the appropriate correlation stringValue for determining MARC coding (-1 if no
   * stringValue is available or known)
   */
  public int getCorrelation(int i) {
    return getCorrelationValues().getValue(i);
  }

  /**
   * sets the given correlation stringValue for this tag.
   *
   * @param i - the index to be set (1, 2, or 3)
   * @param s - the new stringValue
   * @since 1.0
   */
  final public void setCorrelation(int i, int s) {
    setCorrelationValues(getCorrelationValues().change(i, s));
  }

  /**
   * Gets the marc encoding.
   *
   * @return the MARC tag and indicators for this tag
   * @throws DataAccessException the data access exception
   */
  public CorrelationKey getMarcEncoding() throws DataAccessException {
    return correlationKey;
  }

  /**
   * Gets the marc encoding.
   *
   * @param session the session
   * @return the MARC tag and indicators for this tag
   * @throws DataAccessException the data access exception
   */
  public CorrelationKey getMarcEncoding(final Session session) throws DataAccessException {
    correlationKey = tagImpl.getMarcEncoding(this, session);
    return correlationKey;
  }

  /**
   * Sets the correlation key.
   *
   * @param correlationK the new correlation key
   * @throws DataAccessException the data access exception
   */
  public void setCorrelationKey(final CorrelationKey correlationK) throws DataAccessException {
    correlationKey = correlationK;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  /*
   * Note that this version of equals is a default implementation equating any two
   * tags of the same class and itemNumber.  Subclasses should override where
   * required
   */
  public boolean equals(Object obj) {
    if (obj != null && !(obj.getClass().equals(this.getClass())))
      return false;
    Tag other = (Tag) obj;
    return (other != null && other.getItemNumber() == this.getItemNumber());
  }

  /**
   * 2018 Paul Search Engine Java Adds this tag to the XML record with
   * punctuation for MARC export.
   *
   * @param xmlDocument the xml document
   * @return the element
   */
  public Element toExternalMarcSlim(Document xmlDocument) {
    return toXmlElement(xmlDocument, true);
  }

  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  public StringText addPunctuation() throws Exception{
    // overridden in subclasses -- default implementation does nothing
    return null;
  }


  /**
   * To xml element.
   *
   * @param xmlDocument the xml document
   * @param withPunctuation the with punctuation
   * @return the element
   */
  private Element toXmlElement(Document xmlDocument, boolean withPunctuation) {
    CorrelationKey marcEncoding = null;
    try {
      marcEncoding = getMarcEncoding();
    } catch (Exception exception) {
      logger.warn("Invalid tag found in Tag.toXmlElement");
      return xmlDocument.createElement("error");
    }

    String marcTag = marcEncoding.getMarcTag();
    String marcFirstIndicator = "" + marcEncoding.getMarcFirstIndicator();
    String marcSecondIndicator = "" + marcEncoding.getMarcSecondIndicator();

    Element field = null;
    if (isFixedField()) {
      field = xmlDocument.createElement("controlfield");
    } else {
      field = xmlDocument.createElement("datafield");
    }
    field.setAttribute("tag", marcTag);
    if (isFixedField()) {
      Node text = xmlDocument.createTextNode(((FixedField) this)
        .getDisplayString());
      field.appendChild(text);
    } else {
      field.setAttribute("ind1", marcFirstIndicator);
      field.setAttribute("ind2", marcSecondIndicator);
      StringText st;
      if (withPunctuation) {
        try {
          st = addPunctuation();
        } catch (Exception e) {
          logger.warn(
            "ErrorCollection adding punctuation, using original text", e);
          st = ((VariableField) this).getStringText();
        }
      } else {
        st = ((VariableField) this).getStringText();
      }
      for (Iterator subfieldIterator = st.getSubfieldList().iterator(); subfieldIterator
        .hasNext(); ) {
        Subfield subfield = (Subfield) subfieldIterator.next();
        field.appendChild(subfield.toXmlElement(xmlDocument));
      }
    }
    return field;

  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getItemNumber();
  }

  /**
   * After a change in correlation stringValue 1, the available choices for values 2 and
   * 3 are recalculated and the values are reset (to the first available valid choice).
   *
   * @param s the new value1
   * @throws DataAccessException the data access exception
   */
  public void updateFirstCorrelation(int s) throws DataAccessException {
    setCorrelation(1, s);
    List l = getSecondCorrelationList(s);
    if (l != null) {
      updateSecondCorrelation(((T_SINGLE) l.get(0)).getCode());
    }
  }

  /**
   * After a change in correlation stringValue 2, the available choices for values 3
   * are recalculated and the stringValue is reset (to the first available valid choice).
   *
   * @param s the new stringValue 2
   * @throws DataAccessException the data access exception
   */
  public void updateSecondCorrelation(int s) throws DataAccessException {
    setCorrelation(2, s);
    List l = getThirdCorrelationList(getCorrelation(1), getCorrelation(2));
    if (l != null) {
      setCorrelation(3, ((T_SINGLE) l.get(0)).getCode());
    }
  }

  /**
   * Generate new key.
   *
   * @param session the session
   * @throws DataAccessException the data access exception
   * @throws HibernateException the hibernate exception
   * @throws SQLException the SQL exception
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException, SQLException {
  }

  /**
   * Gets the persistence state.
   *
   * @return the persistence state
   */
  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  /**
   * Sets the persistence state.
   *
   * @param object the new persistence state
   */
  public void setPersistenceState(PersistenceState object) {
    persistenceState = object;
  }

  /**
   * Gets the update status.
   *
   * @return the update status
   */
  public int getUpdateStatus() {
    if (persistenceState == null) {
      return -1;
    } else {
      return persistenceState.getUpdateStatus();
    }
  }

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(int i) {
    if (persistenceState != null) {
      persistenceState.setUpdateStatus(i);
    }
  }

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  public boolean isChanged() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isChanged();
    }
  }

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  public boolean isDeleted() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isDeleted();
    }
  }

  /**
   * Checks if is new.
   *
   * @return true, if is new
   */
  public boolean isNew() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isNew();
    }
  }

  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   */
  public boolean isRemoved() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isRemoved();
    }
  }

  /**
   * Mark changed.
   */
  public void markChanged() {
    if (persistenceState != null) {
      persistenceState.markChanged();
    }
  }

  /**
   * Mark deleted.
   */
  public void markDeleted() {
    if (persistenceState != null) {
      persistenceState.markDeleted();
    }
  }

  /**
   * Mark new.
   */
  public void markNew() {
    if (persistenceState != null) {
      persistenceState.markNew();
    }
  }

  /**
   * Mark unchanged.
   */
  public void markUnchanged() {
    if (persistenceState != null) {
      persistenceState.markUnchanged();
    }
  }

  /**
   * On delete.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onDelete(arg0);
    } else {
      return true;
    }
  }

  /**
   * On load.
   *
   * @param arg0 the arg 0
   * @param arg1 the arg 1
   */
  public void onLoad(Session arg0, Serializable arg1) {
    if (persistenceState != null) {
      persistenceState.onLoad(arg0, arg1);
    }
  }

  /**
   * On save.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onSave(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onSave(arg0);
    } else {
      return true;
    }
  }

  /**
   * On update.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onUpdate(arg0);
    } else {
      return true;
    }
  }

  /**
   * Evict.
   *
   * @throws DataAccessException the data access exception
   */
  public void evict() throws DataAccessException {
    if (persistenceState != null) {
      persistenceState.evict(this);
    }
  }

  /**
   * This method creates a XML Document as follows
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * or for a control field
   * <controlfield tag="001">000000005581</controlfield>.
   *
   * @return a Document
   */
  public Document toXmlDocument() {
    DocumentBuilderFactory documentBuilderFactory =
      DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    Document xmlDocument = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      xmlDocument = documentBuilder.newDocument();
      xmlDocument.appendChild(toXmlElement(xmlDocument));
    } catch (ParserConfigurationException parserConfigurationException) {
      logger.error("", parserConfigurationException);
    }
    return xmlDocument;
  }

  /**
   * This method creates a XML Element as follows
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * or for a control field
   * <controlfield tag="001">000000005581</controlfield>.
   *
   * @param xmlDocument the xml document
   * @return an Element
   */
  public Element toXmlElement(Document xmlDocument) {
    CorrelationKey marcEncoding = null;
    try {
      marcEncoding = getMarcEncoding();
    } catch (Exception exception) {
      throw new RuntimeException("Invalid tag found in Tag.toXmlElement");
    }

    String marcTag = marcEncoding.getMarcTag();
    String marcFirstIndicator = "" + marcEncoding.getMarcFirstIndicator();
    String marcSecondIndicator = "" + marcEncoding.getMarcSecondIndicator();

    Element field = null;
    if (isFixedField()) {
      if (marcTag.equals("000"))
        field = xmlDocument.createElement("leader");
      else
        field = xmlDocument.createElement("controlfield");
    } else {
      field = xmlDocument.createElement("datafield");
    }
    field.setAttribute("tag", marcTag);
    if (!isFixedField()) {
      field.setAttribute("ind1", marcFirstIndicator);
      field.setAttribute("ind2", marcSecondIndicator);
      for (Iterator subfieldIterator =
           ((VariableField) this)
             .getStringText()
             .getSubfieldList()
             .iterator();
           subfieldIterator.hasNext();
        ) {
        Subfield subfield = (Subfield) subfieldIterator.next();
        field.appendChild(subfield.toXmlElement(xmlDocument));
      }
    }
    return field;


  }


  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  public Object clone() {
    return deepCopy(this);
  }

  /**
   * Gets the tag impl.
   *
   * @return the tag impl
   */
  public TagImpl getTagImpl() {
    return tagImpl;
  }

  /**
   * Sets the tag impl.
   *
   * @param impl the new tag impl
   */
  public void setTagImpl(TagImpl impl) {
    tagImpl = impl;
  }

  /**
   * Gets the item number.
   *
   * @return the item number
   */
  public int getItemNumber() {
    return itemNumber;
  }

  /**
   * Sets the item number.
   *
   * @param itemNumber the new item number
   */
  public void setItemNumber(int itemNumber) {
    this.itemNumber = itemNumber;
  }

  /**
   * Gets the validation.
   *
   * @return the validation
   * @throws DataAccessException the data access exception
   */
  public Validation getValidation() throws DataAccessException {
    return validation;
  }

  /**
   * Sets the validation.
   *
   * @param validation the new validation
   */
  public void setValidation(Validation validation) {
    this.validation = validation;
  }

  /**
   * Gets the validation.
   *
   * @param session the session
   * @return the validation
   * @throws DataAccessException the data access exception
   */
  public Validation getValidation(final Session session) throws DataAccessException {
    validation = tagImpl.getValidation(this, session);
    return validation;
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  abstract public int getCategory();

  /**
   * Checks if is checks for subfield W.
   *
   * @return true, if is checks for subfield W
   */
  /* (non-Javadoc)
   * @see TagInterface#isHasSubfieldW()
   */
  public boolean isHasSubfieldW() {
    return false; //default implementation
  }


  /**
   * Checks if is equivalence reference.
   *
   * @return true, if is equivalence reference
   */
  @Override
  public boolean isEquivalenceReference() {
    return false;
  }

  /**
   * Gets the display category.
   *
   * @return the display category
   */
  /* (non-Javadoc)
   * @see TagInterface#getDisplayCategory()
   */
  public int getDisplayCategory() {
    return getCategory();
  }

  /**
   * Gets the displays heading type.
   *
   * @return the displays heading type
   */
  /* (non-Javadoc)
   * @see TagInterface#getDisplaysHeadingType()
   */
  public boolean getDisplaysHeadingType() {
    return false;
  }

  /**
   * Validate.
   *
   * @param index the index
   * @throws ValidationException the validation exception
   */
  /* (non-Javadoc)
   * @see TagInterface#validate()
   */
  public void validate(int index) throws ValidationException {
    // default implementation does nothing
  }

  /**
   * Called where a
   * series of changes result in returning the new key back
   * to a pre-existing key.
   */
  public void reinstateDeletedTag() {
    markUnchanged();
    markChanged();
  }

  /**
   * Gets the new subfield content.
   *
   * @return the new subfield content
   */
  public String getNewSubfieldContent() {
    return newSubfieldContent;
  }

  /**
   * Sets the new subfield content.
   *
   * @param newSubfieldContent the new new subfield content
   */
  public void setNewSubfieldContent(String newSubfieldContent) {
    this.newSubfieldContent = newSubfieldContent;
  }

  /**
   * Gets the physical.
   *
   * @return the physical
   */
  public int getPhysical() {
    return PHYSICAL_MATERIAL;
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }
}
