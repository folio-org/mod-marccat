package org.folio.marccat.dao.persistence;

import java.util.Date;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;


/**
 * @author elena
 *
 */
public class Authority008Tag extends FixedFieldUsingItemEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3552697623964822985L;

	public Authority008Tag() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 10);
	}

	public String getDisplayString() {
		String str = "";
		str =
			str
				+ getEnteredOnFileDateYYMMDD()
				+ getSubjectDescriptor()
				+ getRomanizationScheme()
				+ getBilingualUsage()
				+ getRecordType()
				+ getCataloguingRules()
				+ getSubjectSystem()
				+ getSeriesType()
				+ getSeriesNumbering()
				+ getMainAddedEntryIndicator()
				+ getSubjectEntryIndicator()
				+ getSeriesEntryIndicator()
				+ getSubDivisionType()
				+ "          "
				+ getGovernmentAgency()
				+ getReferenceStatus()
				+ " "
				+ getRecordRevision()
				+ getNonUniqueName()
				+ getHeadingStatus()
				+ "    "
				+ getRecordModification()
				+ getCataloguingSourceCode();

		return str;
	}

	public char getCataloguingRules() {
		return getAutItm().getCataloguingRules();
	}

	public char getCataloguingSourceCode() {
		return getAutItm().getCataloguingSourceCode();
	}

	public Date getEnteredOnFileDate() {
		return getAutItm().getEnteredOnFileDate();
	}

	public String getEnteredOnFileDateYYMMDD() {
		return getAutItm().getEnteredOnFileDateYYMMDD();
	}

	public String getEnteredOnFileDateYYYYMMDD() {
		return getAutItm().getEnteredOnFileDateYYYYMMDD();
	}

	public char getGovernmentAgency() {
		return getAutItm().getGovernmentAgency();
	}

	public char getHeadingStatus() {
		return getAutItm().getHeadingStatus();
	}

	public String getLanguageOfCataloguing() {
		return getAutItm().getLanguageOfCataloguing();
	}

	public char getMainAddedEntryIndicator() {
		return getAutItm().getMainAddedEntryIndicator();
	}

	public char getNonUniqueName() {
		return getAutItm().getNonUniqueName();
	}

	public char getRecordModification() {
		return getAutItm().getRecordModification();
	}

	public char getRecordRevision() {
		return getAutItm().getRecordRevision();
	}

	public char getRecordStatusCode() {
		return getAutItm().getRecordStatusCode();
	}

	public char getRecordType() {
		return getAutItm().getRecordType();
	}

	public char getReferenceStatus() {
		return getAutItm().getReferenceStatus();
	}

	public char getRomanizationScheme() {
		return getAutItm().getRomanizationScheme();
	}

	public char getSeriesEntryIndicator() {
		return getAutItm().getSeriesEntryIndicator();
	}

	public char getSeriesNumbering() {
		return getAutItm().getSeriesNumbering();
	}

	public char getSeriesType() {
		return getAutItm().getSeriesType();
	}

	public char getSubDivisionType() {
		return getAutItm().getSubDivisionType();
	}

	public char getSubjectEntryIndicator() {
		return getAutItm().getSubjectEntryIndicator();
	}

	public char getSubjectSystem() {
		return getAutItm().getSubjectSystem();
	}

	public void setCataloguingRules(char c) {
		getAutItm().setCataloguingRules(c);
	}

	public void setCataloguingSourceCode(char c) {
		getAutItm().setCataloguingSourceCode(c);
	}

	public void setEncodingLevel(char c) {
		getAutItm().setEncodingLevel(c);
	}
	public void setEnteredOnFileDate(Date date) {
		getAutItm().setEnteredOnFileDate(date);
	}
	public void setGovernmentAgency(char c) {
		getAutItm().setGovernmentAgency(c);
	}

	public void setHeadingStatus(char c) {
		getAutItm().setHeadingStatus(c);
	}

	public void setMainAddedEntryIndicator(char c) {
		getAutItm().setMainAddedEntryIndicator(c);
	}

	public void setNonUniqueName(char c) {
		getAutItm().setNonUniqueName(c);
	}

	public void setRecordModification(char c) {
		getAutItm().setRecordModification(c);
	}

	public void setRecordRevision(char c) {
		getAutItm().setRecordRevision(c);
	}

	public void setRecordStatusCode(char c) {
		getAutItm().setRecordStatusCode(c);
	}

	public void setRecordType(char c) {
		getAutItm().setRecordType(c);
	}
	public void setReferenceStatus(char c) {
		getAutItm().setReferenceStatus(c);
	}

	public void setRomanizationScheme(char c) {
		getAutItm().setRomanizationScheme(c);
	}
	public void setSeriesEntryIndicator(char c) {
		getAutItm().setSeriesEntryIndicator(c);
	}

	public void setSeriesNumbering(char c) {
		getAutItm().setSeriesNumbering(c);
	}

	public void setSeriesType(char c) {
		getAutItm().setSeriesType(c);
	}

	public void setSubDivisionType(char c) {
		getAutItm().setSubDivisionType(c);
	}

	public void setSubjectDescriptor(char c) {
		getAutItm().setSubjectDescriptor(c);
	}

	public void setSubjectEntryIndicator(char c) {
		getAutItm().setSubjectEntryIndicator(c);
	}

	public char getSubjectDescriptor() {
		return getAutItm().getSubjectDescriptor();
	}

	public void setSubjectSystem(char c) {
		getAutItm().setSubjectSystem(c);
	}

	private AUT getAutItm() {
		return (AUT) getItemEntity();
	}
	public char getBilingualUsage() {
		return getAutItm().getBilingualUsage();
	}

	public void setBilingualUsage(char c) {
		getAutItm().setBilingualUsage(c);
	}
}
