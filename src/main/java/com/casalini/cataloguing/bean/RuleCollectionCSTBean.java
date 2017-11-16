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
import librisuite.hibernate.CLCTN_CST_RULE;
import librisuite.hibernate.CLCTN_CST_RULE_RECORD;
import librisuite.hibernate.CLCTN_RULE_TMP;
import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import com.casalini.cataloguing.business.DAOCollectionRuleCST;
import com.casalini.cataloguing.business.RuleCSTListElement;
import com.casalini.digital.business.RequiredFieldsException;
import com.libricore.librisuite.controller.SessionUtils;

public class RuleCollectionCSTBean extends LibrisuiteBean 
{
	private static final Log logger = LogFactory.getLog(RuleCollectionCSTBean.class);
	Format formatter = new SimpleDateFormat("dd-MM-yyyy");
	private static final Integer PROC_RECORD = new Integer(2);
	public static final String PUBBLICATION_DATE = "P";
	public static final String UPLOAD_DATE = "I";
	
	private DAOCollectionRuleCST dao = new DAOCollectionRuleCST();
	private CLCTN_CST_RULE currentItem;
	private List items = new ArrayList();
	private List ruleList = new ArrayList();
	private static Locale locale = Locale.getDefault();
	private boolean flagInsert = false;
	private String dateTypeInput = "P";
	private String dateRangeInput;
	private String dateEmbRangeInput;
	private String collectionSRCInput;
	private String collectionTRGInput;
	private String amicusNumberMotherInput;
	private List yearRangeInputList = new ArrayList();
	private List yearEmbRangeInputList = new ArrayList();
	private List amicusNumberMotherInputList = new ArrayList();	
	private List ntrLvlList = new ArrayList();
	
	public static RuleCollectionCSTBean getInstance(HttpServletRequest request) throws DataAccessException 
	{
		RuleCollectionCSTBean bean =(RuleCollectionCSTBean) RuleCollectionCSTBean.getSessionAttribute(request,RuleCollectionCSTBean.class);
		if (bean == null) {
			bean = new RuleCollectionCSTBean();
			bean.setSessionAttribute(request, bean.getClass());
		}
		bean.setLocale(SessionUtils.getCurrentLocale(request));
		
	    return bean;
	}

	public List loadAllRules() throws DataAccessException
	{
		return dao.loadAllRules(getLocale(), getNtrLvlList(), getCustomerList());	
	}

	public void loadRule(int ruleId) throws DataAccessException
	{
		setItems(dao.loadRule(ruleId, getLocale()));
		
		if (items.size() == 0) {
			addNew();
		}
		setCurrentItem((CLCTN_CST_RULE) items.get(items.size() - 1));
	}
	
	public void addNew() throws DataAccessException 
	{
		CLCTN_CST_RULE newItem = new CLCTN_CST_RULE();
		newItem.setDataType(PUBBLICATION_DATE);
		getItems().add(newItem);
		setCurrentItem(newItem);
	}
	
	public CLCTN_CST_RULE getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(CLCTN_CST_RULE currentItem) {
		this.currentItem = currentItem;
	}

	public List getCustomerList() {
		return CasaliniCodeListsBean.getCustomerType().getCodeList(getLocale());
	}
	
	public List getYearEmbRangeInputList() {
		return yearEmbRangeInputList;
	}

	public void setYearEmbRangeInputList(List yearEmbRangeInputList) {
		this.yearEmbRangeInputList = yearEmbRangeInputList;
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
		RuleCollectionCSTBean.locale = locale;
	}
	
	public List getRuleList() {
		return ruleList;
	}

	public void setRuleList(List ruleList) {
		this.ruleList = ruleList;
	}

	public String getDateTypeInput() {
		return dateTypeInput;
	}

	public void setDateTypeInput(String dateTypeInput) {
		this.dateTypeInput = dateTypeInput;
	}

	public String getDataEmbFrom() 
	{
		if (getCurrentItem().getDataEmbFrom() != null) {
			return formatter.format(getCurrentItem().getDataEmbFrom());
		}else 
			return null;
	}

	public String getDataEmbTo() 
	{
		if (getCurrentItem().getDataEmbTo() != null) {
			return formatter.format(getCurrentItem().getDataEmbTo());
		}else 
			return null;
	}

	public String getDateRangeInput() {
		return dateRangeInput;
	}

	public void setDateRangeInput(String dateRangeInput) {
		this.dateRangeInput = dateRangeInput;
	}

	public String getDateEmbRangeInput() {
		return dateEmbRangeInput;
	}

