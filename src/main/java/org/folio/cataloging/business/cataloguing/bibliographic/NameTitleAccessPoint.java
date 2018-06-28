/*
 * (c) LibriCore
 * 
 * Created on Dec 12, 2005
 * 
 * NameTitleAccessPoint.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.ConfigHandler;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.BibliographicCorrelationDAO;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.DAONameTitleAccessPoint;
import org.folio.cataloging.dao.DAONameTitleDescriptor;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;

import static org.folio.cataloging.F.deepCopy;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class NameTitleAccessPoint extends NameTitleComponent implements OrderedTag ,Equivalent{

	private static final String VARIANT_CODES = "3v5";
	private static final DAONameTitleAccessPoint daoNameTitleAccessPoint =
		new DAONameTitleAccessPoint();
	private NME_TTL_HDG descriptor = new NME_TTL_HDG();
	private String institution;
	private Short secondaryFunctionCode;
	private static final Log logger = LogFactory.getLog(NameTitleAccessPoint.class);
	private ConfigHandler configHandler =ConfigHandler.getInstance();
	
	/*
	 * Note that the Issn in name/title was not converted when the one in titles was.
	 * This was an oversight, but for now, the data here is still a string.  We are 
	 * not correcting it now, because it is also being considered to re-convert all 
	 * ttl issn's back to a string (rather than a foreign key to CNRL_NBR)
	 */
	private String seriesIssnHeadingNumber;
	private String volumeNumberDescription;
	private Integer sequenceNumber;
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public NameTitleAccessPoint() {
		super();
	//	setFunctionCode(Defaults.getShort("nameTitleAccessPoint.functionCode"));
		setDefaultFunctionCode();
		setSecondaryFunctionCode(
			new Short(
				Defaults.getShort(
					"nameTitleAccessPoint.secondaryFunctionCode")));
	}

	/**
	 * Class constructor
	 *
	 * @param itemNbr
	 * @since 1.0
	 */
	public NameTitleAccessPoint(int itemNbr) {
		super(itemNbr);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		//TODO _JANICK to the Dark Side, this issn number leads
		StringText text = new StringText(volumeNumberDescription);
		text.parse(institution);
		return text;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		/*
		 * NameTitleAccessPoints in bib have function code first in the correlation
		 * so we move down the heading correlation values.  c.f. Authority tags where
		 * the NT heading correlation has the descriptor correlation values first.
		 */
		CorrelationValues v = getDescriptor().getCorrelationValues();
		v = v.change(3, v.getValue(2));
		v = v.change(2, v.getValue(1));
		v = v.change(1, getFunctionCode());
		return v;
	}

	/* (non-Javadoc)
	 * @see Browsable#getDescriptor()
	 */
	public Descriptor getDescriptor() {
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_NME_TTL_FNCTN.class,false);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Short getSecondaryFunctionCode() {
		return secondaryFunctionCode;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		BibliographicCorrelationDAO dao = new BibliographicCorrelationDAO();
		return dao.getSecondCorrelationList(
			getCategory(),
			value1,
			NameType.class);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getSeriesIssnHeadingNumber() {
		return seriesIssnHeadingNumber;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		BibliographicCorrelationDAO dao = new BibliographicCorrelationDAO();
		return dao.getThirdCorrelationList(
			getCategory(),
			value1,
			value2,
			NameSubType.class);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getVolumeNumberDescription() {
		return volumeNumberDescription;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#setAccesspointStringText(org.folio.cataloging.util.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		//		TODO _JANICK externalize codes
		volumeNumberDescription =
			stringText.getSubfieldsWithCodes("v").toString();
		institution = stringText.getSubfieldsWithCodes("5").toString();
	}

	/* (non-Javadoc)
	 * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(1));
		v = v.change(1, v.getValue(2));
		v = v.change(2, v.getValue(3));
		v = v.change(3, CorrelationValues.UNDEFINED);
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see Browsable#setDescriptor(Descriptor)
	 */
	public void setDescriptor(Descriptor descriptor) {
		this.descriptor = (NME_TTL_HDG) descriptor;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDescriptor(NME_TTL_HDG nme_ttl_hdg) {
		descriptor = nme_ttl_hdg;
	}

	public void setDescriptorStringText(StringText stringText) {
		getDescriptor().setStringText(
			stringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setInstitution(String string) {
		institution = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSecondaryFunctionCode(Short s) {
		secondaryFunctionCode = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSeriesIssnHeadingNumber(String s) {
		seriesIssnHeadingNumber = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setVolumeNumberDescription(String string) {
		volumeNumberDescription = string;
	}

	/* (non-Javadoc)
	 * @see Tag#getDAO()
	 */
	public HibernateUtil getDAO() {
		return daoNameTitleAccessPoint;
	}
	
	public String getVariantCodes() {
		return VARIANT_CODES;
	}
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = integer;
	}
	/**
	 * 
	 */
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	
	public List replaceEquivalentDescriptor(short indexingLanguage,
			int cataloguingView) throws DataAccessException {
		DAODescriptor dao = new DAONameTitleDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView,
				indexingLanguage);
		if (ref!=null) {
			AccessPoint aTag = (AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(), cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor().getKey()
					.getHeadingNumber()));
			newTags.add(aTag);
		}
		return newTags;
	}
	
	public void setDefaultFunctionCode(){
		short functionCode=0;
		functionCode= new Short(configHandler.findValue("t_nme_ttl_fnctn","nameTitleAccessPoint.functionCode"));
		setFunctionCode(functionCode);
	}



}
