/*
 * (c) LibriCore
 * 
 * Created on Nov 22, 2005
 * 
 * AuthorityReferenceTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.DescriptorFactory;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOAuthorityCorrelation;
import org.folio.cataloging.dao.DAOAuthorityReferenceTag;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.NoHeadingSetException;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Represents Authority tags 4XX and 5XX.
 * 
 * Note that Persistence is implemented via the associated REF attribute
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public abstract class AuthorityReferenceTag
	extends VariableField
	implements PersistsViaItem, PersistentObjectWithView, Browsable, SkipInFiling {
	private static final Log logger =
		LogFactory.getLog(AuthorityReferenceTag.class);

	private static final String VARIANT_CODES = "wehij4";
	
	private AUT autItm;

	private REF reference;

	private Integer refTypeCorrelationPosition;
	private Descriptor targetDescriptor;
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityReferenceTag() {
		super();
		reference = new NME_REF();
		targetDescriptor = new NME_HDG();
	}
	@Override
	public String buildBrowseTerm() {
		return getDescriptor().buildBrowseTerm();
	}


	/**
	 * Used to change the associated targetDescriptor type
	 * 
	 * @since 1.0
	 */
	public void changeHeadingType(short headingType) {
		Descriptor d = DescriptorFactory.createDescriptor(headingType);
		setTargetDescriptor(d);
	}

	/* (non-Javadoc)
	 * @see TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return !v.isValueDefined(getRefTypeCorrelationPosition());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			AuthorityReferenceTag aRef = (AuthorityReferenceTag) obj;
			return aRef.getReference().equals(this.getReference());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Tag#evict()
	 */
	public void evict() throws DataAccessException {
		reference.evict();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		reference.evict(((AuthorityReferenceTag) obj).getReference());
	}

	/* (non-Javadoc)
	 * @see TagInterface#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// called on save for new tags -- make sure source heading is in reference
		getReference().setSource(getAutItm().getHeadingNumber());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getAuthorityStructure() {
		return reference.getAuthorityStructure();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public AUT getAutItm() {
		return autItm;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCategory()
	 */
	public int getCategory() {
		try {
			return (
				(AccessPoint) getDescriptor()
					.getAccessPointClass()
					.newInstance())
				.getCategory();
		} catch (InstantiationException e) {
			throw new RuntimeException("Could not create an AccessPoint");
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not create an AccessPoint");
		}
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		CorrelationValues c = getTargetDescriptor().getCorrelationValues();
		setRefTypeCorrelationPosition(c.getFirstUnusedPosition());
		c = c.change(getRefTypeCorrelationPosition(), getReference().getType());
		/*
		 * subject correlation in bib uses pos 3 for source but authorities
		 * does not use this value so set to undefined
		 */
		for (int i = getRefTypeCorrelationPosition() + 1; i <= 3; i++) {
			c = c.change(i, CorrelationValues.UNDEFINED);
		}
		return c;
	}

	/* (non-Javadoc)
	 * @see Tag#getDAO()
	 */
	public AbstractDAO getDAO() {
		return new DAOAuthorityReferenceTag();
	}

	/* (non-Javadoc)
	 * @see Browsable#getDescriptor()
	 */
	public Descriptor getDescriptor() {
		return getTargetDescriptor();
	}

	/* (non-Javadoc)
	 * @see TagInterface#getDisplayCategory()
	 */
	public int getDisplayCategory() {
		return 16;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getDisplaysHeadingType()
	 */
	public boolean getDisplaysHeadingType() {
		return true;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public short getDualReferenceIndicator() {
		return T_DUAL_REF.NO;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getEarlierRules() {
		return reference.getEarlierRules();
	}

	/* (non-Javadoc)
	 * @see Browsable#getEditableSubfields()
	 */
	public StringText getEditableSubfields() {
		return new StringText(getReference().getStringText());
	}

	/* (non-Javadoc)
	 * @see TagInterface#getFirstCorrelationList()
	 */
	@Deprecated
	public List getFirstCorrelationList() throws DataAccessException {
		/* if (getRefTypeCorrelationPosition() > 1) {
			if (getTargetDescriptor() instanceof NME_TTL_HDG) {
				return getDaoCodeTable().getList(NameType.class,false);
			} else {
				try {
					return (
						(AccessPoint) getTargetDescriptor()
							.getAccessPointClass()
							.newInstance())
						.getFirstCorrelationList();
				} catch (InstantiationException e) {
					throw new RuntimeException("ErrorCollection creating AccessPoint");
				} catch (IllegalAccessException e) {
					throw new RuntimeException("ErrorCollection creating AccessPoint");
				}
			}
		} else {
			return new DAOAuthorityCorrelation().getValidReferenceTypeList(this);
		} */

		return null;
	}

	/* (non-Javadoc)
	 * @see Browsable#getHeadingNumber()
	 */
	public Integer getHeadingNumber() {
		int result = getAutItm().getHeadingNumber();
		if (result > 0) {
			return new Integer(result);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return autItm;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getMarcEncoding()
	 */
	public CorrelationKey getMarcEncoding()
		throws DataAccessException {
		return super.getMarcEncoding().changeSkipInFilingIndicator(
			getSkipInFiling());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getNoteGeneration() {
		return reference.getNoteGeneration();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getPrintConstant() {
		return reference.getPrintConstant();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public REF getReference() {
		return reference;
	}

	/*
	 * The position of the referenceType correlation value varies by the type of
	 * heading (3 for names, 1 for titles, etc.).  This attribute is set during the
	 * call to getCorrelationValues (and should stay the same for any given instance).
	 * @since 1.0
	 */
	protected int getRefTypeCorrelationPosition() {
		if (refTypeCorrelationPosition == null) {
			// set the value  by calling getCorrelationValues()
			getCorrelationValues();
		}
		return refTypeCorrelationPosition.intValue();
	}

	/* (non-Javadoc)
	 * @see TagInterface#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(int value1)
		throws DataAccessException {
		if (getRefTypeCorrelationPosition() > 2) {
			if (getTargetDescriptor() instanceof NME_TTL_HDG) {
				return new NameAccessPoint().getSecondCorrelationList(value1);
			} else {
				try {
					return (
						(AccessPoint) getTargetDescriptor()
							.getAccessPointClass()
							.newInstance())
							.getSecondCorrelationList(
						value1);
				} catch (InstantiationException e) {
					throw new RuntimeException("ErrorCollection creating AccessPoint");
				} catch (IllegalAccessException e) {
					throw new RuntimeException("ErrorCollection creating AccessPoint");
				}
			}
		} else if (getRefTypeCorrelationPosition() == 2) {
			return new DAOAuthorityCorrelation().getValidReferenceTypeList(this);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see SkipInFiling#getSkipInFiling()
	 */
	public int getSkipInFiling() {
		if (getDescriptor() instanceof SkipInFiling) {
			return getDescriptor().getSkipInFiling();
		} else {
			return 0;
		}
	}

	@Override
	public StringText getStringText() {
			StringText result = new StringText(getTargetDescriptor().getStringText());
			StringText variantCodes = new StringText(getReference().getStringText());
			StringText subi = variantCodes.getSubfieldsWithCodes("i");
			if (subi.getNumberOfSubfields() == 1) {
				result.addSubfield(0, subi.getSubfield(0));
			}
			result.add(variantCodes.getSubfieldsWithCodes("ej"));
			return result;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Descriptor getTargetDescriptor() {
		return targetDescriptor;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(int value1, int value2)
		throws DataAccessException {
		logger.debug("getThirdCorrelationList("+value1+", " + value2 + ")");
		if (getRefTypeCorrelationPosition() == 3) {
			logger.debug("refType is in pos 3");
			return new DAOAuthorityCorrelation().getValidReferenceTypeList(this);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see Tag#getUpdateStatus()
	 */
	public int getUpdateStatus() {
		return reference.getUpdateStatus();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getUserViewString() {
		return getReference().getUserViewString();
	}

	/* (non-Javadoc)
	 * @see Browsable#getValidEditableSubfields()
	 */
	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}

	public String getVariantCodes() {
			return VARIANT_CODES;
		}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getReference().hashCode();
	}

	/* (non-Javadoc)
	 * @see TagInterface#isBrowsable()
	 */
	public boolean isBrowsable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see Tag#isChanged()
	 */
	public boolean isChanged() {
		return reference.isChanged();
	}

	/* (non-Javadoc)
	 * @see Tag#isDeleted()
	 */
	public boolean isDeleted() {
		return reference.isDeleted();
	}

	public boolean isHasDualIndicator() {
		return false; // default implementation
	}

	/* (non-Javadoc)
	 * @see Tag#isNew()
	 */
	public boolean isNew() {
		return reference.isNew();
	}

	/* (non-Javadoc)
	 * @see Tag#isRemoved()
	 */
	public boolean isRemoved() {
		return reference.isRemoved();
	}

	@Override
	public boolean isWorksheetEditable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see Tag#markChanged()
	 */
	public void markChanged() {
		reference.markChanged();
	}

	/* (non-Javadoc)
	 * @see Tag#markDeleted()
	 */
	public void markDeleted() {
		reference.markDeleted();
	}

	/* (non-Javadoc)
	 * @see Tag#markNew()
	 */
	public void markNew() {
		reference.markNew();
	}

	/* (non-Javadoc)
	 * @see Tag#markUnchanged()
	 */
	public void markUnchanged() {
		reference.markUnchanged();
	}

	/* (non-Javadoc)
	 * @see Tag#onDelete(net.sf.hibernate.Session)
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return reference.onDelete(arg0);
	}

	/* (non-Javadoc)
	 * @see Tag#onLoad(net.sf.hibernate.Session, java.io.Serializable)
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		reference.onLoad(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see Tag#onSave(net.sf.hibernate.Session)
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return reference.onSave(arg0);
	}

	/* (non-Javadoc)
	 * @see Tag#onUpdate(net.sf.hibernate.Session)
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return reference.onUpdate(arg0);
	}

	/* (non-Javadoc)
	 * @see VariableField#parseModelXmlElementContent(org.w3c.dom.Element)
	 */
	public void parseModelXmlElementContent(Element xmlElement) {
		setDescriptorStringText(StringText.parseModelXmlElementContent(xmlElement));
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthorityStructure(char b) {
		reference.setAuthorityStructure(b);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAutItm(AUT aut) {
		autItm = aut;
	}

	/* (non-Javadoc)
	 * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		getReference().setType(v.getValue(getRefTypeCorrelationPosition()));
		getTargetDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see Browsable#setDescriptor(librisuite.hibernate.Descriptor)
	 */
	public void setDescriptor(Descriptor d) {
		setTargetDescriptor(d);
	}

	/* (non-Javadoc)
	 * @see Browsable#setDescriptorStringText(org.folio.cataloging.util.StringText)
	 */
	public void setDescriptorStringText(StringText tagStringText) {
		getTargetDescriptor().setStringText(
			tagStringText.getSubfieldsWithoutCodes("w").toString());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setEarlierRules(char b) {
		reference.setEarlierRules(b);
	}

	/* (non-Javadoc)
	 * @see Browsable#setHeadingNumber(java.lang.Integer)
	 */
	public void setHeadingNumber(Integer i) {
		int setting;
		if (i == null) {
			setting = -1;
		} else {
			setting = i.intValue();
		}
		getReference().setTarget(setting);
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setAutItm((AUT) item);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNoteGeneration(char b) {
		reference.setNoteGeneration(b);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPrintConstant(char b) {
		reference.setPrintConstant(b);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReference(REF ref) {
		reference = ref;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRefTypeCorrelationPosition(Integer i) {
		refTypeCorrelationPosition = i;
	}

	/* (non-Javadoc)
	 * @see SkipInFiling#setSkipInFiling(short)
	 */
	public void setSkipInFiling(int i) {
		if (getDescriptor() instanceof SkipInFiling) {
			getDescriptor().setSkipInFiling(i);
		}
	}

	/* (non-Javadoc)
	 * @see VariableField#setStringText(org.folio.cataloging.util.StringText)
	 */
	public void setStringText(StringText stringText) {
		//paulm aut
		getReference().setStringText(stringText.getSubfieldsWithCodes(getVariantCodes()).toString());
		logger.debug("REF.StringText = " + getReference().getStringText());
		// no setting of descriptor from worksheet
		// setDescriptorStringText(stringText);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTargetDescriptor(Descriptor descriptor) {
		targetDescriptor = descriptor;
		/*
		 * make sure that the new descriptor is compatible with the reference class
		 */
		reference.setTarget(descriptor.getKey().getHeadingNumber());
	}

	/* (non-Javadoc)
	 * @see Tag#setUpdateStatus(int)
	 */
	public void setUpdateStatus(int i) {
		reference.setUpdateStatus(i);
	}

	public void setLinkDisplay(Character linkDisplay) {
		reference.setLinkDisplay(linkDisplay);
	}
	
	public Character getLinkDisplay() {
		return reference.getLinkDisplay();
	}

	public void setReplacementComplexity(Character replacementComplexity) {
		reference.setReplacementComplexity(replacementComplexity);
	}

	public Character getReplacementComplexity() {
		return reference.getReplacementComplexity();
	}
	/**
	 * 
	 * @since 1.0
	 */
	public void setUserViewString(String s) {
		getReference().setUserViewString(s);
	}
	
	 /* (non-Javadoc)
	 * @see TagInterface#validate()
	 */
	public void validate(int index) throws NoHeadingSetException {
		if (getTargetDescriptor().isNew()) {
			throw new NoHeadingSetException(index);
		}
	}


}
