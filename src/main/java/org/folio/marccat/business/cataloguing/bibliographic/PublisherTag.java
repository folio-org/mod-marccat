/*
 * (c) LibriCore
 *
 * Created on Dec 21, 2004
 *
 * PublisherTag.java
 */
package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.dao.PublisherDescriptorDAO;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.PUBL_HDG;
import org.folio.marccat.dao.persistence.PublisherAccessPoint;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.folio.marccat.util.F.deepCopy;

/**
 * Publisher Tag differs from other access points in that multiple publisher access
 * points produce a single tag.  This class then implements the Tag/VariableField behaviour
 * and manages the persistence of the constituent PublisherAccessPoint objects
 */
public class PublisherTag extends VariableField implements PersistentObjectWithView {
  private static Log logger = LogFactory.getLog(PublisherTag.class);
  private PersistenceState persistenceState = new PersistenceState();
  private List accessPoints = new ArrayList();
  private List deletedApfs = new ArrayList();
  private List dates = new ArrayList();
  private int apfIndex;
  private String manufacturerPlace = "";
  private String manufacturer = "";
  private String manufacturerDate = "";
  private UserViewHelper userViewHelper = new UserViewHelper();

  /*
   * noteType is used only for the edit page correlation to allow for the possibility
   * that a publisher tag can be changed to another note type.
   */
  private int noteType = 24;


  public PublisherTag() {
    super();
    setPersistenceState(persistenceState);
    addNewAccessPoint();
  }


  public PublisherTag(int bib_itm, int view) {
    super();
    setPersistenceState(persistenceState);
    setBibItemNumber(bib_itm);
    setUserViewString(View.makeSingleViewString(view));
    addNewAccessPoint();
  }

  /**
   * Class constructor
   */
  public PublisherTag(List accessPoints) {
    super();
    setPersistenceState(persistenceState);
    PublisherAccessPoint anApf = (PublisherAccessPoint) accessPoints.get(0);
    setItemNumber(anApf.getItemNumber());
    setUserViewString(anApf.getUserViewString());
    setUpdateStatus(anApf.getUpdateStatus());
    setAccessPoints(accessPoints);
    Collections.sort(accessPoints, new PublisherAccessPointComparator());
  }

  /*
   * Copy dates from access points into Publisher Tag while editing
   */
  private void copyDates() {
    Iterator iter = getAccessPoints().iterator();
    PublisherAccessPoint apf;
    while (iter.hasNext()) {
      apf = (PublisherAccessPoint) iter.next();
      getDates().add(apf.getDate());
    }
  }

  /*
   * Extracts subfields e,f,g from the "last" access point and stores them in separate
   * member variables
   */
  private void extractManufacturerData() {
    if (getAccessPoints().size() > 0) {
      PublisherAccessPoint lastApf =
        (PublisherAccessPoint) accessPoints.get(
          accessPoints.size() - 1);
      StringText s = lastApf.getAccessPointStringText();
      lastApf.setAccessPointStringText(s.getSubfieldsWithoutCodes("efg"));
      setManufacturerPlace(
        s.getSubfieldsWithCodes("e").toDisplayString());
      setManufacturer(s.getSubfieldsWithCodes("f").toDisplayString());
      setManufacturerDate(s.getSubfieldsWithCodes("g").toDisplayString());
    }
  }

  /**
   * updates the publisher tag with the recently browsed publisher heading
   */
  public void updatePublisherFromBrowse(PUBL_HDG p) {
    PublisherAccessPoint apf =
      (PublisherAccessPoint) getAccessPoints().get(getApfIndex());
    apf.setDescriptor(p);
  }

