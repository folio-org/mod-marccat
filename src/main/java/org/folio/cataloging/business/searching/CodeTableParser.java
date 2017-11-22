package org.folio.cataloging.business.searching;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.GeographicSubdivisionType;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.SpecialSubdivisionType;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.TimeSubdivisionType;
import org.folio.cataloging.business.cataloguing.bibliographic.BIB_ITM;
import org.folio.cataloging.dao.DAOBibItem;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.dao.DAOBibliographicCorrelation;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.persistence.CasCache;
import org.folio.cataloging.dao.persistence.LabelTagDisplay;
import org.folio.cataloging.dao.persistence.RdaMarcTagDisplay;
import org.folio.cataloging.dao.persistence.T_CAS_SDIV_GEOG_TYP;
import org.folio.cataloging.dao.persistence.T_CAS_SDIV_PRTC_TYP;
import org.folio.cataloging.dao.persistence.T_CAS_SDIV_TMPL_TYP;

import org.folio.cataloging.bean.cas.CasaliniCodeListsBean;
import org.folio.cataloging.dao.DAOCasCache;

/**
 * This class is invoked from the ResultSummary xslt in order to get the list of
 * values in T_CAS_SDIV_GEOG_TYP,T_CAS_SDIV_PRTC_TYP,T_CAS_SDIV_TMPL_TYP
 * 
 * @author Carmen Trazza
 * @version $Revision: 1.1 $, $Date: 2008/05/28 14.57.00 $
 * @since 1.0
 */

@SuppressWarnings("unchecked")
public class CodeTableParser 
{
	private static final boolean isCasalini = Defaults.getBoolean("customer.casalini.parser.enabled", false);
	
	public static Hashtable<String, String> labelAutoritytags = null;
	public static Hashtable<String, String> labelBibliographictags = null;
	public static List<LabelTagDisplay> labelAutorityMarcDisplayList = null;
	public static List<LabelTagDisplay> labelBibliographicMarcDisplayList = null;
	
	/* Bug 4775 inizio */
	public static Hashtable<String, String> labelRdaBibliographictags = null;
	public static List<RdaMarcTagDisplay> labelRdaBibliographicMarcDisplayList = null;
	
	public static String getStringText(String tbl_vlu_cde, String code,	String lingua) 
	{
		List subdivisionType = null;

		if (!isCasalini)
			return tbl_vlu_cde;
		if (code.equals("b"))
			subdivisionType = getTimeSubdivisionType(getLocale(lingua));
		else if (code.equals("c"))
			subdivisionType = getGeographicSubdivisionType(getLocale(lingua));
		else if (code.equals("d"))
			subdivisionType = getSpecialSubdivisionType(getLocale(lingua));
		if (subdivisionType != null) {
			Iterator iter = subdivisionType.iterator();
			while (iter.hasNext()) {
				ValueLabelElement elemento = (ValueLabelElement) iter.next();
				if (elemento.getValue().equals("" + tbl_vlu_cde))
					return elemento.getLabel();
			}
		}
		return tbl_vlu_cde;
	}

	public static String getStringVerificationLevel(int bibNumber, String lingua) 
	{
		List encodingLevel = null;
		/* Fare la select sul DB per amicusNumber */
		DAOBibItem dao = new DAOBibItem();
		BIB_ITM bib = null;
		char verification;
		String tbl_vlu_cde ="";
		try {
			bib = dao.load(bibNumber, 1);
		} catch (RecordNotFoundException e) {
			tbl_vlu_cde ="";
		} catch (DataAccessException e) {
			tbl_vlu_cde ="";
		}
		if(bib!=null){
		 verification = bib.getVerificationLevel();
		 tbl_vlu_cde = "" +verification;
		 encodingLevel = getVerificationLevel(getLocale(lingua));
		 if (encodingLevel != null) {
			Iterator iter = encodingLevel.iterator();
			while (iter.hasNext()) {
				ValueLabelElement elemento = (ValueLabelElement) iter.next();
				if (elemento.getValue().equals("" + tbl_vlu_cde))
					return elemento.getLabel();
			}
		}
		}
		return tbl_vlu_cde;
	}

