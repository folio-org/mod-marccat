package org.folio.cataloging.bean.crossreference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.business.codetable.CodeTableTranslationLanguage;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DeleteCrossReferenceException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.crossreference.CrossReferenceSummaryElement;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.exception.IncompatibleCrossReferenceHeadingsException;
import org.folio.cataloging.exception.NoHeadingSetException;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceAttrTyp;
import org.folio.cataloging.dao.persistence.ReferenceThsTyp;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.dao.persistence.THS_ATRIB;
import org.folio.cataloging.dao.persistence.THS_REF;
import org.folio.cataloging.dao.persistence.T_DUAL_REF;
import org.folio.cataloging.dao.persistence.T_REF_PRNT_CNSTN;

import org.folio.cataloging.util.StringText;
import org.folio.cataloging.business.controller.SessionUtils;

@SuppressWarnings("unchecked")
public class CrossReferenceBean extends LibrisuiteBean 
{
	private Locale locale;
	private Descriptor sourceDescriptor;
	private Descriptor targetDescriptor;
	private short referenceType;
	private short dualReference;
	private List crossReferenceList = new ArrayList();
	private boolean isThesaurus=false;
	private boolean attribute=false;

	public static CrossReferenceBean getInstance(HttpServletRequest request) 
	{
		CrossReferenceBean bean = (CrossReferenceBean) getSessionAttribute(request, CrossReferenceBean.class);
		if (bean == null) {
			bean = new CrossReferenceBean();
			bean.setSessionAttribute(request, bean.getClass());
		}
		bean.setAuthorisationAgent(SessionUtils.getUserProfile(request.getSession(false)).getAuthorisationAgent());
		bean.setLocale(SessionUtils.getCurrentLocale(request));
		return bean;
	}
	
	public List getSummary(Descriptor d, int cataloguingView, Locale locale, boolean isAttribute) throws DataAccessException 
	{
		List xRefs = null;	
		if(isAttribute)
			xRefs=((DAODescriptor) d.getDAO()).getCrossReferencesAttrib(d);
		else
			xRefs=((DAODescriptor) d.getDAO()).getCrossReferences(d);
		
	    if(isAttribute)
		   return getSummaryFromAttrib (xRefs, locale, cataloguingView);
		else if(isThesaurus)
			return getSummaryFromThesaurus(xRefs, locale, cataloguingView);
		else
			return getSummary(xRefs, cataloguingView, locale);
	}

	public Descriptor getSeeReference(Descriptor d, int cataloguingView) throws DataAccessException 
	{
		return ((DAODescriptor) d.getDAO()).getSeeReference(d, cataloguingView);
	}

	/**
	 * Builds a List of CrossReferenceSummaryElements suitable for use
	 * in the presentation of cross references
	 * 
	 * @param locale - the current local (for decoding codes)
	 * @return the list
	 */
	public List getSummary(List xRefs, int cataloguingView, Locale locale) throws DataAccessException 
	{
		List result = new ArrayList();
		DAOCodeTable cts = new DAOCodeTable();
		setThesaurus(false);
		Iterator iter = xRefs.iterator();
	
		while (iter.hasNext()) 
		{
			REF ref = (REF) iter.next();
			Descriptor hdg = (Descriptor) ref.getTargetDAO().load(ref.getTarget(), cataloguingView);
			CrossReferenceSummaryElement aSummaryElement =	new CrossReferenceSummaryElement();
		
			if(ref.getType() != null)
				aSummaryElement.setDecodedType(cts.load(ReferenceType.class, ref.getType(), locale).getLongText());
			
			if(ref.getPrintConstant() != null)
				aSummaryElement.setDecodedPrintConstant(cts.load(T_REF_PRNT_CNSTN.class,ref.getPrintConstant(),locale).getShortText());
		
			if(hdg.getStringText() != null)
				aSummaryElement.setTarget(new StringText(hdg.getStringText()).toDisplayString());
		
			aSummaryElement.setXRef(ref);
			aSummaryElement.setDocTargetCounts(new Integer(((DAODescriptor) hdg.getDAO()).getDocCount(hdg,	cataloguingView)));
			aSummaryElement.setTargetDescriptor(hdg);
			
			result.add(aSummaryElement);
		}
		compressDualReferences(result, locale);
		return result;
	}
	