	public void setDateEmbRangeInput(String dateEmbRangeInput) {
		this.dateEmbRangeInput = dateEmbRangeInput;
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

	public String getCollectionSRCInput() {
		return collectionSRCInput;
	}

	public void setCollectionSRCInput(String collectionSRCInput) {
		this.collectionSRCInput = collectionSRCInput;
	}

	public String getCollectionTRGInput() {
		return collectionTRGInput;
	}

	public void setCollectionTRGInput(String collectionTRGInput) {
		this.collectionTRGInput = collectionTRGInput;
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
		
		CLCTN_CST_RULE_RECORD mstRuleRecord = null; 
		for (int i = 0; i < getCurrentItem().getRecordsList().size(); i++) {
			mstRuleRecord = (CLCTN_CST_RULE_RECORD)getCurrentItem().getRecordsList().get(i);
			if (mstRuleRecord.getRecordId().equals(recordIdNum)){
				throw new DuplicateKeyException();
			}
		}
		mstRuleRecord = new CLCTN_CST_RULE_RECORD(getCurrentItem().getRuleId(),recordIdNum); 
		getCurrentItem().getRecordsList().add(mstRuleRecord);
		Collections.sort(getCurrentItem().getRecordsList());
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
	
	public void addEmbYear() throws RequiredFieldsException, NumberFormatException, DuplicateKeyException, DateInputException 
	{
		if (getDateEmbRangeInput()==null || getDateEmbRangeInput().trim().length()==0){
			throw new RequiredFieldsException();
		} 
		
//----> Controllo numericita'
		Integer year = new Integer(getDateEmbRangeInput());	
		
		if(!GenericValidator.minLength(getDateEmbRangeInput(), 4)){
			throw new DateInputException("error.year.input");
		}
		
//		if (Integer.parseInt(getDateRangeInput())<1900){
//			throw new DateInputException("error.year.input");
//		}
		
		for (int i = 0; i < getYearEmbRangeInputList().size(); i++) {
			if (getYearEmbRangeInputList().get(i).equals(year)){
				throw new DuplicateKeyException();
			}
		}		
		getYearEmbRangeInputList().add(year);
		Collections.sort(getYearEmbRangeInputList());
	}
	
	public void refresh()
	{
		setDateTypeInput(PUBBLICATION_DATE);
		setDateRangeInput(null);
		setDateEmbRangeInput(null);
		setAmicusNumberMotherInput(null);
		setCollectionSRCInput(null);
		setCollectionTRGInput(null);
		getYearRangeInputList().clear();
		getYearEmbRangeInputList().clear();
		getAmicusNumberMotherInputList().clear();
		setCurrentItem(null);
		setFlagInsert(false);
	}

	public void prepareRuleForSave() throws DataAccessException
	{
		CLCTN_CST_RULE item = getCurrentItem();
		
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
		
		if (RuleCollectionCSTBean.PUBBLICATION_DATE.equalsIgnoreCase(item.getDataType())){
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
			
			buffer.setLength(0);
			for (int i = 0; i < getYearEmbRangeInputList().size(); i++) {
				if (buffer.length()>0){
					buffer.append(", ");
				}
				buffer.append(getYearEmbRangeInputList().get(i));
			}
			item.setDataEmbRange(buffer.toString());
			item.setDataEmbFrom(null);
			item.setDataEmbTo(null);
			
		}else {
			item.setDataPublRange(null);
			item.setDataEmbRange(null);
		}
		
		if (getCollectionSRCInput()!=null && getCollectionSRCInput().trim().length()>0){
			item.setCollectionSource(Long.valueOf(getCollectionSRCInput()));
		}else {
			item.setCollectionSource(new Long(0));
		}
		if (getCollectionTRGInput()!=null && getCollectionTRGInput().trim().length()>0){
			item.setCollectionTarget(Long.valueOf(getCollectionTRGInput()));
		}else {
			item.setCollectionTarget(new Long(0));
		}
		
//		item.setRecordCollectionList(loadTmpRuleList(getAmicusNumberMotherInputList()));
		item.setRecordCollectionList(loadTmpRuleList(getCurrentItem().getRecordsList()));
	}
	
	public void saveRule() throws DataAccessException, HibernateException
	{
		CLCTN_CST_RULE item = getCurrentItem();
		dao.saveRuleAndRelations(item);
	}
	
	/**
	 * Il metodo carica i records nella tabella temporanea, senza impostare il ruleId perche' lo recupera dopo l'insert della tabella CLCNT_MST_RULE 
	 * @param recordList
	 * @return
	 */
	public List loadTmpRuleList(List recordList)
	{
		List tmpList = new ArrayList();
		CLCTN_RULE_TMP ruleTmp = null;
		CLCTN_CST_RULE_RECORD ruleRecord = null;
		for (int i = 0; i < recordList.size(); i++) {
			ruleRecord = (CLCTN_CST_RULE_RECORD) recordList.get(i);
			ruleTmp = new CLCTN_RULE_TMP();
			ruleTmp.setType(PROC_RECORD);
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
		setCurrentItem(((RuleCSTListElement)getRuleList().get(indexElement)).getRule());
		
		setCollectionSRCInput(String.valueOf(getCurrentItem().getCollectionSource().longValue()));
		setCollectionTRGInput(String.valueOf(getCurrentItem().getCollectionTarget().longValue()));
		
		if (RuleCollectionCSTBean.PUBBLICATION_DATE.equalsIgnoreCase(getCurrentItem().getDataType())){
			setYearRangeInputList(splitYearsRange(getCurrentItem().getDataPublRange()));
			setYearEmbRangeInputList(splitYearsRange(getCurrentItem().getDataEmbRange()));
		}
		dao.loadRelationsRule(getCurrentItem());
		setFlagInsert(true);
	}
}