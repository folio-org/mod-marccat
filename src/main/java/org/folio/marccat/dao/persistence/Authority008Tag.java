package org.folio.marccat.dao.persistence;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author elena
 *
 */
public class Authority008Tag extends FixedFieldUsingItemEntity {

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

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date =
			formatter.parse(
				content.getAttribute("enteredOnFileDateYYYYMMDD"),
				new ParsePosition(0));
		setEnteredOnFileDate(date);
		setSubjectDescriptor(
			content.getAttribute("subjectDescriptor").charAt(0));
		setRomanizationScheme(
			content.getAttribute("romanizationScheme").charAt(0));
		setBilingualUsage(content.getAttribute("bilingualUsage").charAt(0));
		setRecordType(content.getAttribute("recordType").charAt(0));
		setCataloguingRules(content.getAttribute("cataloguingRules").charAt(0));
		setSubjectSystem(content.getAttribute("subjectSystem").charAt(0));
		setSeriesType(content.getAttribute("seriesType").charAt(0));
		setSeriesNumbering(content.getAttribute("seriesNumbering").charAt(0));
		setMainAddedEntryIndicator(
			content.getAttribute("mainAddedEntryIndicator").charAt(0));
		setSeriesEntryIndicator(
			content.getAttribute("seriesEntryIndicator").charAt(0));
		setSubDivisionType(content.getAttribute("subDivisionType").charAt(0));
		setGovernmentAgency(content.getAttribute("governmentAgency").charAt(0));
		setReferenceStatus(content.getAttribute("referenceStatus").charAt(0));
		setRecordRevision(content.getAttribute("recordRevision").charAt(0));
		setNonUniqueName(content.getAttribute("nonUniqueName").charAt(0));
		setHeadingStatus(content.getAttribute("headingStatus").charAt(0));
		setRecordModification(
			content.getAttribute("recordModification").charAt(0));
		setCataloguingSourceCode(
			content.getAttribute("cataloguingSourceCode").charAt(0));
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute(
				"enteredOnFileDateYYYYMMDD",
				getEnteredOnFileDateYYYYMMDD());
			content.setAttribute(
				"subjectDescriptor",
				"" + getSubjectDescriptor());

			content.setAttribute(
				"romanizationScheme",
				"" + getRomanizationScheme());
			content.setAttribute("bilingualUsage", "" + getBilingualUsage());
			content.setAttribute("recordType", "" + getRecordType());
			content.setAttribute(
				"cataloguingRules",
				"" + getCataloguingRules());
			content.setAttribute("subjectSystem", "" + getSubjectSystem());
			content.setAttribute("seriesType", "" + getSeriesType());
			content.setAttribute("seriesNumbering", "" + getSeriesNumbering());
			content.setAttribute(
				"mainAddedEntryIndicator",
				"" + getMainAddedEntryIndicator());
			content.setAttribute(
				"seriesEntryIndicator",
				"" + getSeriesEntryIndicator());
			content.setAttribute("subDivisionType", "" + getSubDivisionType());
			content.setAttribute(
				"governmentAgency",
				"" + getGovernmentAgency());
			content.setAttribute("referenceStatus", "" + getReferenceStatus());
			content.setAttribute("recordRevision", "" + getRecordRevision());
			content.setAttribute("nonUniqueName", "" + getNonUniqueName());
			content.setAttribute("headingStatus", "" + getHeadingStatus());
			content.setAttribute(
				"recordModification",
				"" + getRecordModification());
			content.setAttribute(
				"cataloguingSourceCode",
				"" + getCataloguingSourceCode());
		}
		return content;
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
