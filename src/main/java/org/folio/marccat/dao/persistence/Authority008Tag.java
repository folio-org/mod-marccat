package org.folio.marccat.dao.persistence;

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

	public String getEnteredOnFileDateYYMMDD() {
		return getAutItm().getEnteredOnFileDateYYMMDD();
	}

	public char getGovernmentAgency() {
		return getAutItm().getGovernmentAgency();
	}

	public char getHeadingStatus() {
		return getAutItm().getHeadingStatus();
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

	private AUT getAutItm() {
		return (AUT) getItemEntity();
	}
	public char getBilingualUsage() {
		return getAutItm().getBilingualUsage();
	}
	
	public char getSubjectDescriptor() {
		return getAutItm().getSubjectDescriptor();
	}

}
