package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.cataloguing.common.TagImpl;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.librivision.XmlRecord;
import org.folio.cataloging.dao.DAOOrderNames;
import org.folio.cataloging.dao.persistence.CatalogItem;
import org.folio.cataloging.dao.persistence.ItemEntity;
import org.folio.cataloging.dao.persistence.ModelItem;
import org.folio.cataloging.dao.persistence.OrderNames;
import org.folio.cataloging.exception.MandatoryTagException;
import org.folio.cataloging.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BibliographicItem extends CatalogItem implements Serializable 
{
	private static final long serialVersionUID = 8676099561229020012L;
	private Log logger = new Log(BibliographicItem.class);
		
	private static List nameOrderTags = null;
	private BIB_ITM bibItmData;
	private int userView;
	
	public BibliographicItem() {
		super();
	}

	public BibliographicItem(Integer id) 
	{
		super();
		this.bibItmData.setAmicusNumber(id);
	}

	public BIB_ITM getBibItmData() {
		return bibItmData;
	}

	public ItemEntity getItemEntity() {
		return bibItmData;
	}

	@Override
	public TagImpl getTagImpl() {
		return new BibliographicTagImpl();
	}

	public int getUserView() {
		return userView;
	}

	public void setBibItmData(BIB_ITM bib_itm) 
	{
		bibItmData = bib_itm;
		/*
		 * Set all PersistsViaBibItem tags
		 */
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof PersistsViaItem) {
				((PersistsViaItem)aTag).setItemEntity(bib_itm);
			}
		}
	}

	/* (non-Javadoc)
	 * @see CatalogItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setBibItmData((BIB_ITM)item);
	}

	public void setModelItem(ModelItem modelItem) {
		this.modelItem = modelItem;
	}

	public void setUserView(int i) {
		userView = i;
	}

	public Record toRecord(String elementSetName) 
	{
		Document xmlDocument = toXmlDocument();
		XmlRecord xmlRecord = new XmlRecord();
		xmlRecord.setContent(elementSetName, xmlDocument);
		return xmlRecord;
	}

	/**
	 * This method creates a XML Document as follows
	 * <record>
	 *  <leader>00451nam a2200109 a 4500</leader>
	 *  <controlfield tag="001">000000005581</controlfield>
	 *  <datafield tag="100" ind1="1" ind2="@">
	 *   <subfield code="a">content</subfield>
	 *   <subfield code="b">content</subfield>
	 *  </datafield>
	 * </record
	 * 
	 * @return a Document
	 */
	public Document toXmlDocument() 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document xmlDocument = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlDocument = documentBuilder.newDocument();
			xmlDocument.appendChild(toXmlElement(xmlDocument));
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
			//throw new XmlParserConfigurationException(parserConfigurationException);
		}
		return xmlDocument;
	}

	/**
	 * This method creates a XML Element as follows
	 * <record>
	 *  <leader>00451nam a2200109 a 4500</leader>
	 *  <controlfield tag="001">000000005581</controlfield>
	 *  <datafield tag="100" ind1="1" ind2="@">
	 *   <subfield code="a">content</subfield>
	 *   <subfield code="b">content</subfield>
	 *  </datafield>
	 * </record
	 * 
	 * @return an Element
	 */
	public Element toXmlElement(Document xmlDocument) 
	{
		Element record = xmlDocument.createElement("record");
		//bElement author1 = record.addElement("leader").addText("00451nam a2200109 a 4500");
		// TODO set the leader correctly
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
	public void checkForMandatoryTags() throws MandatoryTagException 
	{
//		final String[] tags = new String[] {"000", "008", "040", "245"};
		final String[] tags = new String[] {"000", "008", "040"};
		for (int i=0; i<tags.length; i++) {
			if (findFirstTagByNumber(tags[i]) == null) {
				throw new MandatoryTagException(tags[i]);
			}
		}
	}
	
	private boolean isOrderableNameTag(String string) throws DataAccessException 
	{
		Iterator iter = getOrderableNameTags().iterator();
		while (iter.hasNext()) {
			OrderNames anOrderNameTag = (OrderNames)iter.next();
			if (anOrderNameTag
				.getTagNumber()
				.equals(string)) {
				return true;
			}
		}
		return false;
	}

	private List getOrderableNameTags() throws DataAccessException 
	{
		if (nameOrderTags == null) {
			nameOrderTags = new DAOOrderNames().getOrderNames();
		}
		return nameOrderTags;
	}

	public List getOrderableNames() throws DataAccessException {
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof NameAccessPoint) {
				if (isOrderableNameTag((aTag)
					.getMarcEncoding()
					.getMarcTag())) {
						tags.add(aTag);
				}
			}
		}
		return tags;
	}

	public List getOrderableSubjects() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof SubjectAccessPoint) {
						tags.add(aTag);
			}
		}
		return tags;
	}
	
	public List getOrderableNotes() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof BibliographicNoteTag) {
						tags.add(((BibliographicNoteTag)aTag).getNote());
			}
		}
		return tags;
	}
	
	public List getOrderableTitles() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof TitleAccessPoint) {
						tags.add(aTag);
			}
		}
		return tags;
	}
	
	public List getOrderableClassifications() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof ClassificationAccessPoint) {
						tags.add(aTag);
			}
		}
		return tags;
	}
	
	public List getOrderableControlNumbers() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			if (aTag instanceof ControlNumberAccessPoint) {
						tags.add(aTag);
			}
		}
		return tags;
	}
	
	public List getOrderableRelations() 
	{
		List tags = new ArrayList();
		Iterator iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag)iter.next();
			//Dubbio getSourceRelationship() c'è anche il target
			if (aTag instanceof BibliographicRelationshipTag ) {
						tags.add(((BibliographicRelationshipTag )aTag).getSourceRelationship());
			}
		}
		return tags;
	}
}