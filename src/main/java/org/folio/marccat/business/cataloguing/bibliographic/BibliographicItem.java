package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.persistence.BIB_ITM;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.ModelItem;
import org.folio.marccat.exception.MandatoryTagException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.util.Iterator;

public class BibliographicItem extends CatalogItem implements Serializable {
  private static final long serialVersionUID = 8676099561229020012L;
  private Log logger = new Log(BibliographicItem.class);
  private BIB_ITM bibItmData;
  private int userView;

  public BibliographicItem() {
    super();
  }

  public BibliographicItem(Integer id) {
    super();
    this.bibItmData.setAmicusNumber(id);
  }

  public BIB_ITM getBibItmData() {
    return bibItmData;
  }

  public void setBibItmData(BIB_ITM bib_itm) {
    bibItmData = bib_itm;
    /*
     * Set all PersistsViaBibItem tags
     */
    Iterator iter = getTags().iterator();
    while (iter.hasNext()) {
      Tag aTag = (Tag) iter.next();
      if (aTag instanceof PersistsViaItem) {
        ((PersistsViaItem) aTag).setItemEntity(bib_itm);
      }
    }
  }

  @Override
  public TagImpl getTagImpl() {
    return new BibliographicTagImpl();
  }

  public int getUserView() {
    return userView;
  }

  public void setUserView(int i) {
    userView = i;
  }

  public void setModelItem(ModelItem modelItem) {
    this.modelItem = modelItem;
  }

  /**
   * This method creates a XML Document as follows
   * <record>
   * <leader>00451nam a2200109 a 4500</leader>
   * <controlfield tag="001">000000005581</controlfield>
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * </record
   *
   * @return a Document
   */
  public Document toXmlDocument() {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
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
   * <record>
   * <leader>00451nam a2200109 a 4500</leader>
   * <controlfield tag="001">000000005581</controlfield>
   * <datafield tag="100" ind1="1" ind2="@">
   * <subfield code="a">content</subfield>
   * <subfield code="b">content</subfield>
   * </datafield>
   * </record
   *
   * @return an Element
   */
  public Element toXmlElement(Document xmlDocument) {
    Element record = xmlDocument.createElement("record");
    Iterator tagIterator = this.tags.iterator();
    while (tagIterator.hasNext()) {
      Tag tag = (Tag) tagIterator.next();
      record.appendChild(tag.toXmlElement(xmlDocument));
    }
    return record;
  }

  /* (non-Javadoc)
   * @see CatalogItem#checkForMandatoryTags()
   */
  /*
   * Note that only editable mandatory tags are included -- generated tags
   * like 000, 001, 005 cannot be added by the user and will be generated
   * if not present
   */
  public void checkForMandatoryTags() {
    final String[] tags = new String[]{"000", "008", "040"};
    for (int i = 0; i < tags.length; i++) {
      if (findFirstTagByNumber(tags[i]) == null) {
        throw new MandatoryTagException(tags[i]);
      }
    }
  }

  @Override
  public ItemEntity getItemEntity() {
    return bibItmData;
  }

  /* (non-Javadoc)
   * @see CatalogItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setBibItmData((BIB_ITM) item);
  }

  /* (non-Javadoc)
   * @see CatalogItem#setItemEntity(ItemEntity)
   */




}
