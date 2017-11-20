package org.folio.cataloging.business.marchelper.parser;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.model.Subfield;

public class PunctuationList extends AbstractList {
	
	public class GenericFieldFilter {
		// accept all subfields
		boolean accept(PunctuationSubfield punctuationSubfield, String variantCodes){
			return true;
		}
	}

	public class VariantFilter extends GenericFieldFilter {
//		 accept only variant subfields
		boolean accept(PunctuationSubfield punctuationSubfield, String variantCodes){
			return variantCodes.indexOf(punctuationSubfield.getCode())>=0;
		}
	}	
	
	public class HeadingFilter extends GenericFieldFilter {
//		 accept only not variant subfields
		boolean accept(PunctuationSubfield punctuationSubfield, String variantCodes){
			return variantCodes.indexOf(punctuationSubfield.getCode())<0;
		}
	}
	
	private List ls;
	
	public PunctuationList() {
		super();
		ls = new ArrayList();
	}
	
	public PunctuationList(Collection list) {
		super();
		ls = (List) list;
	}

	public void addField(String content, int begin, int end) throws PunctuationParsingException {
		if(begin==end) {
			// the situation is this:  {}
			// the field is wrong or the {} are part of punctuation
			// I assume field wrong so...
			throw new PunctuationParsingException("a field is not declared");
		}
		String field = content.substring(begin, end);
		ls.add(new PunctuationField(field));
	}

	public void addPunctuation(String content, int begin, int end) {
		if(begin==end) return;
		String punctuation = content.substring(begin, end);
		ls.add(new PunctuationElement(punctuation));
	}

	public void addPunctuation(String content, int begin) {
		addPunctuation(content, begin, content.length());
	}

	public void addCode(Subfield subfield) {
		ls.add(new PunctuationSubfield(subfield));
	}

	public Object get(int index) {
		return ls.get(index);
	}

	public int size() {
		return ls.size();
	}

	public String toString() {
		String s = "";
		Iterator it = iterator();
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			s += elem.toString();
		}
		return s;
	}

	public void add(int arg0, Object arg1) {
		ls.add(arg0, arg1);
	}

	public boolean add(Object arg0) {
		if (!(arg0 instanceof PunctuationElement)) {
			throw new IllegalArgumentException("expected PunctuationElement");
		}
		return ls.add(arg0);
	}

	public Object remove(int index) {
		return ls.remove(index);
	}

	public Object set(int arg0, Object arg1) {
		if (!(arg1 instanceof PunctuationElement)) {
			throw new IllegalArgumentException("expected PunctuationElement");
		}
		return ls.set(arg0, arg1);
	}

	private PunctuationList getFilteredVariantFieldList (GenericFieldFilter filter, String variantCodes, boolean markAsVariant){
		PunctuationList fields = new PunctuationList();
		Iterator it = iterator();
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(elem instanceof PunctuationSubfield) {
				PunctuationSubfield punctuationSubfield = (PunctuationSubfield)elem;
				// add only variant codes
				if(filter.accept(punctuationSubfield, variantCodes)){
					PunctuationList filteredFieldList = (punctuationSubfield).getFilteredFieldList();
					filteredFieldList.markFieldsAsVariant(markAsVariant);
					fields.addAll(filteredFieldList);
				}
			}
		}
		return fields;
	}

	private void markFieldsAsVariant(boolean markAsVariant) {
		Iterator it = this.iterator();
		while (it.hasNext()) {
			PunctuationField element = (PunctuationField) it.next();
			element.setVariant(markAsVariant);
		}
	}

	/**
	 * @return all Fields (Heading fields + Variant fields)
	 */
	public PunctuationList getFilteredFieldList(String variantCodes){
		if(variantCodes==null) {
			// unmarked
			return getFilteredVariantFieldList(new GenericFieldFilter(), null, false);
		}
		// marked
		PunctuationList headingFields = getFilteredVariantFieldList(new HeadingFilter(), variantCodes, false);
		PunctuationList variantFields = getFilteredVariantFieldList(new VariantFilter(), variantCodes, true);
		headingFields.addAll(variantFields);
		return headingFields;
	}
	
	/**
	 * @return only variant Fields (Variant fields)
	 */
	public PunctuationList getVariantFieldList(String variantCodes){
		return getFilteredVariantFieldList(new VariantFilter(), variantCodes, true);
	}

	/**
	 * @return only heading Fields (Heading fields)
	 */
	public PunctuationList getHeadingFieldList(String variantCodes){
		return getFilteredVariantFieldList(new HeadingFilter(), variantCodes, false);
	}
	
	public PunctuationList getFilteredSubfieldList(){
		PunctuationList codes = new PunctuationList();
		Iterator it = iterator();
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(elem instanceof PunctuationSubfield) {
				codes.add(elem);
			}
		}
		return codes;
	}
	
	public List getCodesList(){
		List codes = new ArrayList();
		Iterator it = getFilteredSubfieldList().iterator();
		while (it.hasNext()) {
			PunctuationSubfield elem = (PunctuationSubfield) it.next();
			codes.add(elem.getCode());
		}
		return codes;
	}
	
	public List getMarcCodesList(){
		List codes = new ArrayList();
		Iterator it = getFilteredSubfieldList().iterator();
		while (it.hasNext()) {
			PunctuationSubfield elem = (PunctuationSubfield) it.next();
			codes.add(elem.getMarcCode());
		}
		return codes;
	}
	
	public String getMarcCodesString(){
		String codes = "";
		Iterator it = getFilteredSubfieldList().iterator();
		while (it.hasNext()) {
			PunctuationSubfield elem = (PunctuationSubfield) it.next();
			codes += elem.getMarcCode();
		}
		return codes;
	}
	
	public String getCodesString(){
		String codes = "";
		Iterator it = getFilteredSubfieldList().iterator();
		while (it.hasNext()) {
			PunctuationSubfield elem = (PunctuationSubfield) it.next();
			codes += elem.getCode();
		}
		return codes;
	}

	public PunctuationList getPartialList(String excludeList) {
		
		if(excludeList==null || excludeList.trim().length()==0) {
			return this;
		}
		
		// TODO _MIKE: use 
		PunctuationList newPl = new PunctuationList();
		Iterator it = this.iterator();
		while (it.hasNext()) {
			PunctuationElement element = (PunctuationElement) it.next();
			if(element instanceof PunctuationSubfield) {
				String code = ((PunctuationSubfield)element).getCode();
				if(excludeList.indexOf(code)<0) newPl.add(element);
			}
		}
		
		return newPl;
	}
	

}
