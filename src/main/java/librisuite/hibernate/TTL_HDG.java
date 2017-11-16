/*
 * Created on May 6, 2004
 * */
package librisuite.hibernate;

import java.io.Serializable;

import librisuite.business.cataloguing.bibliographic.TitleAccessPoint;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.*;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * Hibernate class for table TTL_HDG
 * @author paulm
 * @version $Revision: 1.23 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class TTL_HDG extends Descriptor implements SkipInFiling, Serializable {
	private static final long serialVersionUID = 1L;
	private static Class referenceClass = TTL_REF.class;
	private char copyToSubjectIndicator;
	private short indexingLanguage;
	private short skipInFiling;
	
	/**
	 * 
	 */
	public TTL_HDG() {
		super();
		setAccessPointLanguage(Defaults.getShort("title.accessPointLanguage"));
		setCopyToSubjectIndicator(Defaults.getChar("title.copyToSubjectIndicator"));
		setVerificationLevel(Defaults.getChar("title.verificationLevel"));
		setSkipInFiling(Defaults.getShort("title.skipInFiling"));
		setIndexingLanguage(Defaults.getShort("title.indexingLanguage"));
	}

	/**
	 * Getter for copyToSubjectIndicator
	 * 
	 * @return copyToSubjectIndicator
	 */
	public char getCopyToSubjectIndicator() {
		return copyToSubjectIndicator;
	}

	/**
	 * Getter for indexingLanguage
	 * 
	 * @return indexingLanguage
	 */
	public short getIndexingLanguage() {
		return indexingLanguage;
	}


	/**
	 * Setter for copySubjectIndicator
	 * 
	 * @param c copySubjectIndicator
	 */
	public void setCopyToSubjectIndicator(char c) {
		copyToSubjectIndicator = c;
	}

	/**
	 * Setter for indexingLanguage
	 * 
	 * @param s indexingLanguage
	 */
	public void setIndexingLanguage(short s) {
		indexingLanguage = s;
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.rdms.Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		if (targetClazz == TTL_HDG.class) {
			return TTL_REF.class;
		} else if (targetClazz == NME_TTL_HDG.class) {
			return TTL_NME_TTL_REF.class;
		} else if (targetClazz == NME_HDG.class) {
			return NME_TO_TTL_REF.class;
		} else {
			return null;
		}
	}

	public short getSkipInFiling() {
		return skipInFiling;
	}

	public void setSkipInFiling(short s) {
		skipInFiling = s;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "7P0";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOTitleDescriptor();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return TitleAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "TH";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public short getCategory() {
		return 22;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		// do nothing		
	}
	

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(100, 102, 0, 0, getSkipInFiling());
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getHeadingNumberSearchIndex()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "228P";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#calculateSortFormWithoutDB()
	 */
	protected String calculateSortFormWithoutDB() {
		StringText st = SortformUtils.stripSkipInFiling(getStringText(), getSkipInFiling());
		return SortformUtils.defaultSortform(st.toString());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#changeAffectsCacheTable()
	 */
	public boolean changeAffectsCacheTable() {
		return true;
	}
	public String getLockingEntityType() {
		return "TH";
	}
}
