package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.dao.DAOBibliographicRelationship;
import org.folio.cataloging.dao.DAOBibliographicRelationshipTag;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.BibliographicRelationPrintNote;
import org.folio.cataloging.dao.persistence.BibliographicRelationReciprocal;
import org.folio.cataloging.dao.persistence.BibliographicRelationType;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.folio.cataloging.F.deepCopy;

@SuppressWarnings({ "unchecked", "serial" })
public class BibliographicRelationshipTag extends VariableField implements PersistentObjectWithView 
{
	private static final Log logger = LogFactory.getLog(BibliographicRelationshipTag.class);
	private StringText reciprocalStringText = null;
	private short reciprocalOption = -1; // value -1 triggers calculation on construction
	private BibliographicRelationshipTag originalTag = null;
	public BibliographicRelationship sourceRelationship;
	public BibliographicRelationship targetRelationship;

	public BibliographicRelationshipTag() 
	{
		super();
		setSourceRelationship(new BibliographicRelationship());
		setTargetRelationship(new BibliographicRelationship());
		setReciprocalOption((short) 3);
		setRelationTypeCode(Defaults.getShort("bibliographicRelationship.relationTypeCode"));
		setRelationPrintNoteCode(Defaults.getShort("bibliographicRelationship.relationPrintNoteCode"));
		setPersistenceState(new PersistenceState());
		setOriginalTag();
	}

	public BibliographicRelationshipTag(BibliographicRelationship relationship,int userView) throws DataAccessException 
	{
		super();
		setSourceRelationship(relationship);
		setTargetRelationship(handleReciprocalRelationship(userView));
		setReciprocalOption(getReciprocalOption(userView));
		buildReciprocalStringText(userView);
		setPersistenceState(new PersistenceState());
		setOriginalTag();
	}

	public void buildReciprocalStringText(int userView)	throws DataAccessException 
	{
		/* stringtext can be in table RLTSP or should be build from the heading tables */
		StringText s = new StringText();
		if (getSourceRelationship().getTargetBibItemNumber() > 0) {
			DAOBibliographicRelationship b = new DAOBibliographicRelationship();
			s = b.buildRelationStringText(sourceRelationship.getTargetBibItemNumber(), userView);
		}
		setReciprocalStringText(s);
	}