	/**
	 * Builds a List of s suitable for use
	 * in the presentation of cross references from Thesaurus
	 * 
	 * @param locale - the current local (for decoding codes)
	 * @return the list
	 */
	public List getSummaryFromThesaurus(List xRefs, int cataloguingView, Locale locale) throws DataAccessException 
	{
		//setThesaurus(true);
		List result = new ArrayList();
		DAOCodeTable cts = new DAOCodeTable();

		Iterator iter = xRefs.iterator();
		while (iter.hasNext()) 
		{
			REF ref = (REF) iter.next();
			Descriptor hdg = (Descriptor) ref.getTargetDAO().load(ref.getTarget(), cataloguingView);
			CrossReferenceSummaryElement aSummaryElement = new CrossReferenceSummaryElement();
			aSummaryElement.setDecodedType(cts.load(ReferenceThsTyp.class, ref.getType(), locale).getLongText());			
			aSummaryElement.setDecodedPrintConstant(cts.load(T_REF_PRNT_CNSTN.class,ref.getPrintConstant(),locale).getShortText());
			aSummaryElement.setTarget(new StringText(hdg.getStringText()).toDisplayString());
			aSummaryElement.setXRef(ref);
			aSummaryElement.setDocTargetCounts(new Integer(((DAODescriptor) hdg.getDAO()).getDocCount(hdg,	cataloguingView)));
			aSummaryElement.setTargetDescriptor(hdg);
			
			result.add(aSummaryElement);
		}
		compressDualReferences(result, locale);
		return result;
	}
	
	/**
	 * Builds a List of CrossReferenceSummaryElements suitable for use
	 * in the presentation of cross references from Thesaurus
	 * 
	 * @param locale - the current local (for decoding codes)
	 * @return the list
	 */
	public List getSummaryFromThesaurus(List xRefs, Locale locale, int cataloguingView) throws DataAccessException 
	{
		//setThesaurus(true);
		List result = new ArrayList();
		DAOCodeTable cts = new DAOCodeTable();
	
		Iterator iter = xRefs.iterator();
		while (iter.hasNext()) 
		{
			REF ref = (REF) iter.next();
			Descriptor hdg = (Descriptor) ref.getTargetDAO().load(ref.getTarget());
			
			CrossReferenceSummaryElement aSummaryElement = new CrossReferenceSummaryElement();
			aSummaryElement.setDecodedType(cts.load(ReferenceThsTyp.class, ref.getType(), locale).getLongText());
			aSummaryElement.setDecodedPrintConstant(cts.load(T_REF_PRNT_CNSTN.class, ref.getPrintConstant(), locale).getShortText());
			aSummaryElement.setTarget(new StringText(hdg.getStringText()).toDisplayString());
			aSummaryElement.setXRef(ref);
			aSummaryElement.setDocTargetCounts(new Integer(((DAODescriptor) hdg.getDAO()).getDocCount(hdg,	cataloguingView)));
			aSummaryElement.setTargetDescriptor(hdg);
			
			result.add(aSummaryElement);
		}
		compressDualReferences(result, locale);
		return result;
	}
	
	
	/**
	 * Builds a List of CrossReferenceSummaryElements suitable for use
	 * in the presentation of cross references from Thesaurus -- Attribute
	 * 
	 * @param locale - the current local (for decoding codes)
	 * @return the list
	 */
	public List getSummaryFromAttrib(List xRefs, int cataloguingView, Locale locale) throws DataAccessException 
	{
		//setThesaurus(true);
		List result = new ArrayList();
		DAOCodeTable cts = new DAOCodeTable();
	
		Iterator iter = xRefs.iterator();
		while (iter.hasNext()) 
		{
			THS_ATRIB ref = (THS_ATRIB) iter.next();
			Descriptor hdg = (Descriptor) ref.getTargetDAO().load(ref.getTarget(), cataloguingView);
			CrossReferenceSummaryElement aSummaryElement = new CrossReferenceSummaryElement();
			aSummaryElement.setDecodedType(cts.load(ReferenceAttrTyp.class, ref.getType(), locale).getLongText());
			aSummaryElement.setDecodedPrintConstant(cts.load(T_REF_PRNT_CNSTN.class, ref.getPrintConstant(), locale).getShortText());
			aSummaryElement.setTarget(new StringText(hdg.getStringText()).toDisplayString());
			aSummaryElement.setXRef(ref);
			aSummaryElement.setDocTargetCounts(new Integer(((DAODescriptor) hdg.getDAO()).getDocCount(hdg,	cataloguingView)));
			aSummaryElement.setTargetDescriptor(hdg);
			
			result.add(aSummaryElement);
		}
		
		compressDualReferences(result, locale);
		return result;
	}
	