	public static String getStringManagerialLevelType(int bibNumber, String lingua) 
	{
		List encodingLevel = null;
		DAOCasCache dao = new DAOCasCache();
		List casCache = null;
		String tbl_vlu_cde ="";
		try {
			casCache = dao.loadCasCache(bibNumber);
		
		} catch (DataAccessException e) {
			tbl_vlu_cde ="";
		}
		if(casCache!=null && casCache.size()>0){
		 CasCache cas = (CasCache)casCache.get(0);
		 tbl_vlu_cde = cas.getLevelCard();
		 encodingLevel = getManagerialLevelType(getLocale(lingua));
		 if (encodingLevel != null) {
			Iterator iter = encodingLevel.iterator();
			while (iter.hasNext()) {
				ValueLabelElement elemento = (ValueLabelElement) iter.next();
				if (elemento.getValue().equals("" + tbl_vlu_cde))
					return elemento.getLabel();
			}
		}
		}
		return tbl_vlu_cde;
	}

	public static String getStringEncodingLevel(String tbl_vlu_cde, String lingua) 
	{
		List itemRecordType = null;
		itemRecordType = getEncodingLevel(getLocale(lingua));
		
		Iterator iter = itemRecordType.iterator();
		while (iter.hasNext()) {
			ValueLabelElement elemento = (ValueLabelElement) iter.next();
			if (elemento.getValue().equals("" + tbl_vlu_cde)){
			  return elemento.getLabel();
			}
		}
		return tbl_vlu_cde;
	}
	
	public static String getStringItemBibliographicLevel(String tbl_vlu_cde, String lingua) 
	{
		List itemRecordType = null;
		itemRecordType = getItemBibliographicLevel(getLocale(lingua));
		Iterator iter = itemRecordType.iterator();
		while (iter.hasNext()) {
			ValueLabelElement elemento = (ValueLabelElement) iter.next();
		    if (elemento.getValue().equals("" + tbl_vlu_cde)){
		      return elemento.getLabel();
			}
		}
		return tbl_vlu_cde;
	}

	/**
	 * return the description of the type record
	 * @param tbl_vlu_cde
	 * @param lingua
	 * @return
	 */
	public static String getStringTypeRecord(String tbl_vlu_cde, String lingua) 
	{
		List typeRecord = null;
		typeRecord = getTypeRecord(getLocale(lingua));
		
		Iterator iter = typeRecord.iterator();
		while (iter.hasNext()) {
			ValueLabelElement elemento = (ValueLabelElement) iter.next();
			if (elemento.getValue().equals("" + tbl_vlu_cde)){
			  return elemento.getLabel();
			}
		}
		return tbl_vlu_cde;
	}

	/**
	 * LIBRISUITE.T_CAS_SDIV_GEOG_TYP, {@link T_CAS_SDIV_GEOG_TYP},
	 * {@link GeographicSubdivisionType}
	 */
	public static List getGeographicSubdivisionType(Locale locale) {
		return CasaliniCodeListsBean.getGeographicSubdivisionType().getCodeList(locale);
	}

	/**
	 * LIBRISUITE.T_CAS_SDIV_PRTC_TYP, {@link T_CAS_SDIV_PRTC_TYP},
	 * {@link SpecialSubdivisionType}
	 */
	public static List getSpecialSubdivisionType(Locale locale) {
		return CasaliniCodeListsBean.getSpecialSubdivisionType().getCodeList(locale);
	}

	/**
	 * LIBRISUITE.T_CAS_SDIV_TMPL_TYP, {@link T_CAS_SDIV_TMPL_TYP},
	 * {@link TimeSubdivisionType}
	 */
	public static List getTimeSubdivisionType(Locale locale) {
		return CasaliniCodeListsBean.getTimeSubdivisionType().getCodeList(locale);
	}
	public static List getItemBibliographicLevel(Locale locale) {
		return CodeListsBean.getItemBibliographicLevel().getCodeList(locale);
	}
	public static List getVerificationLevel(Locale locale) {
		return CodeListsBean.getVerificationLevel().getCodeList(locale);
	}
	public static List getEncodingLevel(Locale locale) {
		return CodeListsBean.getEncodingLevel().getCodeList(locale);
	}
	public static List getManagerialLevelType(Locale locale) {
		return CasaliniCodeListsBean.getManagerialLevelType().getCodeList(locale);
	}
	public static List getTypeRecord(Locale locale) {
		return CodeListsBean.getItemRecordType().getCodeList(locale);
	}

