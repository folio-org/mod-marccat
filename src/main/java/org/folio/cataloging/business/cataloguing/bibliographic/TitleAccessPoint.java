package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.ConfigHandler;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.marchelper.MarcHelperTag;
import org.folio.cataloging.dao.DAOBibliographicCorrelation;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.DAOTitleDescriptor;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.TTL_HDG;
import org.folio.cataloging.dao.persistence.TitleFunction;
import org.folio.cataloging.dao.persistence.TitleSecondaryFunction;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;

import static org.folio.cataloging.F.deepCopy;

/**
 *
 * @author paulm
 * @since 1.0
 */
public class TitleAccessPoint extends NameTitleComponent implements MarcHelperTag, OrderedTag, Equivalent {
	private static final long serialVersionUID = 1636144329543139231L;
	
	private String institution;
	private Integer seriesIssnHeadingNumber;
	private short secondaryFunctionCode;
	private String volumeNumberDescription;
	private String variantTitle;
	private TTL_HDG descriptor = new TTL_HDG();
	private static final String VARIANT_CODES = "3civ5";
	private Integer sequenceNumber;
	private ConfigHandler configHandler = ConfigHandler.getInstance();

	public TitleAccessPoint() {
		setDefaultTypeAndFunction();
	}

	public TitleAccessPoint(int itemNbr) {
		super(itemNbr);
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String string) {
		institution = string;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof TitleAccessPoint))
			return false;
		final TitleAccessPoint other = (TitleAccessPoint) obj;
		return super.equals(obj)
			&& (other.getFunctionCode() == this.getFunctionCode())
			&& (other.nameTitleHeadingNumber == this.nameTitleHeadingNumber);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Descriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Descriptor ttl_hdg) {
		descriptor = (TTL_HDG) ttl_hdg;
	}

	@Override
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(TitleFunction.class,true);
	}

	@Override
	public List getSecondCorrelationList(short value1) throws DataAccessException {
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getSecondCorrelationList(getCategory(),value1,TitleSecondaryFunction.class);
	}

	@Override
	public List getThirdCorrelationList(short value1, short value2) throws DataAccessException {
		return null;
	}

	public short getSecondaryFunctionCode() {
		return secondaryFunctionCode;
	}

	public Integer getSeriesIssnHeadingNumber() {
		return seriesIssnHeadingNumber;
	}

	public String getVariantTitle() {
		return variantTitle;
	}

	public String getVolumeNumberDescription() {
		return volumeNumberDescription;
	}

	public void setSecondaryFunctionCode(short s) {
		secondaryFunctionCode = s;
	}

	public void setSeriesIssnHeadingNumber(Integer integer) {
		seriesIssnHeadingNumber = integer;
	}

	public void setVariantTitle(String string) {
		variantTitle = string;
	}

	public void setVolumeNumberDescription(String string) {
		volumeNumberDescription = string;
	}

	@Override
	public String getRequiredEditPermission() {
		return "editTitle";
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return (v.isValueDefined(1) && (v.getValue(1) != getFunctionCode()));
	}

	@Override
	public CorrelationValues getCorrelationValues() {
		return getDescriptor().getCorrelationValues().change(1, getFunctionCode()).change(2, getSecondaryFunctionCode());
	}

	@Override
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(1));
		setSecondaryFunctionCode(v.getValue(2));
		getDescriptor().setCorrelationValues(v);
	}

	@Override
	public StringText getStringText() {
		StringText result = new StringText();
		result.add(getAccessPointStringText().getSubfieldsWithCodes("i"));
		if (getDescriptor() != null) {
			result.parse(getDescriptor().getStringText());
		}
		result.add(getAccessPointStringText().getSubfieldsWithoutCodes("i"));
		return result;
	}

	@Override

	public StringText getAccessPointStringText() 
	{
		//TODO _JANICK to the Dark Side, this issn number leads
		StringText text = new StringText(variantTitle);
		text.parse(institution);
		if(this.getSeriesIssnHeadingNumber()!=null)
			text.addSubfield(new Subfield("x",""+getISSNText()));
		text.parse(volumeNumberDescription);
		return text;
	}

	@Override
	public void setAccessPointStringText(final StringText stringText) {
		variantTitle = stringText.getSubfieldsWithCodes("ci").toString();
		institution = stringText.getSubfieldsWithCodes("5").toString();
		if(!stringText.getSubfieldsWithCodes("x").isEmpty() && this.getSeriesIssnHeadingNumber()!=null) {
			seriesIssnHeadingNumber = new Integer(this.getSeriesIssnHeadingNumber().intValue());
		} else {
			seriesIssnHeadingNumber = null;
		}
		volumeNumberDescription = stringText.getSubfieldsWithCodes("v").toString();
	}

	public void setDescriptorStringText(final StringText stringText) {
		getDescriptor().setStringText(
			stringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
	}
	@Override
	public short getCategory() {
		return 3;
	}

	@Override
	public String getVariantCodes() {
		return VARIANT_CODES;
	}

	@Override
	public String getKey() throws DataAccessException {
		return getMarcEncoding().getMarcTag();
	}
	
	public String getISSNText(){
		DAOTitleDescriptor daoTitle= new DAOTitleDescriptor();
		return daoTitle.getISSNString(this.getSeriesIssnHeadingNumber().intValue());
	
	}

	@Override
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = integer;
	}

	@Override
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException {
		DAODescriptor dao = new DAOTitleDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView, indexingLanguage);
	    if (ref!=null) {
			AccessPoint aTag =	(AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(),cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor().getKey().getHeadingNumber()));
			newTags.add(aTag);
		}
		return newTags;
	}
	
	public void setDefaultTypeAndFunction() {
		int typCode= new Integer(configHandler.findValue("t_ttl_fnctn_2nd_fnctn","titleAccessPoint.functionCode"));		
		int type = configHandler.isParamOfGlobalVariable("t_ttl_fnctn_2nd_fnctn") ? this.getType(typCode) : typCode;
		
		setFunctionCode((short)type);
		int funCode= new Integer(configHandler.findValue("t_ttl_fnctn_2nd_fnctn","titleAccessPoint.secondaryFunctionCode"));
		int function= this.getFunction(funCode);
		setSecondaryFunctionCode((short)function);
	}
}