	/**
	 * Builds a List of CrossReferenceSummaryElements suitable for use
	 * in the presentation of cross references from Thesaurus -- Attribute
	 * 
	 * @param locale - the current local (for decoding codes)
	 * @return the list
	 */
	public List getSummaryFromAttrib(List xRefs, Locale locale, int cataloguingView) throws DataAccessException 
	{
		//setThesaurus(true);
		List result = new ArrayList();
		DAOCodeTable cts = new DAOCodeTable();
	
		Iterator iter = xRefs.iterator();
		while (iter.hasNext()) 
		{
			THS_ATRIB ref = (THS_ATRIB) iter.next();
			Descriptor hdg = (Descriptor) ref.getTargetDAO().load(ref.getTarget());
			
			CrossReferenceSummaryElement aSummaryElement =	new CrossReferenceSummaryElement();
			aSummaryElement.setDecodedType(cts.load(ReferenceAttrTyp.class, ref.getType(), locale).getLongText());
			aSummaryElement.setDecodedPrintConstant(cts.load(T_REF_PRNT_CNSTN.class, ref.getPrintConstant(), locale).getShortText());
			aSummaryElement.setTarget(new StringText(hdg.getStringText()).toDisplayString());
			aSummaryElement.setXRef(ref);
			aSummaryElement.setDocTargetCounts(new Integer(((DAODescriptor) hdg.getDAO()).getDocCount(hdg,	cataloguingView)));
			aSummaryElement.setTargetDescriptor(hdg);
			
			result.add(aSummaryElement);
		}
		compressDualReferences(result, locale);
		return result;
	}
	
	/**
	 * Converts adjacent see also/see also from references to the same heading
	 * into a single "Dual" reference (for display purposes only)
	 * 
	 * @param xRefs - a List of CrossReferenceSummaryElements
	 */
	private void compressDualReferences(List xRefs, Locale locale) throws DataAccessException 
	{
		CrossReferenceSummaryElement prevRef = null;
		Iterator iter = xRefs.iterator();

		while (iter.hasNext()) 
		{
			CrossReferenceSummaryElement aRef = (CrossReferenceSummaryElement) iter.next();
			if (prevRef != null) {
				if (prevRef.getXRef().getTarget() == aRef.getXRef().getTarget()) {
					prevRef.setDecodedType(new CodeTableTranslationLanguage((short) 672).decode(locale));
					iter.remove();
				}
			}
			prevRef = aRef;
		}
	}

	public List add(Descriptor source, Descriptor target, short referenceType, short dualReference, int cataloguingView, boolean isAttribute) throws DataAccessException 
	{
		List l = new ArrayList();
		REF ref = REF.add(source, target, referenceType, cataloguingView,isAttribute);
		l.add(ref);

		if (ReferenceType.isSeeAlso(referenceType) || ReferenceType.isSeeAlsoFrom(referenceType)) {
			if (T_DUAL_REF.isDual(dualReference)) {
				ref = REF.add(target, source, referenceType, cataloguingView,isAttribute);
				l.add(ref);
			}
		}
		return l;
	}