  /* (non-Javadoc)
   * @see VariableField#getStringText()
   */
  public StringText getStringText() {
    StringText result = new StringText();
    PublisherAccessPoint aPub;
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      aPub = (PublisherAccessPoint) iter.next();
      result.add(aPub.getStringText());
    }
    return result;
  }

  /* (non-Javadoc)
   * @see VariableField#setStringText(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    // should only be called from model parsing
    /*
     * clear the current apf's
     */
    setAccessPoints(new ArrayList());
    /*
     * break up the string text into constituent PublisherAccessPoints
     */
    StringText newText = new StringText();
    String lastSubfield = "z";
    Iterator iter = stringText.getSubfieldList().iterator();
    while (iter.hasNext()) {
      Subfield aSubfield = (Subfield) iter.next();
      if (aSubfield.getCode().compareTo(lastSubfield) < 0
        || !iter.hasNext()) {
        if (!iter.hasNext()) {
          // add the last subfield to newText
          newText.addSubfield(aSubfield);
        }
        /*
         * we have reached the end of an APF group of subfields
         * so create a new APF and set the text
         */
        if (newText.getNumberOfSubfields() > 0) {
          addNewAccessPoint();
          PublisherAccessPoint p =
            (PublisherAccessPoint) getAccessPoints().get(
              getAccessPoints().size() - 1);
          p.setStringText(newText);
          newText = new StringText();
        }
      }
      newText.addSubfield(aSubfield);
      lastSubfield = aSubfield.getCode();
    }
    parseForEditing();
  }

  /**
   * @return the MARC display string for the publisher tag
   */
  public String getDisplayString() {
    /*
     * This is used in the worksheet jsp for displaying tags that
     * cannot be directly edited on the page (as with publisher)
     */
    return getStringText().getMarcDisplayString();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
   */
  public int getCategory() {
    return 7;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues(
      noteType,
      CorrelationValues.UNDEFINED,
      CorrelationValues.UNDEFINED);
  }

  /* (non-Javadoc)
   * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    noteType = v.getValue(1);
  }


  public List getAccessPoints() {
    return accessPoints;
  }


  public void setAccessPoints(List list) {
    accessPoints = list;
  }

  public PublisherAccessPoint getAnyPublisher() {
    PublisherAccessPoint result = new PublisherAccessPoint();
    if (getAccessPoints() != null & getAccessPoints().size() > 0) {
      result = (PublisherAccessPoint) getAccessPoints().get(0);
    }
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
   */
  @Deprecated
  public List getFirstCorrelationList() {
    return Collections.emptyList();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PublisherTag) {
      PublisherTag p = (PublisherTag) obj;
      return p.getItemNumber() == this.getItemNumber();
    }
    return false;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isWorksheetEditable()
   */
  @Override
  public boolean isWorksheetEditable() {
    return false;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isPublisher()
   */
  @Override
  public boolean isPublisher() {
    return true;
  }


  public String getManufacturer() {
    return manufacturer;
  }


  public void setManufacturer(String string) {
    manufacturer = string;
  }


  public String getManufacturerDate() {
    return manufacturerDate;
  }


  public void setManufacturerDate(String string) {
    manufacturerDate = string;
  }


  public String getManufacturerPlace() {
    return manufacturerPlace;
  }


  public void setManufacturerPlace(String string) {
    manufacturerPlace = string;
  }


  public List getDeletedApfs() {
    return deletedApfs;
  }


  public void setDeletedApfs(List list) {
    deletedApfs = list;
  }


  public int getApfIndex() {
    return apfIndex;
  }


  public void setApfIndex(int i) {
    apfIndex = i;
  }


  public List getDates() {
    return dates;
  }


  public void setDates(List list) {
    logger.debug("setting all dates");
    dates = list;
    for (int i = 0; i < dates.size(); i++) {
      logger.debug("dates[" + i + "]='" + dates.get(i) + "'");
    }
  }


  public List getDates(int i) {
    logger.debug("getting date[" + i + "] = " + dates.get(i));
    return dates;
  }

  public void addNewAccessPoint() {
    PublisherAccessPoint apf = new PublisherAccessPoint();
    apf.setTagImpl(new BibliographicTagImpl());
    apf.setItemNumber(getItemNumber());
    apf.setUserViewString(getUserViewString());
    getAccessPoints().add(apf);
    getDates().add("");
  }

  /**
   * Extract dates and manufacturer data for editing as individual fields
   *
   * @since 1.0
   */
  public void parseForEditing() {
    extractManufacturerData();
    copyDates();
  }

  /**
   * Incorporate edit changes (dates, manufacturer data, sequences, etc.) into
   * the accessPoints ready for saving to the database
   *
   * @since 1.0
   */
  public void saveEdits() {
    PublisherAccessPoint apf;
    for (int i = 0; i < getAccessPoints().size(); i++) {
      apf = (PublisherAccessPoint) getAccessPoints().get(i);
      apf.setSequenceNumber(i + 1);
      /*modifica barbara PRN 101*/
      if (getDates().size() > 0) {
        if (!("".equals(getDates().get(i)))) {
          apf.setAccessPointStringText(
            new StringText(
              Subfield.SUBFIELD_DELIMITER + "c" + getDates().get(i)));
        } else {
          apf.setAccessPointStringText(
            new StringText(""));
        }

      } else {
        apf.setAccessPointStringText(
          new StringText(""));
      }

    }
    setDates(new ArrayList());
    // check if any of subfields e,f,g are present
    StringText s = new StringText();
    String luogo = "";
    String nome = "";
    String data = "";

    if (!("".equals(getManufacturerPlace()))) {
      luogo = getManufacturerPlace();
      if (luogo.indexOf("(") == -1)
        luogo = "(" + luogo;
      if (getManufacturer().equals("") && getManufacturerDate().equals("")) {
        if (luogo.indexOf(")") == -1)
          luogo = luogo + ")";
      }
      s.addSubfield(new Subfield("e", luogo));
      setManufacturerPlace("");
    }
    if (!("".equals(getManufacturer()))) {
      nome = getManufacturer();
      if (luogo.equals("") && nome.indexOf("(") == -1)
        nome = "(" + nome;
      if (getManufacturerDate().equals("") && nome.indexOf(")") == -1)
        nome = nome + ")";
      s.addSubfield(new Subfield("f", nome));
      setManufacturer("");
    }
    if (!("".equals(getManufacturerDate()))) {
      data = getManufacturerDate();
      if (luogo.equals("") && nome.equals("")) {
        if (data.indexOf("(") == -1)
          data = "(" + data;
        if (data.indexOf(")") == -1)
          data = data + ")";
      } else if (data.indexOf(")") == -1) {
        data = data + ")";
        s.addSubfield(new Subfield("g", data));
        setManufacturerDate("");
      }
    }

    if (s.getNumberOfSubfields() > 0) {
      if (getAccessPoints().size() == 0) {
        // create a new apf to hold the manufacturer data
        getAccessPoints().add(new PublisherAccessPoint());
      }
      // add the manufacturer data to the last apf
      apf =
        (PublisherAccessPoint) getAccessPoints().get(
          getAccessPoints().size() - 1);
      StringText s2 = apf.getAccessPointStringText();
      s2.add(s);
      apf.setAccessPointStringText(s2);

    }
  }

  @Override
  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content =
        getStringText().generateModelXmlElementContent(xmlDocument);
    }
    return content;
  }

  @Override
  public void parseModelXmlElementContent(Element xmlElement) {
    setStringText(StringText.parseModelXmlElementContent(xmlElement));
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

  /* (non-Javadoc)
   * @see TagInterface#evict()
   */
  public void evict() throws DataAccessException {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).evict();
    }
  }

  /* (non-Javadoc)
   * @see TagInterface#markChanged()
   */
  public void markChanged() {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).markChanged();
    }
    super.markChanged();
  }

  /* (non-Javadoc)
   * @see TagInterface#markDeleted()
   */
  public void markDeleted() {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).markDeleted();
    }
    super.markDeleted();
  }

  /* (non-Javadoc)
   * @see TagInterface#markNew()
   */
  public void markNew() {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).markNew();
    }
    super.markNew();
  }

  /* (non-Javadoc)
   * @see TagInterface#markUnchanged()
   */
  public void markUnchanged() {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).markUnchanged();
    }
    super.markUnchanged();
  }

  /* (non-Javadoc)
   * @see TagInterface#setUpdateStatus(int)
   */
  public void setUpdateStatus(int i) {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      ((PublisherAccessPoint) iter.next()).setUpdateStatus(i);
    }
    super.setUpdateStatus(i);
  }

  /* (non-Javadoc)
   * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.getValue(1) != 24;
  }

  public void removeDescriptorDefaults(int i) {
    PublisherAccessPoint pap = (PublisherAccessPoint) getAccessPoints().get(i);
    removeDescriptorDefaults(pap);
  }

  public void removeDescriptorDefaults() {
    Iterator iter = getAccessPoints().iterator();
    while (iter.hasNext()) {
      PublisherAccessPoint pap = ((PublisherAccessPoint) iter.next());
      removeDescriptorDefaults(pap);
    }
  }

  public void removeDescriptor(int i) {
    PublisherAccessPoint pap = (PublisherAccessPoint) getAccessPoints().get(i);
    detachDescriptor(pap);
  }

  /**
   * @param pap
   */
  private void removeDescriptorDefaults(PublisherAccessPoint pap) {
    PUBL_HDG publ_hdg = ((PUBL_HDG) pap.getDescriptor());
    if (pap.getHeadingNumber() == null) {
      publ_hdg.setNameStringText("");
      publ_hdg.setPlaceStringText("");
    }
  }

  /**
   * @param pap Non deve rimuovere la heading ma solamente staccarla
   */

  private void detachDescriptor(PublisherAccessPoint pap) {
    PUBL_HDG publ_hdg = ((PUBL_HDG) pap.getDescriptor());
    if (pap.getHeadingNumber() == null) {
      publ_hdg.setNameStringText("");
      publ_hdg.setPlaceStringText("");

    } else {
      pap.setDescriptor(null);
      pap.setHeadingNumber(null);
    }
  }

  public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException {
    final DescriptorDAO dao = new PublisherDescriptorDAO();
    List newTags = new ArrayList();
    PublisherAccessPoint anApf = null;
    List accessPointsApp = new ArrayList();
    PublisherTag aTag = (PublisherTag) deepCopy(this);
    for (int i = 0; i < getAccessPoints().size(); i++) {
      anApf = (PublisherAccessPoint) getAccessPoints().get(i);
      Descriptor d = anApf.getDescriptor();
      REF ref = null;
      try {
        ref = dao.getCrossReferencesWithLanguage(d, cataloguingView, indexingLanguage, null);
      } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
      }
      if (ref != null) {
        aTag.markNew();
        anApf.setDescriptor(dao.load(ref.getTarget(), cataloguingView));
        anApf.setHeadingNumber((anApf.getDescriptor()
          .getKey().getHeadingNumber()));
        accessPointsApp.add(anApf);

      } else {
        accessPointsApp.add(anApf);
      }
    }
    if (aTag != null) {
      aTag.setAccessPoints(accessPointsApp);
      newTags.add(aTag);
    }

    return newTags;
  }


}
