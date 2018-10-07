package org.folio.cataloging.business.cataloguing.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.persistence.CorrelationKey;
import org.folio.cataloging.dao.persistence.T_SINGLE;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import static org.folio.cataloging.F.deepCopy;

//import org.folio.cataloging.dao.DAOCodeTable;
//import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class Tag implements Serializable, Cloneable, TagInterface {

  public static final int PHYSICAL_MATERIAL = 1;
  public static final int NOT_PHYSICAL_MATERIAL = 0;
  private static Log logger = LogFactory.getLog (Tag.class);
  protected PersistenceState persistenceState;
  private TagImpl tagImpl;
  private int itemNumber = -1;
  private CorrelationKey correlationKey;


  private Validation validation;
  private String newSubfieldContent;

  public Tag() {
    /*
     * This default implementation can be overridden either in individual tag
     * constructors or when the tag is added to a CatalogItem
     */
    setTagImpl (TagImplFactory.getDefaultImplementation ( ));
  }

  public Tag(int itemNumber) {
    this ( );
    setItemNumber (itemNumber);
  }

  public String getHeadingType() {
    return tagImpl.getHeadingType (this);
  }

  public Catalog getCatalog() {
    return tagImpl.getCatalog ( );
  }

  /**
   * indicates whether the proposed change in correlation values would result in a
   * new persistent key for this tag.  Values of -1 are ignored.
   */
  final public boolean correlationChangeAffectsKey(
    int value1,
    int value2,
    int value3) {
    return correlationChangeAffectsKey (
      new CorrelationValues (value1, value2, value3));
  }

  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return false;
  }

  /**
   * @return true if tag is a publisher (260)
   */
  public boolean isPublisher() {
    return false;
  }

  /**
   * return true if tag is a note
   */
  public boolean isNote() {
    return false;
  }

  /**
   * return true if tag is a relationship
   */
  public boolean isRelationship() {
    return false;
  }

  /**
   * @return the name of the permission that is required if this tag is
   * allowed to be edited -- to be overridden in concrete classes where needed
   */
  public String getRequiredEditPermission() {
    return "basicCataloguing";
  }

  /**
   * @param i - the correlation stringValue wanted (1, 2, or 3)
   * @return the appropriate correlation stringValue for determining MARC coding (-1 if no
   * stringValue is available or known)
   */
  public int getCorrelation(int i) {
    return getCorrelationValues ( ).getValue (i);
  }

  /**
   * sets the given correlation stringValue for this tag
   *
   * @param i - the index to be set (1, 2, or 3)
   * @param s - the new stringValue
   * @since 1.0
   */
  final public void setCorrelation(int i, int s) {
    setCorrelationValues (getCorrelationValues ( ).change (i, s));
  }

  /**
   * @return the MARC tag and indicators for this tag
   */
  public CorrelationKey getMarcEncoding() throws DataAccessException {
    return correlationKey;
  }

  /**
   * @return the MARC tag and indicators for this tag
   */
  public CorrelationKey getMarcEncoding(final Session session) throws DataAccessException {
    correlationKey = tagImpl.getMarcEncoding (this, session);
    return correlationKey;
  }

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
    if (!(obj.getClass ( ).equals (this.getClass ( ))))
      return false;
    Tag other = (Tag) obj;
    return (other.getItemNumber ( ) == this.getItemNumber ( ));
  }

  /**
   * 2018 Paul Search Engine Java Adds this tag to the XML record with
   * punctuation for MARC export
   *
   * @param xmlDocument
   * @return
   */
  public Element toExternalMarcSlim(Document xmlDocument) {
    return toXmlElement (xmlDocument, true);
  }

  // 2018 Paul Search Engine Java
  public StringText addPunctuation() {
    // overridden in subclasses -- default implementation does nothing
    return null;
  }

  // 2018 Paul Search Engine Java
  private Element toXmlElement(Document xmlDocument, boolean withPunctuation) {
		/*CorrelationKey marcEncoding = null;
		try {
			marcEncoding = getMarcEncoding();
		} catch (Exception exception) {
			logger.warn("Invalid tag found in Tag.toXmlElement");
			return xmlDocument.createElement("error");
		}

		String marcTag = marcEncoding.getMarcTag();
		String marcFirstIndicator = new String(""
				+ marcEncoding.getMarcFirstIndicator());
		String marcSecondIndicator = new String(""
				+ marcEncoding.getMarcSecondIndicator());

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
					// if addPunctuation fails, we want to proceed using the raw
					// string text
					logger.warn(
							"ErrorCollection adding punctuation, using original text", e);
					st = ((VariableField) this).getStringText();
				}
			} else {
				st = ((VariableField) this).getStringText();
			}
			for (Iterator subfieldIterator = st.getSubfieldList().iterator(); subfieldIterator
					.hasNext();) {
				Subfield subfield = (Subfield) subfieldIterator.next();
				field.appendChild(subfield.toXmlElement(xmlDocument));
			}
		}
		return field;*/
    return null;
  }

  public int hashCode() {
    return getItemNumber ( );
  }

	/* nat: public DAOCodeTable getDaoCodeTable() {
		return daoCodeTable;
	}*/

  /**
   * After a change in correlation stringValue 1, the available choices for values 2 and
   * 3 are recalculated and the values are reset (to the first available valid choice)
   *
   * @param s the new value1
   */
  public void updateFirstCorrelation(int s) throws DataAccessException {
    setCorrelation (1, s);
    List l = getSecondCorrelationList (s);
    if (l != null) {
      updateSecondCorrelation (((T_SINGLE) l.get (0)).getCode ( ));
    }
  }

  /**
   * After a change in correlation stringValue 2, the available choices for values 3
   * are recalculated and the stringValue is reset (to the first available valid choice)
   *
   * @param s the new stringValue 2
   */
  public void updateSecondCorrelation(int s) throws DataAccessException {
    setCorrelation (2, s);
    List l = getThirdCorrelationList (getCorrelation (1), getCorrelation (2));
    if (l != null) {
      setCorrelation (3, ((T_SINGLE) l.get (0)).getCode ( ));
    }
  }

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException, SQLException {
  }

  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  public void setPersistenceState(PersistenceState object) {
    persistenceState = object;
  }

  public int getUpdateStatus() {
    if (persistenceState == null) {
      return -1;
    } else {
      return persistenceState.getUpdateStatus ( );
    }
  }

  public void setUpdateStatus(int i) {
    if (persistenceState != null) {
      persistenceState.setUpdateStatus (i);
    }
  }

  public boolean isChanged() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isChanged ( );
    }
  }

  public boolean isDeleted() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isDeleted ( );
    }
  }

  public boolean isNew() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isNew ( );
    }
  }

  public boolean isRemoved() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isRemoved ( );
    }
  }

  public void markChanged() {
    if (persistenceState != null) {
      persistenceState.markChanged ( );
    }
  }

  public void markDeleted() {
    if (persistenceState != null) {
      persistenceState.markDeleted ( );
    }
  }

  public void markNew() {
    if (persistenceState != null) {
      persistenceState.markNew ( );
    }
  }

  public void markUnchanged() {
    if (persistenceState != null) {
      persistenceState.markUnchanged ( );
    }
  }

  public boolean onDelete(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onDelete (arg0);
    } else {
      return true;
    }
  }

  public void onLoad(Session arg0, Serializable arg1) {
    if (persistenceState != null) {
      persistenceState.onLoad (arg0, arg1);
    }
  }

  public boolean onSave(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onSave (arg0);
    } else {
      return true;
    }
  }

  public boolean onUpdate(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onUpdate (arg0);
    } else {
      return true;
    }
  }

  public void evict() throws DataAccessException {
    if (persistenceState != null) {
      persistenceState.evict (this);
    }
  }

  /**
   * This method creates a XML Document as follows
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * or for a control field
   * <controlfield tag="001">000000005581</controlfield>
   *
   * @return a Document
   */
  public Document toXmlDocument() {
    DocumentBuilderFactory documentBuilderFactory =
      DocumentBuilderFactory.newInstance ( );
    DocumentBuilder documentBuilder = null;
    Document xmlDocument = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder ( );
      xmlDocument = documentBuilder.newDocument ( );
      xmlDocument.appendChild (toXmlElement (xmlDocument));
    } catch (ParserConfigurationException parserConfigurationException) {
      logger.error ("", parserConfigurationException);
      //throw new XmlParserConfigurationException(parserConfigurationException);
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
   * <controlfield tag="001">000000005581</controlfield>
   *
   * @return an Element
   */
  public Element toXmlElement(Document xmlDocument) {
		/*CorrelationKey marcEncoding = null;
		try {
			marcEncoding = getMarcEncoding();
		} catch (Exception exception) {
			throw new RuntimeException("Invalid tag found in Tag.toXmlElement");
		}

		String marcTag = marcEncoding.getMarcTag();
		String marcFirstIndicator =
			new String("" + marcEncoding.getMarcFirstIndicator());
		String marcSecondIndicator =
			new String("" + marcEncoding.getMarcSecondIndicator());

		Element field = null;
		if (isFixedField()) {
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
		return field;*/
    return null;
  }

  /**
   * This method is used to generated the model xml.
   *
   * @since 1.0
   */
  public Element generateModelXmlElement(Document xmlDocument) {
    Element field = null;
		/*if (xmlDocument != null) {
			field = xmlDocument.createElement("field");
			try {
				field.setAttribute("tag", this.getMarcEncoding().getMarcTag());
				field.setAttribute(
					"firstIndicator",
					"" + this.getMarcEncoding().getMarcFirstIndicator());
				field.setAttribute(
					"secondIndicator",
					"" + this.getMarcEncoding().getMarcSecondIndicator());
				field.setAttribute("categoryCode", "" + this.getCategory());
				field.setAttribute(
					"firstCorrelationValue",
					"" + this.getCorrelation(1));
				field.setAttribute(
					"secondCorrelationValue",
					"" + this.getCorrelation(2));
				field.setAttribute(
					"thirdCorrelationValue",
					"" + this.getCorrelation(3));
			} catch (MarcCorrelationException marcCorrelationException) {
			} catch (DataAccessException dataAccessException) {
			}
			field.appendChild(generateModelXmlElementContent(xmlDocument));
		}*/
    return field;
  }

  public Object clone() {
    return deepCopy (this);
  }

  public TagImpl getTagImpl() {
    return tagImpl;
  }

  public void setTagImpl(TagImpl impl) {
    tagImpl = impl;
  }

  public int getItemNumber() {
    return itemNumber;
  }

  public void setItemNumber(int itemNumber) {
    this.itemNumber = itemNumber;
  }

  public Validation getValidation() throws DataAccessException {
    //return tagImpl.getValidation(this);
    return validation;
  }

  public void setValidation(Validation validation) {
    this.validation = validation;
  }

  public Validation getValidation(final Session session) throws DataAccessException {
    validation = tagImpl.getValidation (this, session);
    return validation;
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  abstract public int getCategory();

  /* (non-Javadoc)
   * @see TagInterface#isHasSubfieldW()
   */
  public boolean isHasSubfieldW() {
    return false; //default implementation
  }

	/* nat: public HibernateUtil getDAO() {
		return persistenceState.getDAO();
	}*/

  @Override
  public boolean isEquivalenceReference() {
    return false;
  }

  /* (non-Javadoc)
   * @see TagInterface#getDisplayCategory()
   */
  public int getDisplayCategory() {
    return getCategory ( );
  }

  /* (non-Javadoc)
   * @see TagInterface#getDisplaysHeadingType()
   */
  public boolean getDisplaysHeadingType() {
    return false;
  }

  /* (non-Javadoc)
   * @see TagInterface#validate()
   */
  public void validate(int index) throws ValidationException {
    // default implementation does nothing
  }

  /**
   * Called where a
   * series of changes result in returning the new key back
   * to a pre-existing key
   */
  public void reinstateDeletedTag() {
    markUnchanged ( );
    markChanged ( );
  }

  public String getNewSubfieldContent() {
    return newSubfieldContent;
  }

  public void setNewSubfieldContent(String newSubfieldContent) {
    this.newSubfieldContent = newSubfieldContent;
  }

  public int getPhysical() {
    return PHYSICAL_MATERIAL;
  }

  public AbstractDAO getDAO() {
    return persistenceState.getDAO ( );
  }
}