	public void delete(REF ref) throws DeleteCrossReferenceException 
	{
		try {
			ref.getDAO().delete(ref);
		} catch (DataAccessException e) {
			throw new DeleteCrossReferenceException();
		}
	}

	public void deleteByIndex(int index) throws DeleteCrossReferenceException 
	{
		CrossReferenceSummaryElement anElem = (CrossReferenceSummaryElement) getCrossReferenceList().get(index);
		getCrossReferenceList().remove(index);
		delete(anElem.getXRef());
	}

	public boolean isAttribute() {
		return attribute;
	}

	public void setAttribute(boolean attribute) {
		this.attribute = attribute;
	}

	public void setThesaurus(boolean isThesaurus) {
		this.isThesaurus = isThesaurus;
	}
	
	public List getCrossReferenceList() {
		return crossReferenceList;
	}

	public Descriptor getSourceDescriptor() {
		return sourceDescriptor;
	}

	public void setCrossReferenceList(List list) {
		crossReferenceList = list;
	}

	public void setSourceDescriptor(Descriptor descriptor,boolean isAttribute) throws DataAccessException 
	{
		sourceDescriptor = descriptor;
		setCrossReferenceList(
			getSummary(
				getSourceDescriptor(),
				View.toIntView(getSourceDescriptor().getUserViewString()),
				getLocale(),isAttribute));
	}

	public Descriptor getTargetDescriptor() {
		return targetDescriptor;
	}

	public void setTargetDescriptor(Descriptor descriptor) throws IncompatibleCrossReferenceHeadingsException 
	{
		/*if (getSourceDescriptor().getReferenceClass(descriptor.getClass())
			== null) {
			throw new IncompatibleCrossReferenceHeadingsException();
		}*/
		targetDescriptor = descriptor;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public short getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(short s) {
		referenceType = s;
	}
	
   //TODO non sono sicura di questa modifica da chiedea
	public boolean isDual() 
	{
		if(isThesaurus || isAttribute())
			return false;
		else
			return ReferenceType.isSeeAlso(getReferenceType()) || ReferenceType.isSeeAlsoFrom(getReferenceType());
	}

	public void saveNewReference(int cataloguingView, boolean isAttribute) throws DataAccessException, NoHeadingSetException 
	{
		// add the new cross reference(s)
		if(getTargetDescriptor()==null)
			throw new NoHeadingSetException();
		add(
			getSourceDescriptor(),
			getTargetDescriptor(),
			getReferenceType(),
			getDualReference(),
			cataloguingView,isAttribute);
		// replace the summary list
		setCrossReferenceList(getSummary(getSourceDescriptor(), cataloguingView, getLocale(),isAttribute));
	}		

	public short getDualReference() {
		return dualReference;
	}

	public void setDualReference(short s) {
		dualReference = s;
	}

	public List getReferenceTypeList() 
	{
		 if(isAttribute())
			return CodeListsBean.getReferenceAttrType().getCodeList(getLocale());
		 else if(isThesaurus)
			return CodeListsBean.getReferenceThsType().getCodeList(getLocale());
		 else
		   return CodeListsBean.getCrossReferenceType().getCodeList(getLocale());
	}
	
	public List getReferenceThsTypeList() {
		return CodeListsBean.getReferenceThsType().getCodeList(getLocale());
	}

	public List getDualReferenceList() {
		return CodeListsBean.getDualReference().getCodeList(getLocale());
	}

	public int getNumberOfReferences() {
		return getCrossReferenceList().size();
	}
	
	public boolean isCanEditHeadings(){
		return getAuthorisationAgent().isPermitted("editHeadings");
	}
	
	public boolean isThesaurusRef(List xRefs) 
	{
		Iterator iter = xRefs.iterator();
		boolean isThesaurus = false;
		while (iter.hasNext()) 
		{
			REF ref = (REF) iter.next();
			if (ref instanceof THS_REF) {
				isThesaurus = true;
				break;
			} else
				isThesaurus = false;
		}
		return isThesaurus;
	}
}