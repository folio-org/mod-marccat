package com.casalini.cataloguing.bean;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import librisuite.bean.LibrisuiteBean;
import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.DateInputException;
import librisuite.business.searching.DuplicateKeyException;
import librisuite.hibernate.CLCTN_MST_RULE;
import librisuite.hibernate.CLCTN_MST_RULE_RECORD;
import librisuite.hibernate.CLCTN_MST_RULE_REL;
import librisuite.hibernate.CLCTN_RULE_TMP;

import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import com.casalini.cataloguing.business.DAOCollectionRule;
import com.casalini.cataloguing.business.RuleListElement;
import com.casalini.digital.business.RequiredFieldsException;
import com.libricore.librisuite.controller.SessionUtils;

public class RuleCollectionMSTBean extends LibrisuiteBean 
{
	private static final Log logger = LogFactory.getLog(RuleCollectionMSTBean.class);
	Format formatter = new SimpleDateFormat("dd-MM-yyyy");
	private static final Integer PROC_TYPE_COLLECTION = new Integer(1);
	private static final Integer PROC_TYPE_RECORD = new Integer(2);	
	public static final String PUBBLICATION_DATE = "P";
	public static final String UPLOAD_DATE = "I";
	
	private DAOCollectionRule dao = new DAOCollectionRule();
	private CLCTN_MST_RULE currentItem;
	private List items = new ArrayList();
	private List ruleList = new ArrayList();
	private static Locale locale = Locale.getDefault();
	private boolean flagInsert = false;
	private String dateTypeInput = "P";
	private String dataUploadFrom;
	private String dataUploadTo;
	private String dateRangeInput;
	private String collectionInput;
	private String amicusNumberMotherInput;
	private List yearRangeInputList = new ArrayList();
	private List collectionInputList = new ArrayList();
	private List amicusNumberMotherInputList = new ArrayList();	
	private List ntrLvlList = new ArrayList();
	
	public static RuleCollectionMSTBean getInstance(HttpServletRequest request) throws DataAccessException 
	{
		RuleCollectionMSTBean bean =(RuleCollectionMSTBean) RuleCollectionMSTBean.getSessionAttribute(request,RuleCollectionMSTBean.class);
		if (bean == null) {
			bean = new RuleCollectionMSTBean();
			bean.setSessionAttribute(request, bean.getClass());
		}
		bean.setLocale(SessionUtils.getCurrentLocale(request));
		
	    return bean;
	}

	public List loadAllRules() throws DataAccessException
	{
		return dao.loadAllRules(getLocale(), getNtrLvlList());	
	}

	public void loadRule(int ruleId) throws DataAccessException
	{
		setItems(dao.loadRule(ruleId, getLocale()));
		
		if (items.size() == 0) {
			addNew();
		}
		setCurrentItem((CLCTN_MST_RULE) items.get(items.size() - 1));
	}
	
	public void addNew() throws DataAccessException 
	{
		CLCTN_MST_RULE newItem = new CLCTN_MST_RULE();
		newItem.setDataType("P");
		getItems().add(newItem);
		setCurrentItem(newItem);
	}
	
	public CLCTN_MST_RULE getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(CLCTN_MST_RULE currentItem) {
		this.currentItem = currentItem;
	}

	public List getNtrLvlList() {
		return ntrLvlList;
	}

	public boolean isFlagInsert() {
		return flagInsert;
	}

	public void setFlagInsert(boolean flagInsert) {
		this.flagInsert = flagInsert;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		RuleCollectionMSTBean.locale = locale;
	}
	
	public List getRuleList() {
		return ruleList;
	}

	public void setRuleList(List ruleList) {
		this.ruleList = ruleList;
	}

	public void setDataUploadFrom(String dataUploadFrom) {
		this.dataUploadFrom = dataUploadFrom;
	}

	public void setDataUploadTo(String dataUploadTo) {
		this.dataUploadTo = dataUploadTo;
	}

	public String getDateTypeInput() {
		return dateTypeInput;
	}

	public void setDateTypeInput(String dateTypeInput) {
		this.dateTypeInput = dateTypeInput;
	}

	public String getDateRangeInput() {
		return dateRangeInput;
	}

	public void setDateRangeInput(String dateRangeInput) {
		this.dateRangeInput = dateRangeInput;
	}

	public String getAmicusNumberMotherInput() {
		return amicusNumberMotherInput;
	}

	public void setAmicusNumberMotherInput(String amicusNumberMotherInput) {
		this.amicusNumberMotherInput = amicusNumberMotherInput;
	}

	public List getYearRangeInputList() {
		return yearRangeInputList;
	}

	public void setYearRangeInputList(List yearRangeInputList) {
		this.yearRangeInputList = yearRangeInputList;
	}

	public List getCollectionInputList() {
		return collectionInputList;
	}