	/**
	 * List of BibliographicMarcTagDisplay for language
	 * @param language
	 * @return
	 */
	public static List<LabelTagDisplay> getMarcTagDisplay(String language) {
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getMarcTagDisplay(language);
	}
	
	/**
	 * List of AutorityMarcTagDisplay for language
	 * @param language
	 * @return
	 */
	public static List<LabelTagDisplay> getAutorityMarcTagDisplay(String language) {
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getAutorityMarcTagDisplay(language);
	}
	
	/**
	 * Returns the bibliographic description of the tags for this parameters
	 * @param tag
	 * @param ind1
	 * @param ind2
	 * @param language
	 * @return
	 */
	public static String getLabel(String tag, String ind1, String ind2,String language)
	{		
		if (labelBibliographicMarcDisplayList==null){
			labelBibliographicMarcDisplayList = getMarcTagDisplay(language);
		}
		String key = buildKey(tag, ind1, ind2);
		if (labelBibliographictags==null){
			labelBibliographictags = getHashTags(labelBibliographicMarcDisplayList);
		}
		String label=" ";
		label = labelBibliographictags.get(key);
		if(label==null){
			 key = buildKey(tag, null, null);
			 label=labelBibliographictags.get(key);
		}
		return label;
	}
	
	/**
	 * Returns the autority description of the tags for this parameters 
	 * @param tag
	 * @param ind1
	 * @param ind2
	 * @param language
	 * @return
	 */
	public static String getAutorityLabel(String tag, String ind1, String ind2, String language)
	{
		if (labelAutorityMarcDisplayList==null){
			labelAutorityMarcDisplayList = getAutorityMarcTagDisplay(language);	
		}
		String key = buildKey(tag, ind1, ind2);
		if (labelAutoritytags==null){
			labelAutoritytags = getHashTags(labelAutorityMarcDisplayList);
		}
		String label=" ";
		label = labelAutoritytags.get(key);
		if (label==null){
			key = buildKey(tag, null, null);
			label=labelAutoritytags.get(key);
		}
		return label;
	}
	
	private static Locale getLocale(String lingua) 
	{
		if (lingua.equalsIgnoreCase("eng"))
			return Locale.ENGLISH;
		return Locale.ITALIAN;
	}
	
	public static Hashtable<String,String> getHashTags(List<LabelTagDisplay> marcTagDisplayList)
	{
		Hashtable<String, String> hashTags = new Hashtable<String, String>();
		Iterator<LabelTagDisplay> ite = marcTagDisplayList.iterator();
		while(ite.hasNext())
		{
			LabelTagDisplay marc =(LabelTagDisplay)ite.next();
			String key="";
			String value="";
			if (marc.getMarcFirstIndicator()==null && marc.getMarcSecondIndicator()==null ){
				key= buildKey(marc.getMarcTag(),null,null);
				value= marc.getMarcTagDescription();
				hashTags.put(key,value);
			} else if(marc.getMarcFirstIndicator()!=null && marc.getMarcSecondIndicator()!=null ){
				key= buildKey(marc.getMarcTag(),marc.getMarcFirstIndicator().toString(),marc.getMarcSecondIndicator().toString());
				value= marc.getMarcTagDescription();
				hashTags.put(key,value);
			}
		}
		return hashTags;
	}

	/**
	 * check the tags for format brief
	 * @param numTag
	 * @return
	 */
	public static boolean containsTagsBriefList(String numTag) 
	{
		List <String> vector = getListTags();
		return vector.contains(numTag);
	}
	
	private static List<String> getListTags() 
	{
		List <String> vector = new ArrayList<String>();
		vector.add("020");
		vector.add("022");
		vector.add("040");
		vector.add("100");
		vector.add("110");
		vector.add("111");
		vector.add("130");
		vector.add("240");
		vector.add("245");
		vector.add("246");
		vector.add("700");
		vector.add("710");
		vector.add("711");
		vector.add("730");
		vector.add("740");
		vector.add("850");
		return vector;
	}
	
