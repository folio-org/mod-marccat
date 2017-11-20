/*
 * (c) LibriCore
 * 
 * Created on Dec 12, 2005
 * 
 * NME_TTL_HDG.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.NameTitleAccessPoint;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.dao.DAONameTitleDescriptor;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class NME_TTL_HDG extends Descriptor {
	private static final long serialVersionUID = 1L;
	private char copyToSubjectIndicator;

	private NME_HDG nameHeading = new NME_HDG();
	private int nameHeadingNumber;
	private TTL_HDG titleHeading = new TTL_HDG();
	private int titleHeadingNumber;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public NME_TTL_HDG() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof NME_TTL_HDG) {
			return getKey().equals(obj);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return NameTitleAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getCategory()
	 */
	public short getCategory() {
		return 11;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getCopyToSubjectIndicator() {
		return copyToSubjectIndicator;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		CorrelationValues v = new CorrelationValues();
		v = v.change(1, getNameHeading().getTypeCode());
		v = v.change(2, getNameHeading().getSubTypeCode());
		return v;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAONameTitleDescriptor();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "250S";
	}

	/**
	 * 
	 * @since 1.0
	 */
	public NME_HDG getNameHeading() {
		return nameHeading;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getNameHeadingNumber() {
		return nameHeadingNumber;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "MH";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		if (targetClazz == this.getClass()) { 
			return NME_TTL_REF.class;
		}
		else if (targetClazz == NME_HDG.class){
			return NME_NME_TTL_REF.class;
		}
		else if (targetClazz == TTL_HDG.class) {
			return TTL_NME_TTL_REF.class;
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		//TODO does an NT heading have browse capabilities?
		/*
		 * Could be implemented similar to Publishers (name and place) but then you
		 * would need to look in two places if you were looking for name usage (for ex.)
		 */
		return getTitleHeading().getSortFormParameters();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public TTL_HDG getTitleHeading() {
		return titleHeading;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getTitleHeadingNumber() {
		return titleHeadingNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getKey().hashCode();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCopyToSubjectIndicator(char c) {
		copyToSubjectIndicator = c;
	}

	/* (non-Javadoc)
	 * @see Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		/* even though the user cannot change the name type/subtype via a name/title
		 * we do alter the name heading instance here for the proper behaviour of model
		 * editing and of correlation list setting on the edit screen.  In the case of
		 * models, the actual name heading is not stored.  In the case of the edit screen,
		 * the attached heading is also not modified.
		 */
		 getNameHeading().setCorrelationValues(v);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNameHeading(NME_HDG nme_hdg) {
		nameHeading = nme_hdg;
		setNameHeadingNumber(nme_hdg.getKey().getHeadingNumber());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNameHeadingNumber(int i) {
		nameHeadingNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTitleHeading(TTL_HDG ttl_hdg) {
		titleHeading = ttl_hdg;
		setTitleHeadingNumber(ttl_hdg.getKey().getHeadingNumber());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTitleHeadingNumber(int i) {
		titleHeadingNumber = i;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getStringText()
	 */
	public String getStringText() {
		StringText result = new StringText(getNameHeading().getStringText());
		StringText title = new StringText(getTitleHeading().getStringText());
		StringText subA = title.getSubfieldsWithCodes("a");
		if (subA.getNumberOfSubfields() > 0) {
			subA.getSubfield(0).setCode("t");
		}
		result.add(subA);
		result.add(title.getSubfieldsWithoutCodes("a"));
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDisplayText()
	 */
	public String getDisplayText() {
		String name = getNameHeading().getDisplayText();
		String title = getTitleHeading().getDisplayText();
		String spacer;
		if (name.endsWith(".")) {
			spacer = " ";
		} else {
			spacer = ". ";
		}
		return name + spacer + title;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getHeadingNumberSearchIndex()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "252P";
	}

	/* (non-Javadoc)
	 * @see Descriptor#setStringText(java.lang.String)
	 */
	public void setStringText(String string) {
		/* TODO
		 * Puts all subfields before $t into name and all after into title  --
		 * this may result in invalid subfields if input does not follow this 
		 * convention
		 */
		if (getNameHeading() != null) { 
			/*
			 * if the name heading is null then we are being called from the 
			 * Descriptor constructor (before init) so bypass this activity
			 */ 
			StringText s = new StringText(string);
			StringText name = new StringText();
			StringText title = new StringText();
			int i;
			for (i = 0; i < s.getNumberOfSubfields(); i++) {
				Subfield f = s.getSubfield(i);
				if (f.getCode().equals("t")) {
					f.setCode("a");
					break;
				}
				name.addSubfield(f);
			}
			for (int j = i; j < s.getNumberOfSubfields(); j++) {
				title.addSubfield(s.getSubfield(j));
			}
			getNameHeading().setStringText(name.toString());
			getTitleHeading().setStringText(title.toString());
		}
	}
	
	public String getLockingEntityType() {
		return "MH";
	}

	@Override
	public String buildBrowseTerm() {
		return getNameHeading().getDisplayText() + " : " + getTitleHeading().getDisplayText();
	}

}
