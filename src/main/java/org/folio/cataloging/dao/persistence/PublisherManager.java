package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.cataloguing.bibliographic.Equivalent;
import org.folio.cataloging.business.cataloguing.bibliographic.PublisherTagComparator;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.PublisherManagerDAO;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

/**
 * Management class for publishers.
 *
 * Multiple publisher headings contribute to a single tag, controlled from table PUBL_TAG.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class PublisherManager extends VariableField implements PersistentObjectWithView , Equivalent
{
	private static final long serialVersionUID = 1L;
	private static final Log logger = new Log(PublisherManager.class);
	private static final PublisherManagerDAO daoPublisherTag = new PublisherManagerDAO();

	private PersistenceState persistenceState = new PersistenceState();
	private List<PUBL_TAG> publisherTagUnits = new ArrayList<>();
	private List<PUBL_TAG> deletedUnits = new ArrayList<>();
	private PublisherAccessPoint apf = new PublisherAccessPoint();
	private List<String> dates = new ArrayList<>();
	private int tagUnitIndex;
	private UserViewHelper userViewHelper = new UserViewHelper();
	private String stringTextForFastDigitPublisher;
	private int noteType;

	/**
	 * Gets stringText for subfield e,f,g in fast mode.
	 *
	 * @return stringTextForFastDigitPublisher.
	 */
	public String getStringTextForFastDigitPublisher() {
		return stringTextForFastDigitPublisher;
	}

	/**
	 * Sets stringText for subfield e,f,g in fast mode.
	 *
	 * @param stringTextForFastDigitPublisher -- the string text to set.
	 */
	public void setStringTextForFastDigitPublisher(final String stringTextForFastDigitPublisher) {
		this.stringTextForFastDigitPublisher = stringTextForFastDigitPublisher;
	}

	/**
	 * Gets note type associated to publisher.
	 *
	 * @return noteType.
	 */
	public int getNoteType() {
		return noteType;
	}

	/**
	 * Sets note type to publisher.
	 *
	 * @param noteType -- the note type to set.
	 */
	public void setNoteType(final int noteType) {
		this.noteType = noteType;
	}

	/**
	 * Default constructor.
	 */
	public PublisherManager()
	{
		super();
		setPersistenceState(persistenceState);
		setNoteType(GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE);
		setApf(new PublisherAccessPoint());
	}

	/**
	 * @param bibItemNumber -- record number.
	 * @param view -- user view.
	 */
	public PublisherManager(final int bibItemNumber, final int view)
	{
		super();
		setApf(new PublisherAccessPoint(bibItemNumber));
		final String singleViewString = View.makeSingleViewString(view);
		getApf().setUserViewString(singleViewString);
		setPersistenceState(persistenceState);
		setBibItemNumber(bibItemNumber);
		setUserViewString(singleViewString);
		setNoteType(GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE);
	}

	/**
	 * @param apf -- publisher access point.
	 */
	public PublisherManager(final PublisherAccessPoint apf)
	{
		super();
		setPersistenceState(persistenceState);
		setItemNumber(apf.getItemNumber());
		setUserViewString(apf.getUserViewString());
		setUpdateStatus(apf.getUpdateStatus());
		setApf(apf);
		getDates().add("");
		setPublisherTagUnits(((PublisherTagDescriptor)getApf().getDescriptor()).getPublisherTagUnits());
        getPublisherTagUnits().sort(new PublisherTagComparator());
		setNoteType(GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE);
	}

	/**
	 * Copy dates from access points into Publisher Tag while editing
	 */
	private void copyDates() {
		getPublisherTagUnits().stream().forEach(publisherTag -> {
			getDates().add(publisherTag.getDate());
		});
	}

	/**
	 * Extracts subfields e,f,g from the "last" access point and stores them in separate member variables.
	 */
	private void extractManufacturerData()
	{
        final PUBL_TAG last = getPublisherTagUnits().stream().reduce((a, b) -> b).orElse(null);
        if (ofNullable(last).isPresent()){
            final StringText stringText = new StringText(last.getOtherSubfields());
            last.setOtherSubfields(stringText.getSubfieldsWithoutCodes(GlobalStorage.PUBLISHER_FAST_PRINTER_SUBFIELD_CODES).toString());
            final String remainingFieldsText = stringText.getSubfieldsWithCodes(GlobalStorage.PUBLISHER_FAST_PRINTER_SUBFIELD_CODES).toString();
            if (F.isNotNullOrEmpty(remainingFieldsText) && remainingFieldsText.contains(Subfield.SUBFIELD_DELIMITER)){
                setStringTextForFastDigitPublisher(remainingFieldsText.replaceAll(Subfield.SUBFIELD_DELIMITER, Global.SUBFIELD_DELIMITER_FOR_VIEW));
            }else
                setStringTextForFastDigitPublisher(remainingFieldsText);
        }
	}


    /**
     * Updates the publisher tag with the recently browsed publisher heading.
     *
     * @param p -- the publisher heading to update.
     * @throws DataAccessException in case of data access exception.
     */
	//TODO move it in storageService
	@Deprecated
	public void updatePublisherFromBrowse(final PUBL_HDG p) throws DataAccessException {
		/*
	    PUBL_HDG myP = (PUBL_HDG) ((DAODescriptor) p.getDAO())
                .findOrCreateMyView(p.getHeadingNumber(), p.getUserViewString(), View.toIntView(getUserViewString()));
		PUBL_TAG tagUnit = getPublisherTagUnits().get(getTagUnitIndex());
		tagUnit.setDescriptor(myP);
		*/
	}

    /**
     * Gets stringText associated to publisher tag.
     *
     * @return stringText.
     */
	public StringText getStringText()
	{
		final StringText result = new StringText();
        getPublisherTagUnits().stream().forEach(aTagUnit -> {
            result.add(new StringText(aTagUnit.getStringText()));
        });
		return result;
	}

	/**
	 * @return the MARC display string for the publisher tag
	 */
	public String getDisplayString() {
		return getStringText().getMarcDisplayString();
	}

    /**
     * Sets stringText to publisher.
     *
     * @param stringText -- the string text to set.
     */
	public void setStringText(final StringText stringText) {
        setPublisherTagUnits(new ArrayList<>());
        String lastSubfield = "a";
        StringText newText = new StringText();
		List<Subfield> theList = stringText.getSubfieldList();

        for (Subfield aSubfield : theList) {
            if (aSubfield.getCode().compareTo(lastSubfield) < 0 || theList.lastIndexOf(aSubfield) == theList.size() - 1) {
                if (theList.lastIndexOf(aSubfield) == theList.size() - 1) {
                    newText.addSubfield(aSubfield);
                }
                if (newText.getNumberOfSubfields() > 0) {
                    addNewTagUnit();
                    final PUBL_TAG p = getPublisherTagUnits().get(getPublisherTagUnits().size() - 1);
                    p.setStringText(newText.toString());
                    newText = new StringText();
                }
            }
            newText.addSubfield(aSubfield);
            lastSubfield = aSubfield.getCode();
        }
        parseForEditing();
	}

    /**
     * Gets title marc category code.
     *
     * @return category.
     */
	public int getCategory() {
		return GlobalStorage.PUBLISHER_CATEGORY;
	}

    /**
     * Gets correlation values of publisher.
     *
     * @return correlationValues.
     */
	public CorrelationValues getCorrelationValues() {
		return getApf().getCorrelationValues();
	}

    /**
     * Gets list of tag publisher units.
     *
     * @return publisherTagUnits.
     */
	public List<PUBL_TAG> getPublisherTagUnits() {
		return publisherTagUnits;
	}

    /**
     * Sets list of tag publisher unit.
     *
     * @param list -- the list to set.
     */
	public void setPublisherTagUnits(List<PUBL_TAG> list) {
		publisherTagUnits = list;
	}

	@Deprecated
	public List getFirstCorrelationList() throws DataAccessException {
		/* return getDaoCodeTable().getList(BibliographicNoteType.class); */
		return null;
	}

    /**
     * Compares an object with another one.
     *
     * @param obj -- the object to compare.
     * @return true if equals.
     */
	public boolean equals(final Object obj) {
		if (obj instanceof PublisherManager) {
			PublisherManager p = (PublisherManager) obj;
			return p.getApf().equals(this.getApf());
		}
		return false;
	}

    /**
     *
     * @return daoPublisherTag.
     */
	public AbstractDAO getDAO() {
		return daoPublisherTag;
	}

    /**
     * Gets default implementation.
     *
     * @return false.
     */
    public boolean isWorksheetEditable() {
		return false;
	}

    /**
     * Gets default implementation.
     *
     * @return true.
     */
	public boolean isPublisher() {
		return true;
	}

    /**
     * Gets list of deleted tag publisher unit.
     *
     * @return deletedUnits.
     */
	public List<PUBL_TAG> getDeletedUnits() {
		return deletedUnits;
	}

    /**
     * Sets list of deleted tag publisher unit.
     *
     * @param list -- the list to set.
     */
	public void setDeletedUnits(final List<PUBL_TAG> list) {
		deletedUnits = list;
	}

	public int getTagUnitIndex() {
		return tagUnitIndex;
	}
	public void setApfIndex(final int i) {
		tagUnitIndex = i;
	}

    /**
     * Returns list of dates.
     *
     * @return dates.
     */
	public List<String> getDates() {
		return dates;
	}

    /**
     * Sets list of dates.
     *
     * @param list -- the list to set.
     */
	public void setDates(final List<String> list) { dates = list; }

    /**
     * Creates a new tag publisher unit and add it to publisherTagUnits list.
     *
     */
	public void addNewTagUnit() {
		final PUBL_TAG tagUnit = new PUBL_TAG();
		tagUnit.setUserViewString(getUserViewString());
		getPublisherTagUnits().add(tagUnit);
		getDates().add("");
	}

	/**
	 * Extract dates and manufacturer data for editing as individual fields.
	 */
	public void parseForEditing() {
		extractManufacturerData();
		copyDates();
	}

	/**
	 * Incorporate edit changes (dates, manufacturer data, sequences, etc.) into the tagUnits ready for saving to the database.
	 *
	 */
	public void saveEdits() {

        IntStream.range(0, getPublisherTagUnits().size())
            .forEach(idx -> {
                final PUBL_TAG publTag = getPublisherTagUnits().get(idx);
                final PUBL_HDG publUnit = publTag.getDescriptor();
                if(publUnit != null && publUnit.getKey().getHeadingNumber()==-1){
                    publUnit.setNameStringText("");
                    publUnit.setPlaceStringText("");
                }
                publTag.setSequenceNumber(idx + 1);
                String date = "";
                if (!"".equals(getDates().get(idx)))
                    date = Subfield.SUBFIELD_DELIMITER + "c" + getDates().get(idx);

                publTag.setOtherSubfields(date);
            });
        setDates(new ArrayList<>());

		final StringText s = new StringText();
		s.add(new StringText(getStringTextForFastDigitPublisher()));
		if (s.getNumberOfSubfields() > 0) {
			if (getPublisherTagUnits().size() == 0) {
				getPublisherTagUnits().add(new PUBL_TAG());
			}

            final PUBL_TAG last = getPublisherTagUnits().stream().reduce((a, b) -> b).orElse(null);
			if (ofNullable(last).isPresent()){
                StringText st = new StringText(last.getOtherSubfields()).add(s);
                last.setOtherSubfields(st.toString());
            }
		}
		((PublisherTagDescriptor)getApf().getDescriptor()).setPublisherTagUnits(getPublisherTagUnits());
	}

    /**
     * Gets user view string associated.
     *
     * @return userView.
     */
	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

    /**
     * Sets user view string.
     *
     * @param userview -- the param to set.
     */
	public void setUserViewString(final String userview) {
		userViewHelper.setUserViewString(userview);
	}

    /**
     * Returns the record amicus number.
     *
     * @return itemNumber.
     */
	public int getBibItemNumber() {
		return getItemNumber();
	}

    /**
     * Sets the record item number.
     *
     * @param i -- the item number to set.
     */
	public void setBibItemNumber(final int i) {
		setItemNumber(i);
	}

    /**
     * Evict
     * @throws DataAccessException in case of data access exception.
     */
	public void evict() throws DataAccessException {
		final PublisherAccessPoint apf = getApf();
		apf.evict();
        getPublisherTagUnits().stream().forEach(tagUnit -> {
            tagUnit.evict();
        });
	}

    /**
     * Sets correlation values related to publisher.
     *
     * @param v -- the correlation values to set.
     */
	public void setCorrelationValues(final CorrelationValues v) {
		getApf().setCorrelationValues(v);
	}

    /**
     * Mark as changed.
     */
	public void markChanged() {
        getPublisherTagUnits().forEach(tagUnit -> tagUnit.markChanged());
		super.markChanged();
	}

    /**
     * Mark as deleted.
     */
	public void markDeleted() {
        getPublisherTagUnits().forEach(tagUnit -> tagUnit.markDeleted());
		super.markDeleted();
	}

    /**
     * Mark as new.
     */
    public void markNew() {
        getPublisherTagUnits().forEach(tagUnit -> tagUnit.markNew());
        super.markNew();
    }

    /**
     * Mark as unchanged.
     */
	public void markUnchanged() {
        getPublisherTagUnits().forEach(tagUnit ->  tagUnit.markUnchanged());
		super.markUnchanged();
	}

    /**
     * Sets update status.
     */
	public void setUpdateStatus(final int i) {
        getPublisherTagUnits().forEach(tagUnit ->  tagUnit.setUpdateStatus(i));
		super.setUpdateStatus(i);
	}

    /**
     * Checks correlation key value is changed for publisher.
     *
     * @param v -- the correlation values.
     * @return boolean.
     */
	public boolean correlationChangeAffectsKey(final CorrelationValues v) {
		return !BibliographicNoteType.isPublisherType(v.getValue(1));
	}

    /**
     * Returns the access point associated to publisher.
     *
     * @return apf.
     */
	public PublisherAccessPoint getApf() {
		return apf;
	}

    /**
     * Sets the access point associated to publisher.
     *
     * @param apf -- the apf to set.
     */
	public void setApf(final PublisherAccessPoint apf) {
		this.apf = apf;
	}

	public int hashCode() {
		return getApf().hashCode();
	}

	/**
	 * Remove descriptor from publisher tag units.
	 *
	 * @param i -- the index of tag unit to remove.
	 */
	public void removeDescriptor(final int i)
	{
		PUBL_TAG tagUnit = getPublisherTagUnits().get(i);
		detachDescriptor(tagUnit);
	}
	private void detachDescriptor(final PUBL_TAG tagUnit) {
		final PUBL_HDG publ_hdg = tagUnit.getDescriptor();
		if(tagUnit.getPublisherHeadingNumber()==null){
		   publ_hdg.setNameStringText("");
		   publ_hdg.setPlaceStringText("");

	    }else{
			tagUnit.setDescriptor(null);
			tagUnit.setPublisherHeadingNumber(null);
		}
	}

	@Deprecated
	//TODO moved in storageService
	public List replaceEquivalentDescriptor(short indexingLanguage,	int cataloguingView) throws DataAccessException
	{
		return null;
	}
}