	public static String buildKey(String tag, String ind1, String ind2){
		return tag+","+ind1+","+ind2;
	}
	
	/**
	 * @param s
	 * @param punctuation
	 * @return a string without punctuation
	 */
	public static String strip(String s) {
		String result = s.trim();
		if (result.endsWith(";")| result.endsWith("/")| 
		     result.endsWith(",")|
			result.endsWith(":")) {
			return result.substring(0, result.length() - 1);
		}
		else {
			return result;
		}
	}
	
	/* Bug 4775 inizio */
	public static String getRdaLabel(String tag, String ind1, String ind2,String language, String typeRecord, String code)
	{		
		if (labelRdaBibliographicMarcDisplayList==null){
			labelRdaBibliographicMarcDisplayList = getRdaMarcTagDisplay(language);
		}
		String key = rdaBuildKey(tag, ind1, ind2, typeRecord, code);
		
		if (labelRdaBibliographictags==null){
			labelRdaBibliographictags = getRdaHashTags(labelRdaBibliographicMarcDisplayList);
		}
		String label=" ";
		label = labelRdaBibliographictags.get(key);
		/* Se indicatori diversi da null e codea  null */
		if(label==null){
			 key = rdaBuildKey(tag, ind1, ind2, typeRecord,null);
			 label=labelRdaBibliographictags.get(key);
		}
		
		//Solo Indicatori a null
		if(label==null){
			 key = rdaBuildKey(tag, null, null, typeRecord,code);
			 label=labelRdaBibliographictags.get(key);
		}
		//Indicatori a null e code null
		if(label==null){
			 key = rdaBuildKey(tag, null, null, typeRecord,null);
			 label=labelRdaBibliographictags.get(key);
		}
		//Solo code a null
		if(label==null){
			 key = rdaBuildKey(tag, ind1, ind2, typeRecord,null);
			 label=labelRdaBibliographictags.get(key);
		}
		
		return label;
	}
	
	private static String rdaBuildKey(String tag, String ind1, String ind2, String typeRecord, String code) 
	{
		StringBuilder builder = new StringBuilder();
		return builder.append(tag).append(",").append(ind1).append(",").append(ind2).append(",").append(typeRecord).append(",").append(code).toString();
	}

	public static List<RdaMarcTagDisplay> getRdaMarcTagDisplay(String language) 
	{
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getRdaMarcTagDisplay(language);
	}
	
	public static Hashtable<String,String> getRdaHashTags(List<RdaMarcTagDisplay> marcTagDisplayList)
	{
		Hashtable<String, String> hashTags = new Hashtable<String, String>();
		Iterator<RdaMarcTagDisplay> ite = marcTagDisplayList.iterator();
		//System.out.println("marcTagDisplayList: "+marcTagDisplayList.size());
		while(ite.hasNext())
		{
			LabelTagDisplay  marc =(LabelTagDisplay)ite.next();
			String key="";
			String value="";
			
			key = rdaBuildKey(
						marc.getMarcTag(),
					   (marc.getMarcFirstIndicator()==null?null:marc.getMarcFirstIndicator().toString()),
					   (marc.getMarcSecondIndicator()==null?null:marc.getMarcSecondIndicator().toString()), 
						marc.getMarcFbrType(),
					   (marc.getMarcTagSubfieldCode()==null?null:marc.getMarcTagSubfieldCode().toString()));
			value = marc.getMarcTagDescription() + "@" + marc.getMarcTagNumberText();
			//System.out.println("VALUE: " +value +" Chiave: " +key+ " --- "+ marc);
			//if(hashTags.containsKey(key)) System.out.println(">>>>>>>>>>> duplicata: "+ key);
			hashTags.put(key,value);				
		}
		
		return hashTags;
	}
	/* Bug 4775 fine */
	
	public static void refreshLabels() 
	{
		labelBibliographicMarcDisplayList = null;
		labelAutorityMarcDisplayList = null;
		labelBibliographictags =null;
		labelAutoritytags =null;
		labelRdaBibliographicMarcDisplayList=null;
		labelRdaBibliographictags=null;
	}
}