	public void createTargetBibItem(int userView) throws DataAccessException 
	{
		if (getReciprocalOption(userView) == 1) {
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

	public boolean equals(Object obj) 
	{
		if (!(obj instanceof BibliographicRelationshipTag))
			return false;
		
		BibliographicRelationshipTag other = (BibliographicRelationshipTag) obj;
		return (other.getItemNumber() == getItemNumber())
				&& (other.getUserViewString().equals(getUserViewString()))
				&& (other.getTargetBibItemNumber() == getTargetBibItemNumber())
				&& (other.getRelationTypeCode() == getRelationTypeCode());
	}

	public void generateNewBlindRelationshipKey() throws DataAccessException 
	{
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setTargetBibItemNumber(-dao.getNextNumber("BR"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return sourceRelationship.getCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelation(int)
	 */
	public short getCorrelation(int i) {
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

	public HibernateUtil getDAO() {
		return new DAOBibliographicRelationshipTag();
	}

	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(BibliographicRelationType.class,true);
	}

	public int getItemNumber() {
		return sourceRelationship.getItemNumber();
	}

	public List getReciprocalList() throws DataAccessException {
		return getDaoCodeTable().getList(BibliographicRelationReciprocal.class,true);
	}

	public short getReciprocalOption(int userView) throws DataAccessException 
	{
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
							this.getItemNumber(), userView);
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

	public short getReciprocalOption() {
		return reciprocalOption;
	}

	public short getRelationPrintNoteCode() {
		return sourceRelationship.getRelationPrintNoteCode();
	}

	public StringText getRelationshipStringText() 
	{
		sourceRelationship.getStringText().parse(sourceRelationship.getMaterialSpecificText());
		sourceRelationship.getStringText().parse(sourceRelationship.getDescription());
		sourceRelationship.getStringText().parse(sourceRelationship.getQualifyingDescription());

		if ((getTargetBibItemNumber() > 0)) {
			sourceRelationship.getStringText().addSubfield(new Subfield("w", new String("" + getTargetBibItemNumber())));
		}
		return sourceRelationship.getStringText();
	}

	public short getRelationTypeCode() {
		return sourceRelationship.getRelationTypeCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short,
	 *      java.util.Locale)
	 */
	public List getSecondCorrelationList(short value1) throws DataAccessException {
		return getDaoCodeTable().getList(BibliographicRelationPrintNote.class,true);
	}

	public BibliographicRelationship getSourceRelationship() {
		return sourceRelationship;
	}

	public StringText getStringText() 
	{
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

	public String getStringTextString() {
		return getStringText().toString();
	}

	public int getTargetBibItemNumber() {
		return sourceRelationship.getTargetBibItemNumber();
	}

	public BibliographicRelationship getTargetRelationship() {
		return targetRelationship;
	}

	public List getThirdCorrelationList(short value1, short value2)	throws DataAccessException {
		return null;
	}

	public String getUserViewString() {
		return sourceRelationship.getUserViewString();
	}

	public BibliographicRelationship handleReciprocalRelationship(int userView) 
	{
		DAOBibliographicRelationship b = new DAOBibliographicRelationship();
		BibliographicRelationship relationship = new BibliographicRelationship();

		try {
			if (getTargetBibItemNumber() > 0) {
				relationship = b.loadReciprocalBibItem(getTargetBibItemNumber(), getItemNumber(), userView);
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
	public int hashCode() {
		return super.hashCode() + getTargetBibItemNumber() + getRelationTypeCode();
	}

	public boolean isBrowsable() {
		return false;
	}

	public boolean isRelationship() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setRelationTypeCode(v.getValue(1));
		setRelationPrintNoteCode(v.getValue(2));
	}

	public void setItemNumber(int i) {
		sourceRelationship.setItemNumber(i);
	}

	public void setReciprocalOption(short s) 
	{
		logger.debug("Setting reciprocalOption to " + s);
		reciprocalOption = s;
		if (!BibliographicRelationReciprocal.isReciprocal(s)) {
			setTargetRelationship(null);
		}
		if (BibliographicRelationReciprocal.isBlind(s)) {
			if (getTargetBibItemNumber() > 0) { //pm 2011
				setTargetBibItemNumber(0);
			}
		}
	}

	public void changeReciprocalOption(short s) {
		setReciprocalOption(s);
		markChanged();
	}

	public void setRelationPrintNoteCode(short i) {
		sourceRelationship.setRelationPrintNoteCode(i);
	}

	public void setRelationshipStringText(StringText text) {
		sourceRelationship.setRelationshipStringText(text);
	}

	public void setRelationTypeCode(short i) {
		sourceRelationship.setRelationTypeCode(i);
	}

	public void setSourceRelationship(BibliographicRelationship relationship) {
		sourceRelationship = relationship;
	}

	public void setStringText(StringText text) {
		sourceRelationship.setRelationshipStringText(text);
		if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
			sourceRelationship.setStringText(text);
		}
	}

	private void setStringTextString(String string) {
		if (string != null) {
			setStringText(new StringText(string));
		}
	}

	public void setTargetBibItemNumber(int i) {
		sourceRelationship.setTargetBibItemNumber(i);
	}

	public void setTargetRelationship(BibliographicRelationship relationship) {
		targetRelationship = relationship;
	}

	public void setUserViewString(String string) 
	{
		sourceRelationship.setUserViewString(string);
		if (targetRelationship != null) {
			targetRelationship.setUserViewString(string);
		}
	}

	public void replaceTargetRelationship(int amicusNumber, int cataloguingView) throws DataAccessException 
	{
		getSourceRelationship().evict();
		setSourceRelationship((BibliographicRelationship) deepCopy(getSourceRelationship()));
		getSourceRelationship().markNew();
		setTargetBibItemNumber(amicusNumber);
		buildReciprocalStringText(cataloguingView);
		createTargetBibItem(cataloguingView);
		markChanged();
	}

	public void setReciprocalStringText(StringText text) {
		reciprocalStringText = text;
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
	public void reinstateDeletedTag() {
		super.reinstateDeletedTag();
		setOriginalTag();
	}

	public Set getValidEditableSubfields() 
	{
		Set set = new TreeSet(new SubfieldCodeComparator());
		if (BibliographicRelationReciprocal.isBlind(getReciprocalOption())) {
			set.addAll(Arrays.asList(new String[] { "a", "s", "t", "p", "b",
					"c", "m", "d", "e", "f", "k", "n", "q","r", "u", "x", "z", "y",
					"g", "w", "i","3","4" }));
		} else {
			/* Bug 4122 */
//			set.addAll(Arrays.asList(new String[] { "c", "g", "n","q","3","4" }));
			set.addAll(Arrays.asList(new String[] { "c", "g", "i", "n","q","3","4" }));
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