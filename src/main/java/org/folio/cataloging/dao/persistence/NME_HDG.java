package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.ConfigHandler;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAONameDescriptor;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * Hibernate class for table NME_HDG
 * @author paulm
 * @since 1.0
 */
public class NME_HDG extends Descriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private char copyToSubjectIndicator;
	private int indexingLanguage;
	private int subTypeCode;
	private int typeCode;
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
		setDefaultTypeAndFunction();
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
	public int getCategory() {
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
	public AbstractDAO getDAO() {
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
	public int getIndexingLanguage() {
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
	public int getSubTypeCode() {
		return subTypeCode;
	}
	public int getTypeCode() {
		return typeCode;
	}

	public void setCorrelationValues(CorrelationValues v) {
		typeCode = v.getValue(1);
		subTypeCode = v.getValue(2);
	}
	public void setIndexingLanguage(short s) {
		indexingLanguage = s;
	}

	public void setSubTypeCode(short s) {
		subTypeCode = s;
	}

	public void setTypeCode(short s) {
		typeCode = s;
	}

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
