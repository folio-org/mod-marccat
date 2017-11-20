package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import org.folio.cataloging.business.common.CrossReferenceExistsException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.DAOCrossReferences;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.business.descriptor.Descriptor;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * abstract class for all cross-reference tables (including NME_NME_TTL_REF
 * and TTL_NME_TTL_REF)
 * @author paulm
 * @version $Revision: 1.10 $, $Date: 2006/01/19 12:31:26 $
 * @since 1.0
 */
public abstract class REF extends PersistenceState implements Serializable, Cloneable, PersistentObjectWithView 
{
	private static final DAOCrossReferences theDAO = new DAOCrossReferences();
	private REF_KEY key = new REF_KEY();
	private Character printConstant;
	private Character noteGeneration;
	private Character formerHeading;
	private Character authorityStructure;
	private Character earlierRules;
	private Character linkDisplay;
	private Character replacementComplexity;
	private Character verificationLevel;
	private String stringText;
	private final PersistenceState persistenceState = new PersistenceState();
	abstract public DAODescriptor getTargetDAO();
	
	public static REF add(Descriptor source,Descriptor target,short referenceType,int cataloguingView,boolean isAttribute) throws DataAccessException 
	{
		/* instantiate the appropriate REF type and populate key from arguments */
		REF ref = REF.newInstance(source, target, referenceType, cataloguingView,isAttribute);
		DAOCrossReferences dao = (DAOCrossReferences) ref.getDAO();
		/* verify that this xref doesn't already exist in the database */
		if (dao.load(source, target, referenceType, cataloguingView) != null) {
			throw new CrossReferenceExistsException();
		}
		dao.save(ref);
		return ref;
	}
	
	static public REF newInstance(Descriptor source,Descriptor target,short referenceType,int cataloguingView, boolean isAttribute) 
	{
		REF ref = null;
		try {
			if(isAttribute)
				ref = new THS_ATRIB();
			else
				ref =(REF) source.getReferenceClass(target.getClass()).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("error creating cross-reference object");
		}
		ref.init(source, target, referenceType, cataloguingView);
		return ref;
	}

	public REF() 
	{
		setDefault();
	}

	public void init(Descriptor source,Descriptor target,short referenceType,int cataloguingView) 
	{
		setKey(new REF_KEY());
		getKey().setSource(source.getKey().getHeadingNumber());
		getKey().setTarget(target.getKey().getHeadingNumber());
		getKey().setType(referenceType);
		getKey().setUserViewString(View.makeSingleViewString(cataloguingView));
		setDefault();
	}
	
	public void setDefault() 
	{
		this.setAuthorityStructure('a');
		this.setEarlierRules('x');
		this.setFormerHeading('x');
		if (getKey() != null
			&& ReferenceType.isEquivalence(getKey().getType())) {
			this.setNoteGeneration('x');
		} else {
			this.setNoteGeneration('@');
		}
		this.setPrintConstant(Defaults.getChar("authority.reference.specialRelationship"));
		this.setLinkDisplay('n');
		this.setReplacementComplexity('n');
		this.setVerificationLevel('1');
	}
	
	public Object clone() {
		try {
			REF_KEY newKey = (REF_KEY) getKey().clone();
			REF result = (REF) super.clone();
			result.setKey(newKey);
			return result;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public REF createReciprocal() 
	{
		REF result = (REF) this.clone();
		result.setSource(getTarget());
		result.setTarget(getSource());
		result.setType(ReferenceType.getReciprocal(result.getType()));
		return result;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj.getClass().equals(this.getClass())) {
			return this.getKey().equals(((REF) obj).getKey());
		}
		return false;
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}
	public void generateNewKey() throws DataAccessException {
		return;
	}
	public Character getAuthorityStructure() {
		return authorityStructure;
	}
	public HibernateUtil getDAO() {
		return theDAO;
	}
	public Character getEarlierRules() {
		return earlierRules;
	}
	public Character getFormerHeading() {
		return formerHeading;
	}
	private REF_KEY getKey() {
		/* getKey is made private so that implementers are required to provide individual
		 * delegate methods.  This so that both NME_REF and NME_NME_TTL_REF can behave 
		 * polymorphically
		 */
		return key;
	}
	public Character getNoteGeneration() {
		return noteGeneration;
	}
	public Character getPrintConstant() {
		return printConstant;
	}
	public int getSource() {
		return key.getSource();
	}
	public String getStringText() {
		return stringText;
	}
	public int getTarget() {
		return key.getTarget();
	}
	public Short getType() {
		return key.getType();
	}
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}
	public String getUserViewString() {
		return getKey().getUserViewString();
	}
	public Character getVerificationLevel() {
		return verificationLevel;
	}
	public int hashCode() {
		return getKey().hashCode();
	}
	public boolean isChanged() {
		return persistenceState.isChanged();
	}
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}
	public boolean isNew() {
		return persistenceState.isNew();
	}
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}
	public void markChanged() {
		persistenceState.markChanged();
	}
	public void markDeleted() {
		persistenceState.markDeleted();
	}
	public void markNew() {
		persistenceState.markNew();
	}
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}
	public boolean onDelete(Session s) throws CallbackException {
		return persistenceState.onDelete(s);
	}
	public void onLoad(Session s, Serializable id) {
		persistenceState.onLoad(s, id);
	}
	public boolean onSave(Session s) throws CallbackException {
		return persistenceState.onSave(s);
	}
	public boolean onUpdate(Session s) throws CallbackException {
		return persistenceState.onUpdate(s);
	}
	public void setAuthorityStructure(Character b) {
		authorityStructure = b;
	}
	public void setEarlierRules(Character b) {
		earlierRules = b;
	}
	public void setFormerHeading(Character b) {
		formerHeading = b;
	}
	private void setKey(REF_KEY nme_ref_key) {
		key = nme_ref_key;
	}
	public void setNoteGeneration(Character b) {
		noteGeneration = b;
	}
	public void setPrintConstant(Character b) {
		printConstant = b;
	}
	public void setSource(int i) {
		key.setSource(i);
	}
	public void setStringText(String stringText) {
		this.stringText = stringText;
	}
	public void setTarget(int i) {
		key.setTarget(i);
	}
	public void setType(Short s) {
		key.setType(s);
	}
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}
	public void setUserViewString(String s) {
		getKey().setUserViewString(s);
	}
	public void setVerificationLevel(Character b) {
		verificationLevel = b;
	}
	public Character getLinkDisplay() {
		return linkDisplay;
	}
	public void setLinkDisplay(Character linkDisplay) {
		this.linkDisplay = linkDisplay;
	}
	public Character getReplacementComplexity() {
		return replacementComplexity;
	}
	public void setReplacementComplexity(Character replacementComplexity) {
		this.replacementComplexity = replacementComplexity;
	}
}