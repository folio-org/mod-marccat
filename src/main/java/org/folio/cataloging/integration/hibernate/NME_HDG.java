/*
 * Created on May 6, 2004
 * */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.cataloguing.bibliographic.NameAccessPoint;
import librisuite.business.cataloguing.common.Browsable;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.ConfigHandler;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.*;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * Hibernate class for table NME_HDG
 * @author paulm
 * @version $Revision: 1.22 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class NME_HDG extends Descriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private char copyToSubjectIndicator;
	private short indexingLanguage;
	private short subTypeCode;
	private short typeCode;
	private ConfigHandler configHandler =ConfigHandler.getInstance();
	/**
	 * 
	 * Class constructor - establishes default values for new names
	 *
	 * 
	 * @since 1.0
	 */
	public NME_HDG() {
		super();
		//setAccessPointLanguage(Defaults.getShort("name.accessPointLanguage"));
		setCopyToSubjectIndicator(
			Defaults.getChar("name.copyToSubjectIndicator"));
		/*setTypeCode(Defaults.getShort("name.typeCode"));
		setSubTypeCode(Defaults.getShort("name.subTypeCode"));*/
		setDefaultTypeAndFunction();
		//TODO add other defaults		
		setVerificationLevel(Defaults.getChar("name.verificationLevel"));

	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return NameAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public short getCategory() {
		return 17;
	}

	/**
	 * Getter for copyToSubjectIndicator
	 * 
	 * @return copyToSubjectIndicator
	 */
	public char getCopyToSubjectIndicator() {
		return copyToSubjectIndicator;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
			typeCode,
			subTypeCode,
			CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAONameDescriptor();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "2P0";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getHeadingNumberSearchIndex()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "227P";
	}

	/**
	 * Getter for indexingLanguage
	 * 
	 * @return indexingLanguage
	 */
	public short getIndexingLanguage() {
		return indexingLanguage;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "NH";
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.rdms.Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		if (targetClazz == NME_HDG.class) {
			return NME_REF.class;
		} else if (targetClazz == NME_TTL_HDG.class) {
			return NME_NME_TTL_REF.class;
		} else if (targetClazz == TTL_HDG.class) {
			return NME_TO_TTL_REF.class;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	* @see librisuite.hibernate.Descriptor#getSortFormParameters()
	*/
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(
			100,
			101,
			getTypeCode(),
			getSubTypeCode(),
			0);
	} /**
			 * Getter for subTypeCode
			 * 
			 * @return subTypeCode
			 */
	public short getSubTypeCode() {
		return subTypeCode;
	} /**
			 * Getter for typeCode
			 * 
			 * @return typeCode
			 */
	public short getTypeCode() {
		return typeCode;
	} /**
			 * Setter for copySubjectIndicator
			 * 
			 * @param c copySubjectIndicator
			 */
	public void setCopyToSubjectIndicator(char c) {
		copyToSubjectIndicator = c;
	} /* (non-Javadoc)
			 * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
			 */
	public void setCorrelationValues(CorrelationValues v) {
		typeCode = v.getValue(1);
		subTypeCode = v.getValue(2);
	} /**
			 * Setter for indexingLanguage
			 * 
			 * @param s indexingLanguage
			 */
	public void setIndexingLanguage(short s) {
		indexingLanguage = s;
	} /**
			 * Setter for subTypeCode
			 * 
			 * @param s subTypeCode
			 */
	public void setSubTypeCode(short s) {
		subTypeCode = s;
	} /**
			 * Setter for typeCode
			 * 
			 * @param s typeCode
			 */
	public void setTypeCode(short s) {
		typeCode = s;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#changeAffectsCacheTable()
	 */
	public boolean changeAffectsCacheTable() {
		return true;
	}
	public String getLockingEntityType() {
		return "NH";
	}
	
	public void setDefaultTypeAndFunction(){
		int typCode= new Integer(configHandler.findValue("t_nme_typ_and_sub_typ","name.typeCode"));
		int type = configHandler.isParamOfGlobalVariable("t_nme_typ_and_sub_typ") ? this.getType(typCode) : typCode;
		
		setTypeCode((short)type);
		int funCode= new Integer(configHandler.findValue("t_nme_typ_and_sub_typ","name.subTypeCode"));
		int function= this.getFunction(funCode);
		setSubTypeCode((short)function);
	}
	
	@Override
	public void setDefaultsFromWorksheet(Tag currentTag) {
			if (currentTag instanceof Browsable) {
				Descriptor d = ((Browsable) currentTag).getDescriptor();
				if (d.getClass().equals(this.getClass())) {
					setCorrelationValues(d.getCorrelationValues());
					setAuthoritySourceCode(d.getAuthoritySourceCode());
					setSkipInFiling(d.getSkipInFiling());
				}
				else if (d instanceof NME_TTL_HDG) {
			        setCorrelationValues(d.getCorrelationValues());
					setAuthoritySourceCode(d.getAuthoritySourceCode());
					setSkipInFiling(d.getSkipInFiling());

				}
			}
	  }
				

}
