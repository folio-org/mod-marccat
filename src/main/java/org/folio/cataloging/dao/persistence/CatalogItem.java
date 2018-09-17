package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.FixedField;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.cataloguing.common.TagImpl;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.filter.SameDescriptorTagFilter;
import org.folio.cataloging.business.common.filter.TagFilter;
import org.folio.cataloging.business.common.group.*;
import org.folio.cataloging.exception.DuplicateTagException;
import org.folio.cataloging.exception.MandatoryTagException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.integration.log.MessageCatalogStorage;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.Validation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public abstract class CatalogItem implements Serializable {

	private Log logger = new Log(CatalogItem.class);
	protected List<Tag> deletedTags = new ArrayList();
	protected ModelItem modelItem = null;
	protected List<Tag> tags = new ArrayList();

	public CatalogItem() {
		super();
	}

    private static final Comparator<Tag> tagComparator =
            (Tag tag1, Tag tag2) -> (tag1.getMarcEncoding().getMarcTag().compareTo(tag2.getMarcEncoding().getMarcTag()));


	/**
	 * This method creates a MarcSlim XML Element for this item
	 * @return an Element
	 */
	public Document toExternalMarcSlim() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document xmlDocument = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlDocument = documentBuilder.newDocument();
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
			//throw new XmlParserConfigurationException(parserConfigurationException);
		}
		Element record = xmlDocument.createElement("record");
		for (Object t : tags) {
			Tag tag = (Tag) t;
			logger.debug("appending " + tag);
			record.appendChild(tag.toExternalMarcSlim(xmlDocument));
		}
		xmlDocument.appendChild(record);
		return xmlDocument;
	}

	@Deprecated
	public void addAllTags(Tag[] tags) {
		for (int i = 0; i < tags.length; i++) {
			addTag(tags[i]);
		}
	}

	@Deprecated
	public void addDeletedTag(Tag aTag) {
		if (!deletedTags.contains(aTag)) {
			deletedTags.add(aTag);
		}
	}

	/**
     * Adds the newTag to the list _AFTER_ the point i.
     *
     * @param i -- the tag index.
	 * @param newTag -- the new tag to add.
	 */
	public void addTag(final int i, final Tag newTag) {
		if (tags.size() > i) {
			tags.add(i + 1, newTag);
		} else {
			tags.add(newTag);
		}
	}

	public void addTag(Tag newTag) {
		tags.add(newTag);
	}

	abstract public void checkForMandatoryTags() throws MandatoryTagException;

	/**
     * Checks if the specified tag is illegally repeated in the item and throws an exception if so.
     *
     * @param index -- the tag index.
     */
	public void checkRepeatability(final int index) throws DataAccessException, DuplicateTagException {
		final Tag t = getTag(index);
		Validation bv = t.getValidation();
		if (!bv.isMarcTagRepeatable()) {
            getTags().remove(index);
		    getTags().sort(tagComparator);
			if (Collections.binarySearch(getTags(), t, tagComparator) >= 0) {
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
	public Tag findFirstTagByNumber(final String s) {
		try {
			return getTags().stream().filter(tag -> tag.getMarcEncoding().getMarcTag().startsWith(s)).findFirst().orElse(null);
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
	 *
	 * @return tags to delete.
	 */
	public List<Tag> getDeletedTags() {
		return deletedTags;
	}

	public abstract ItemEntity getItemEntity();

	public ModelItem getModelItem() {
		return modelItem;
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

	abstract public int getUserView();

	/**
	 *
	 * @return the verification level of item entity.
	 */
	public char getVerificationLevel() {
		return getItemEntity().getVerificationLevel();
	}

	/**
	 * remove a tag from the deletedTags list (by Object)
	 *
	 */
	public void removeDeletedTag(Tag tag) {
		deletedTags.remove(tag);
	}

	/**
	 * remove a tag from the Item (by Object)
	 *
	 */
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}

	public abstract void setItemEntity(ItemEntity item);

	/**
	 * Sets model item associated to item.
	 *
	 * @param model -- the model to set.
	 * @throws DataAccessException in
	 */
	public void setModelItem(final Model model) {
		this.modelItem.setItem(this.getAmicusNumber().longValue());
		this.modelItem.setModel(model);
		this.modelItem.setRecordFields(model.getRecordFields());
	}

	public void setModelItemNoAN(Model model) {
		this.modelItem = new BibliographicModelItem();
		this.modelItem.markNew();
		this.modelItem.setModel(model);
		this.modelItem.setRecordFields(model.getRecordFields());
	}
	/**
     * Replacing old tag with a new one in the bibItem.
	 *
	 */
	public void setTag(Tag oldTag, Tag newTag) {
		if (getAmicusNumber() != null) {
			newTag.setItemNumber(getAmicusNumber().intValue());
		}
		tags.set(tags.indexOf(oldTag), newTag);
	}

	public void setTags(List<Tag> set) {
		tags = set;
	}

	public void setVerificationLevel(char verificationLevel) {
		getItemEntity().setVerificationLevel(verificationLevel);
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
			logger.info(MessageCatalogStorage._00017_MARC_CORRELATION_SORTING);
		} catch (DataAccessException e) {
			logger.info(MessageCatalogStorage._00010_DATA_ACCESS_FAILURE);
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
        tagContainers.stream().forEach(item -> {
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
	private LinkedHashMap<Object, TagContainer> populateGroups() throws DataAccessException {
		final LinkedHashMap<Object, TagContainer> ht = new LinkedHashMap();
		final GroupManager groupManager = new BibliographicGroupManager();

		tags.stream().forEach(tag -> {
            final TagGroup group = groupManager.getGroup(tag);
            if(group==null) {
                ht.put(tag, new UniqueTagContainer(tag));
            }else {
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
     * Conversion of catalog item into marc21 format.
     *
     * @return a byte[] representing marc21 record.
     * @throws DataAccessException in case of data access exception.
     */
	public byte[] toMarc() throws DataAccessException {
		final DecimalFormat n4 = new DecimalFormat("0000");
		final DecimalFormat n5 = new DecimalFormat("00000");
		final ByteArrayOutputStream directory = new ByteArrayOutputStream();
		final ByteArrayOutputStream body = new ByteArrayOutputStream();
		final ByteArrayOutputStream record = new ByteArrayOutputStream();
		final Leader leaderTag = (Leader) getTag(0);
		leaderTag.getItemEntity().setCharacterCodingSchemeCode('a');
		final String leader = leaderTag.getDisplayString();

        try {
		    this.getTags().stream().skip(1).forEach(aTag -> {
                try {
                    final CorrelationKey correlation = aTag.getMarcEncoding();
                    final String entry = aTag.isFixedField()
                            ? (((FixedField) aTag).getDisplayString() + Subfield.FIELD_DELIMITER)
                            : ("" + correlation.getMarcFirstIndicator() + correlation.getMarcSecondIndicator() +
                            ((VariableField) aTag).getStringText().getMarcDisplayString(Subfield.SUBFIELD_DELIMITER) + Subfield.FIELD_DELIMITER);

                    int offset = body.size();
                    body.write(entry.getBytes(GlobalStorage.CHARSET_UTF8));
                    directory.write(correlation.getMarcTag().getBytes(GlobalStorage.CHARSET_UTF8));
                    directory.write(
                            n4.format(body.size() - offset).getBytes(GlobalStorage.CHARSET_UTF8));
                    directory.write(n5.format(offset).getBytes(GlobalStorage.CHARSET_UTF8));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
		    });

            directory.write(Subfield.FIELD_DELIMITER.getBytes(GlobalStorage.CHARSET_UTF8));
            body.write(Subfield.RECORD_DELIMITER.getBytes(GlobalStorage.CHARSET_UTF8));
            record.write(n5.format(body.size() + directory.size() + leader.length()).getBytes(GlobalStorage.CHARSET_UTF8));
            record.write(leader.substring(5, 12).getBytes(GlobalStorage.CHARSET_UTF8));
            record.write(
                    n5.format(directory.size() + leader.length()).getBytes(
                            GlobalStorage.CHARSET_UTF8));
            record.write(leader.substring(17).getBytes(GlobalStorage.CHARSET_UTF8));
            record.write(directory.toByteArray());
            record.write(body.toByteArray());

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

		return record.toByteArray();
	}


    /**
     * Validates, check mandatory and repeatability tags.
     *
     * @throws ValidationException in case of validation exception.
     * @throws DataAccessException in case of data access exception.
     */
	public void validate() throws ValidationException, DataAccessException {
		checkForMandatoryTags();
		for (int i = 0; i < getTags().size(); i++) {
			checkRepeatability(i);
			getTag(i).validate(i);
		}
	}

    /**
     * Gets a filtered list og tags.
     *
     * @param filter -- the filter to apply.
     * @param optionalFilterParameter -- the optional filter parameter.
     * @return filtered tag list.
     */
	public List<Tag> findTags(final TagFilter filter, final Object optionalFilterParameter) {
		List<Tag> tags = getTags();
		return tags.stream().filter(current -> filter.accept(current, optionalFilterParameter)).collect(Collectors.toList());
	}

    /**
     * Checks if descriptor is already present in tag as browsable descriptor.
     *
     * @param descriptor -- the descriptor to find.
     * @param current -- the current tag to check.
     * @return true if already present, false otherwise.
     * @throws DataAccessException in case of data access exception.
     */
	public boolean isDescriptorAlreadyPresent(final Descriptor descriptor, final Tag current) throws DataAccessException {
        if (!(current instanceof AccessPoint))
            return false;

        if (findTagsEqual(((AccessPoint)current).getFunctionCode()).size() >= 2) {
            if(!descriptor.equals(((Browsable)current).getDescriptor()))
                return !findTags(new SameDescriptorTagFilter(), descriptor).isEmpty();
        }
        return false;
	}

    /**
     * Finds tags having same function code and same access point.
     *
     * @param functionCode -- the function code used as filter criteria.
     * @return list of tags.
     * @throws DataAccessException in case of data access failure.
     */
	public List<Tag> findTagsEqual(final int functionCode) throws DataAccessException {
		final List<Tag> tags = getTags();
		return tags.stream().filter(current -> current instanceof AccessPoint)
				.filter(current -> ((AccessPoint)current).getFunctionCode()==functionCode)
				.collect(Collectors.toList());
	}

    /**
     * Finds fixed tags having same code number.
     *
     * @param marcTag -- the tag number used as filter criteria.
     * @return list of tags.
     * @throws DataAccessException in case of data access failure.
     */
	public List<Tag> findTagsFixedEqual(final String marcTag) throws DataAccessException {
		List<Tag> tags = getTags();
		return tags.stream().filter(current -> current instanceof FixedField)
                .filter(tag -> tag.getMarcEncoding().getMarcTag().equals(marcTag))
                .collect(Collectors.toList());
	}

    /**
     * Finds variable tags having same code number.
     *
     * @param marcTag -- the tag number used as filter criteria.
     * @return list of tags.
     * @throws DataAccessException in case of data access failure.
     */
	public List<Tag> findTagsVariableEqual(final String marcTag) throws DataAccessException {
		List<Tag> tags = getTags();
        return tags.stream().filter(current -> current instanceof VariableField)
                .filter(tag -> tag.getMarcEncoding().getMarcTag().equals(marcTag))
                .collect(Collectors.toList());
	}

    /**
     * Finds tags with same marc category code.
     *
     * @param marcCategory -- the marc category code used as filter criteria.
     * @return list of tags.
     */
	public List findTagByCategory(final int marcCategory)
	{
		try {
            List<Tag> tags = getTags();
            return tags.stream().filter(tag -> tag.getMarcEncoding().getMarcTagCategoryCode()==(marcCategory))
                    .collect(Collectors.toList());
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
}