	public void setCollectionInputList(List collectionInputList) {
		this.collectionInputList = collectionInputList;
	}

	public String getCollectionInput() {
		return collectionInput;
	}

	public void setCollectionInput(String collectionInput) {
		this.collectionInput = collectionInput;
	}

	public List getAmicusNumberMotherInputList() {
		return amicusNumberMotherInputList;
	}

	public void setAmicusNumberMotherInputList(List amicusNumberMotherInputList) {
		this.amicusNumberMotherInputList = amicusNumberMotherInputList;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public String getDataUploadFrom() 
	{
		if (getCurrentItem().getDataUploadFrom() != null) {
			return formatter.format(getCurrentItem().getDataUploadFrom());
		}else 
			return null;
	}
	public String getDataUploadTo() 
	{
		if (getCurrentItem().getDataUploadTo() != null) {
			return formatter.format(getCurrentItem().getDataUploadTo());
		}else 
			return null;
	}

	public void addMother() throws RequiredFieldsException, NumberFormatException, DuplicateKeyException 
	{
		if (getAmicusNumberMotherInput()==null || getAmicusNumberMotherInput().trim().length()==0){
			throw new RequiredFieldsException();
		} 
		
//----> Controllo numericita'
		Long recordIdNum = new Long(getAmicusNumberMotherInput().trim());
//		String recordId = String.valueOf(recordIdNum);
		
		CLCTN_MST_RULE_RECORD mstRuleRecord = null; 
		for (int i = 0; i < getCurrentItem().getRecordsList().size(); i++) {
			mstRuleRecord = (CLCTN_MST_RULE_RECORD)getCurrentItem().getRecordsList().get(i);
			if (mstRuleRecord.getRecordId().equals(recordIdNum)){
//			if (mstRuleRecord.getRecordId()==recordIdNum){
				throw new DuplicateKeyException();
			}
		}
		mstRuleRecord = new CLCTN_MST_RULE_RECORD(getCurrentItem().getRuleId(),recordIdNum); 
		getCurrentItem().getRecordsList().add(mstRuleRecord);
		Collections.sort(getCurrentItem().getRecordsList());
	}

	public void addCollection() throws RequiredFieldsException, NumberFormatException, DuplicateKeyException 
	{
		if (getCollectionInput()==null || getCollectionInput().trim().length()==0){
			throw new RequiredFieldsException();
		} 
		
//----> Controllo numericita'
		Long collNum = new Long(getCollectionInput().trim());
//		String coll = String.valueOf(collNum);
		
		CLCTN_MST_RULE_REL mstRuleRel = null; 
		for (int i = 0; i < getCurrentItem().getCollectionsList().size(); i++) {
			mstRuleRel = (CLCTN_MST_RULE_REL)getCurrentItem().getCollectionsList().get(i);
			if (mstRuleRel.getIdCollection().equals(collNum)){
//			if (mstRuleRel.getIdCollection()==collNum){
				throw new DuplicateKeyException();
			}
		}		
		mstRuleRel = new CLCTN_MST_RULE_REL(getCurrentItem().getRuleId(),collNum); 
		getCurrentItem().getCollectionsList().add(mstRuleRel);
		Collections.sort(getCurrentItem().getCollectionsList());
	}
	
	public void addYear() throws RequiredFieldsException, NumberFormatException, DuplicateKeyException, DateInputException 
	{
		if (getDateRangeInput()==null || getDateRangeInput().trim().length()==0){
			throw new RequiredFieldsException();
		} 
		
//----> Controllo numericita'
		Integer year = new Integer(getDateRangeInput());	
		
		if(!GenericValidator.minLength(getDateRangeInput(), 4)){
			throw new DateInputException("error.year.input");
		}
		
//		if (Integer.parseInt(getDateRangeInput())<1900){
//			throw new DateInputException("error.year.input");
//		}
		
		for (int i = 0; i < getYearRangeInputList().size(); i++) {
			if (getYearRangeInputList().get(i).equals(year)){
				throw new DuplicateKeyException();
			}
		}		
		getYearRangeInputList().add(year);
		Collections.sort(getYearRangeInputList());
	}
	
	public void refresh()
	{
		setDateTypeInput("P");
		setDataUploadFrom("");
		setDataUploadTo("");
		setDateRangeInput("");
		setAmicusNumberMotherInput("");
		setCollectionInput("");
		getYearRangeInputList().clear();
		getCollectionInputList().clear();
		getAmicusNumberMotherInputList().clear();
		setCurrentItem(null);
		setFlagInsert(false);
	}

	public void prepareRuleForSave() throws DataAccessException
	{
		CLCTN_MST_RULE item = getCurrentItem();
		
		item.setFlagProcessing("N");
		item.setDataProcessing(null);
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!item.isNew()){
			item.setDataUpdate(formatter.parse(formatter.format(new Date()),new ParsePosition(0)));
			item.markChanged();
		}else {
			item.setDataInsert(formatter.parse(formatter.format(new Date()),new ParsePosition(0)));
			item.setDataUpdate(formatter.parse(formatter.format(new Date()),new ParsePosition(0)));
			item.markNew();
		}
		
		if ("P".equalsIgnoreCase(item.getDataType())){
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < getYearRangeInputList().size(); i++) {
				if (buffer.length()>0){
					buffer.append(", ");
				}
				buffer.append(getYearRangeInputList().get(i));
			}
			item.setDataPublRange(buffer.toString());
			item.setDataUploadFrom(null);
			item.setDataUploadTo(null);
		}else {
			item.setDataPublRange(null);
		}
//		item.setRecordCollectionList(loadTmpRuleList(getCollectionInputList(), getAmicusNumberMotherInputList()));
		item.setRecordCollectionList(loadTmpRuleList(getCurrentItem().getCollectionsList(), getCurrentItem().getRecordsList()));
	}
	
	public void saveRule() throws DataAccessException, HibernateException
	{
		CLCTN_MST_RULE item = getCurrentItem();
		dao.saveRuleAndRelations(item);
	}
	
	public List loadTmpRuleList(List collectionList, List recordList)
	{
		List tmpList = new ArrayList();
		CLCTN_RULE_TMP ruleTmp = null;
		
//----> Carico le collection nella tabella temporanea, non imposto il ruleId perche' lo recupero dopo l'insert della tabella CLCNT_MST_RULE
		CLCTN_MST_RULE_REL ruleCollection = null;
		for (int i = 0; i < collectionList.size(); i++) {
			ruleCollection = (CLCTN_MST_RULE_REL) collectionList.get(i);
			ruleTmp = new CLCTN_RULE_TMP();
			ruleTmp.setType(PROC_TYPE_COLLECTION);
			ruleTmp.setIdItem(ruleCollection.getIdCollection());
			tmpList.add(ruleTmp);
		}
		
//----> Carico i record nella tabella temporanea, non imposto il ruleId perche' lo recupero dopo l'insert della tabella CLCNT_MST_RULE
		CLCTN_MST_RULE_RECORD ruleRecord = null;
		for (int i = 0; i < recordList.size(); i++) {
			ruleRecord = (CLCTN_MST_RULE_RECORD) recordList.get(i);
			ruleTmp = new CLCTN_RULE_TMP();
			ruleTmp.setType(PROC_TYPE_RECORD);
			ruleTmp.setIdItem(ruleRecord.getRecordId());
			tmpList.add(ruleTmp);
		}
		return tmpList;
	}
	
	public void deleteRule(int idRule) throws DataAccessException
	{
		dao.delete(idRule);
	}

	public void loadNrtLvlList() 
	{
		getNtrLvlList().clear();
//---->	Imposto la tendina statica della natura livello
		ResourceBundle bundle = ResourceBundle.getBundle("resources/casalini/collection", getLocale());
		getNtrLvlList().add(new ValueLabelElement("", bundle.getString("ntr.lvl.not")));
		getNtrLvlList().add(new ValueLabelElement("s", bundle.getString("ntr.lvl.s")));
		getNtrLvlList().add(new ValueLabelElement("011", bundle.getString("ntr.lvl.011")));
		getNtrLvlList().add(new ValueLabelElement("m", bundle.getString("ntr.lvl.m")));
		getNtrLvlList().add(new ValueLabelElement("014", bundle.getString("ntr.lvl.014")));
		getNtrLvlList().add(new ValueLabelElement("002", bundle.getString("ntr.lvl.002")));
		getNtrLvlList().add(new ValueLabelElement("004", bundle.getString("ntr.lvl.004")));
		getNtrLvlList().add(new ValueLabelElement("006", bundle.getString("ntr.lvl.006")));
	}

	public void deleteItem(List itemList, int index) 
	{
		itemList.remove(index);
	}
	
	public List splitYearsRange(String dataRange)
	{		
		List yearList = new ArrayList();
		if (dataRange!=null && dataRange.trim().length()>0){
			String[] arr = dataRange.split(", ");
			for (int i = 0; i < arr.length; i++) {
				yearList.add(new Integer(arr[i]));
			}	
		}
		return yearList;
	}

	public void prepareForEditing(int indexElement) throws DataAccessException 
	{
		setCurrentItem(((RuleListElement)getRuleList().get(indexElement)).getRule());
		if ("P".equalsIgnoreCase(getCurrentItem().getDataType())){
			setYearRangeInputList(splitYearsRange(getCurrentItem().getDataPublRange()));	
		}
		dao.loadRelationsRule(getCurrentItem());
		setFlagInsert(true);
	}
}