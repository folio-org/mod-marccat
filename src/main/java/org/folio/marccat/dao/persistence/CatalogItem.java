package org.folio.marccat.dao.persistence;

import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.group.*;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.DuplicateTagException;
import org.folio.marccat.exception.MarcCorrelationException;
import org.folio.marccat.exception.ValidationException;
import org.folio.marccat.shared.Validation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.util.*;


/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public abstract class CatalogItem implements Serializable {

  private static final Comparator<Tag> tagComparator =
    (Tag tag1, Tag tag2) -> (tag1.getMarcEncoding().getMarcTag().compareTo(tag2.getMarcEncoding().getMarcTag()));
  private List<Tag> deletedTags = new ArrayList<>();
  protected ModelItem modelItem;
  protected Session session;
  private List<Tag> tags = new ArrayList<>();
  private transient Log logger = new Log(CatalogItem.class);

  public CatalogItem() {
    super();
  }

  /**
   * This method creates a MarcSlim XML Element for this item
   *
   * @return an Document
   */
  public Document toExternalMarcSlim(Session session) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    DocumentBuilder documentBuilder;
    Document xmlDocument = null;
    Element record;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      xmlDocument = documentBuilder.newDocument();
    } catch (ParserConfigurationException parserConfigurationException) {
      logger.error("Xml Parser Configuration Exception", parserConfigurationException);
    }
    if(xmlDocument != null) {
      record = xmlDocument.createElement("record");
      for (Object t : tags) {
        Tag tag = (Tag) t;
        logger.debug("appending " + tag);
        record.appendChild(tag.toExternalMarcSlim(xmlDocument, session));
      }
      xmlDocument.appendChild(record);
    }
     return xmlDocument;
  }





  /**
   * @param newTag
   */
  public void addTag(Tag newTag) {
    tags.add(newTag);
  }

  /**
   * @param session
   */
   public abstract void checkForMandatoryTags(Session session);

  /**
   * Checks if the specified tag is illegally repeated in the item and throws an exception if so.
   *
   * @param index -- the tag index.
   */
  public void checkRepeatability(final int index) {
    final Tag t = getTag(index);
    Validation bv = t.getValidation();
    if (!bv.isMarcTagRepeatable()) {
      List l = new ArrayList(getTags());
      l.remove(index);
      l.sort(tagComparator);
      if (Collections.binarySearch(l, t, tagComparator) >= 0) {
        throw new DuplicateTagException(index);
      }
    }
  }

  /**
   * Finds the first tag occurrence of the given tag number.
   * If a length shorter than 3 is given (e.g. "1"), then the first tag starting with a 1 will be returned.
   *
   * @param s -- the tag number to search.
   */


  /**
   * Finds the first tag occurrence of the given tag number.
   * If a length shorter than 3 is given (e.g. "1"), then the first tag starting with a 1 will be returned.
   *
   * @param s -- the tag number to search.
   */
  public Tag findFirstTagByNumber(final String s, final Session session) {
    try {
      return getTags().stream().filter(tag -> tag.getMarcEncoding(session).getMarcTag().startsWith(s)).findFirst().orElse(null);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * @return the Amicus number for the item (aut or bib)
   */
  public Integer getAmicusNumber() {
    return getItemEntity().getAmicusNumber();
  }

  /**
   * @return tags to delete.
   */
  public List<Tag> getDeletedTags() {
    return deletedTags;
  }

  public abstract ItemEntity getItemEntity();

  public abstract void setItemEntity(ItemEntity item);

  public ModelItem getModelItem() {
    return modelItem;
  }


  /**
   * Sets model item associated to item.
   *
   * @param model -- the model to set.
   * @throws DataAccessException in
   */
  public void setModelItem(final Model model) {
    if (this.modelItem == null) {
      this.modelItem = new BibliographicModelItem();
      this.modelItem.markNew();
    }
    this.modelItem.setItem(this.getAmicusNumber());
    this.modelItem.setModel(model);
    this.modelItem.setRecordFields(model.getRecordFields());
  }

  public int getNumberOfTags() {
    return tags.size();
  }

  public Tag getTag(int i) {
    return tags.get(i);
  }

  public abstract TagImpl getTagImpl();

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> set) {
    tags = set;
  }

  public abstract int getUserView();

  /**
   * @return the verification level of item entity.
   */
  public char getVerificationLevel() {
    return getItemEntity().getVerificationLevel();
  }

  public void setVerificationLevel(char verificationLevel) {
    getItemEntity().setVerificationLevel(verificationLevel);
  }

  /**
   * remove a tag from the deletedTags list (by Object)
   */
  public void removeDeletedTag(Tag tag) {
    deletedTags.remove(tag);
  }

  /**
   * remove a tag from the Item (by Object)
   */
  public void removeTag(Tag tag) {
    tags.remove(tag);
  }


  /**
   * Replacing old tag with a new one in the bibItem.
   */
  public void setTag(Tag oldTag, Tag newTag) {
    if (getAmicusNumber() != null) {
      newTag.setItemNumber(getAmicusNumber());
    }
    tags.set(tags.indexOf(oldTag), newTag);
  }

  /**
   * Sorting of tags.
   */
  @SuppressWarnings("unchecked")
  public void sortTags() {
    try {

      final LinkedHashMap<Object, TagContainer> groupsHashMap = populateGroups();
      List<TagContainer> tagContainers = new ArrayList<>(groupsHashMap.values());
      tagContainers.sort(new GroupComparator());
      final List<Tag> tagSet = unlist(tagContainers);
      setTags(tagSet);
    } catch (MarcCorrelationException e) {
      logger.info(Message.MOD_MARCCAT_00017_MARC_CORRELATION_SORTING);
    } catch (DataAccessException e) {
      logger.info(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE);
    }
  }

  /**
   * Puts the tags back in order by pre-ordering the groups based on the sequence number.
   *
   * @param tagContainers -- list of tag containers.
   * @return list of ordered tags.
   */
  private List<Tag> unlist(final List<TagContainer> tagContainers) {
    final List<Tag> tagSet = new ArrayList<>();
    tagContainers.forEach(item -> {
      if (item instanceof Tag)
        tagSet.add((Tag) item);
      else {
        item.sort();
        tagSet.addAll(item.getList());
      }
    });
    return tagSet;
  }

  /**
   * Rearrange the tags in groups.
   *
   * @return
   * @throws DataAccessException in case of data access failure.
   */
  private LinkedHashMap<Object, TagContainer> populateGroups() {
    final LinkedHashMap<Object, TagContainer> ht = new LinkedHashMap<>();
    final GroupManager groupManager = new BibliographicGroupManager();

    tags.forEach(tag -> {
      final TagGroup group = groupManager.getGroup(tag);
      if (group == null) {
        ht.put(tag, new UniqueTagContainer(tag));
      } else {
        TagContainer tc = ht.get(group);
        if (tc == null) {
          tc = new MultiTagContainer();
          ht.put(group, tc);
        }
        tc.add(tag);
      }
    });
    return ht;
  }


  /**
   * Validates, check mandatory and repeatability tags.
   *
   * @throws ValidationException in case of validation exception.
   * @throws DataAccessException in case of data access exception.
   */
  public void validate(Session session) {
    checkForMandatoryTags(session);
    for (int i = 0; i < getTags().size(); i++) {
      checkRepeatability(i);
      getTag(i).validate(i);
    }
  }

}
