/*
 * Project LibriCat
 * (c) PrimeSource 2007
 * 
 * Author: michele.mercurio@primesource.it
 * Date: 05/ott/07 09:46:43
 * Source: librisuite.business.marchelper.MarcHelperSample
 */

package librisuite.business.marchelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import librisuite.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.marchelper.parser.FieldValueReplacer;
import librisuite.business.marchelper.parser.PunctuationField;
import librisuite.business.marchelper.parser.PunctuationList;
import librisuite.business.marchelper.parser.SampleMatcher;
import librisuite.hibernate.TAG_MODEL;

import com.libricore.librisuite.common.StringText;

/**
 * @author michelem
 *
 */
public class TagModelItem {
	
	/**
	 * This list is only used to retrieve the page values
	 */
	private List/*<KeyValueLabelElement>*/ instanceFields = null;
	private Locale currentLocale;
	
	
	private TAG_MODEL selectedModel = null;
	
	public TagModelItem(TAG_MODEL matchingModel, Locale locale, String variantCodes) {
		super();
		this.selectedModel = matchingModel;
		initEmpty(locale, variantCodes);
	}
	
	public TagModelItem(TAG_MODEL matchingModel, StringText st, Locale locale, String variantCodes, boolean excludeVariant) {
		super();
		this.selectedModel = matchingModel;
		init(st, locale, variantCodes, excludeVariant);
	}

	private void init(StringText st, Locale locale, String variantCodes, boolean excludeVariants){
		currentLocale = locale;
		if(excludeVariants){
			populateExcludingVariants(st, variantCodes);
		} else {
			populate(st, variantCodes, false);
		}
	}
	
//	public void replaceStringText(StringText st, Locale locale){
//		init(st, locale);
//	}
//
	
	private void initEmpty(Locale locale, String variantCodes){
		currentLocale = locale;
		SampleMatcher matcher = new SampleMatcher();
		Map fieldMap = new Hashtable();
		matcher.populate(selectedModel, fieldMap, variantCodes);
		List fieldLabels/*<KeyValueLabelElement>*/ = getFieldLabels(fieldMap, locale, variantCodes);
		instanceFields = fieldLabels;
	}

	public void forceStringTextValues(StringText st, String variantCodes){
		SampleMatcher matcher = new SampleMatcher();
		Map fieldMap = new Hashtable();
		PunctuationList punctuationElements = getSelectedModel().getPunctuationElements();
		matcher.forcePopulatation(punctuationElements, fieldMap, st);
		instanceFields = getFieldLabels(fieldMap, currentLocale, variantCodes);
	}
	
	/**
	 * @param variantCodes
	 * @param ignoreVariantCodes 
	 */
	public void populate(StringText st, String variantCodes, boolean ignoreVariantCodes) {
		SampleMatcher matcher = new SampleMatcher();
		Map fieldMap = new Hashtable();
		matcher.populate(getSelectedModel(), fieldMap, st, variantCodes, ignoreVariantCodes);
		instanceFields = getFieldLabels(fieldMap, currentLocale, variantCodes);
	}

	public void populateExcludingVariants(StringText st, String variantCodes){
		SampleMatcher matcher = new SampleMatcher();
		Map fieldMap = new Hashtable();
		PunctuationList partialPunctuationElements = getSelectedModel().getPunctuationElements().getPartialList(variantCodes);
		matcher.populate(partialPunctuationElements, fieldMap, st);
		instanceFields = getFieldLabels(fieldMap, currentLocale, variantCodes);
	}

	public boolean isMatchingSelected(StringText st, String exclude){
		MatcherPredicate matcher = new MatcherPredicate(st ,exclude);
		return matcher.evaluate(getSelectedModel());
	}
	
	private List/*<MarcHelperTagModel>*/ models = null;

	public List getModels() {
		return models;
	}

	public void setModels(List models) {
		this.models = models;
	}

	public List/*<KeyValueLabelElement>*/ getInstanceFields() {
		return instanceFields;
	}

	private List/*<KeyValueLabelElement>*/ getVariantFields() {
		// TODO _MIKE:
		return instanceFields;
	}

	public List/*<KeyValueLabelElement>*/ getHeadingFields() {
		List hList = new ArrayList();
		Iterator it = instanceFields.iterator();
		while (it.hasNext()) {
			KeyValueLabelElement elem = (KeyValueLabelElement) it.next();
			if(!elem.isVariant()){
				hList.add(elem);
			}
		}
		return hList;
	}

	public String toString() {
		return getInstanceFields().toString();
	}

	public TAG_MODEL getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(TAG_MODEL selectedModel) {
		this.selectedModel = selectedModel;
	}
	
	/**
	 * Update only its values
	 * @param newValues
	 */
	public void refresh(Map newValues) {
		List fields = getInstanceFields();
		Iterator it = fields.iterator();
		while (it.hasNext()) {
			KeyValueLabelElement elem = (KeyValueLabelElement) it.next();
			String value = (String) newValues.get(elem.getKey());
			elem.setValue(value!=null?value:"");
		}
	}
	
	private List/*<KeyValueLabelElement>*/ getFieldLabels(Map fieldMap, Locale locale, String variantCodes) {
		List labels = CodeListsBean.getMarcHelperLabel().getCodeList(locale);
		List fieldLabels = new ArrayList();
//		Iterator it = fieldMap.keySet().iterator();
		Iterator it = getSelectedModel().getPunctuationElements().getFilteredFieldList(variantCodes).iterator();
		while (it.hasNext()) {
			PunctuationField key = (PunctuationField) it.next();
			String value = (String) fieldMap.get(key.getValue());
			String label = findLabel(labels, key.getValue());
			KeyValueLabelElement mhField = new KeyValueLabelElement(key.getValue(), value, label);
			mhField.setVariant(key.isVariant());
			fieldLabels.add(mhField);
		}
		return fieldLabels;
	}

	private String findLabel(List labels, String key) {
		Iterator it = labels.iterator();
		while (it.hasNext()) {
			ValueLabelElement element = (ValueLabelElement) it.next();
			if(key.equals(element.getValue())){
				return element.getLabel();
			}
		}
		// TODO _MIKE: throws an exception instead
		return "?? "+key+" ??";
	}
	
	public StringText getStringText(){
		return getStringText(getInternalMap());
	}
	
	public StringText getStringText(Map values){
		FieldValueReplacer replacer = new FieldValueReplacer();
		String text = replacer.replaceFieldValue(values, getSelectedModel().getPunctuationElements());
		StringText st = new StringText(text);
		return st;
	}

	private Map getInternalMap(){
		Map internal = new HashMap();
		Iterator it = getInstanceFields().iterator();
		while (it.hasNext()) {
			KeyValueLabelElement elem = (KeyValueLabelElement) it.next();
			internal.put(elem.getKey(), elem.getValue());
		}
		return internal;
	}
	/**
	 * MIKE
	 * @return true if no KeyValueLabelElement are present or all the value are empty
	 */
	public boolean isEmpty(boolean checkVariant){
		if(getInstanceFields().size()==0) {
			return true;
		}
		Iterator it = getInstanceFields().iterator();
		while(it.hasNext()){
			KeyValueLabelElement s = (KeyValueLabelElement)it.next();
			if(s.isVariant() && !checkVariant) {
				continue;
			}
			if(s.getValue().equals("")) {
				return true;
			}
		}
		return false;
	}
}
