package org.folio.cataloging.business.marchelper.parser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.folio.cataloging.business.marchelper.MarcHelperUtils;
import org.folio.cataloging.dao.persistence.TAG_MODEL;

import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;

public class SampleMatcher {
	
	public boolean deepMatch(StringText st, PunctuationList/*<PunctuationSubfield>*/ sample){
		if(!softMatch(st, sample)) return false;
		// matching subfields
		// notice: this code don't match correctly if a subfield is repeated
		Iterator it = sample.iterator();
		while (it.hasNext()) {
			PunctuationSubfield subfield = (PunctuationSubfield) it.next();
			StringText stSubfields = st.getSubfieldsWithCodes(subfield.getCode());
			if(stSubfields.getNumberOfSubfields()>1){
				throw new IllegalArgumentException("Subfield "+subfield.getCode()+" repeated in "+sample);
			}
			Subfield stSubfield = stSubfields.getSubfield(0);
			if (subfield.isPunctuationPresents() 
					&& !matchSubfield(subfield, stSubfield)) {
				return false;
			}
		}
		return true;
	}

	public boolean softMatch(StringText st, PunctuationList sample) {
		if(st.getNumberOfSubfields()!=sample.size()) {
			return false;
		}
		String sampleCodes = MarcHelperUtils.sortString(sample.getCodesString());
		String stCodes = MarcHelperUtils.createStringTextMatchKey(st);
		if(!stCodes.equals(sampleCodes)){
			return false;
		}
		return true;
	}

	private boolean matchSubfield(PunctuationSubfield subfield,
			Subfield stSubfield) {
		Iterator it = subfield.getElements().iterator();
		String content = stSubfield.getContent();
		int pos = 0;
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(!(elem instanceof PunctuationField)){
				String value = elem.getValue();
				int npos = content.indexOf(value, pos);
				if(npos<0) {
					return false;
				}
				pos = npos + value.length();
			}
		}
		return true;
	}


	/**
	 * Populate using StringText values
	 * this method is overloaded.
	 * @param matchingModel
	 * @param tableValues
	 * @param st
	 */
	public void populate(TAG_MODEL matchingModel, Map tableValues, StringText st, String variantCodes, boolean variantCanBeIgnored){
		Iterator it = matchingModel.getPunctuationElements().iterator();
		while (it.hasNext()) {
			PunctuationSubfield subfield = (PunctuationSubfield) it.next();
			StringText stSubfields = st.getSubfieldsWithCodes(subfield.getCode());
			if(stSubfields.getNumberOfSubfields()>1){
				// TODO _MIKE repeated subfields! throw exception?
				throw new IllegalArgumentException("Sample has multiple subfields: "+matchingModel.toString());
			}
			Subfield stSubfield;
			try {
				stSubfield = stSubfields.getSubfield(0);
			} catch (IndexOutOfBoundsException e) {
				// is a variant code ignorable?
				if( variantCanBeIgnored 
					&& variantCodes!=null 
					&& variantCodes.indexOf(subfield.getCode())>-1
				) continue;
				else throw e;
			}			
			if (subfield.getFilteredFieldList().size()>0) {
				extractValues(subfield, stSubfield, tableValues);
			}
		}
	}

	/**
	 * Populate using StringText values
	 * this method is overloaded.
	 * @param partialList
	 * @param tableValues
	 * @param st
	 */
	public void populate(PunctuationList partialList, Map tableValues, StringText st){
		Iterator it = partialList.iterator();
		while (it.hasNext()) {
			PunctuationSubfield subfield = (PunctuationSubfield) it.next();
			StringText stSubfields = st.getSubfieldsWithCodes(subfield.getCode());
			if(stSubfields.getNumberOfSubfields()>1){
				// TODO _MIKE repeated subfields! throw exception?
				throw new IllegalArgumentException("Sample has multiple subfields: "+partialList.toString());
			}
			Subfield stSubfield = stSubfields.getSubfield(0);
			if (subfield.getFilteredFieldList().size()>0) {
				extractValues(subfield, stSubfield, tableValues);
			}
		}
	}

	/*
	 * field1
	 * field1..
	 * ..field1
	 * ..field1..
	 * ..field1field2
	 * ..field1..field2
	 * ..field1..field2..
	 */
	private void extractValues(PunctuationSubfield subfield,
			Subfield stSubfield, Map tableValues) {
		Iterator it = subfield.getElements().iterator();
		String content = stSubfield.getContent();
		int previousPos = 0;
		int nextPos = 0;
		PunctuationElement last = null;
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(!(elem instanceof PunctuationField)){
				String expectedPunctuation = elem.getValue();
				nextPos = content.indexOf(expectedPunctuation, previousPos);
				if(nextPos<0) {
					// TODO _MIKE: error!. Are we sure that this subfield match the example?
					return;
				}
				last = addField(tableValues, content, last, previousPos, nextPos);
				// skip
				previousPos = nextPos + expectedPunctuation.length();
				nextPos = previousPos;
			} else {
				// PunctuationField
				if(last==null){
					last = elem;
				} else {
					// old
					last = addField(tableValues, content, last, previousPos, nextPos);
				}
			}
		}
		nextPos = content.length();
		last = addField(tableValues, content, last, previousPos, nextPos);
	}

	private PunctuationElement addField(Map tableValues, String content,
			PunctuationElement last, int previousPos, int nextPos) {
		if(last!=null){
			String text = content.substring(previousPos, nextPos);
			tableValues.put(last.getValue(), text);
			last = null;
		}
		return last;
	}

	/**
	 * Create a fields map with empty values
	 * this method is overloaded.
	 * @param selectedModel
	 * @param tableValues
	 */
	public void populate(TAG_MODEL selectedModel, Map tableValues, String variantCodes) {
		List fields = selectedModel.getPunctuationElements().getFilteredFieldList(variantCodes);
		Iterator it = fields.iterator();
		while (it.hasNext()) {
			PunctuationField field = (PunctuationField) it.next();
			tableValues.put(field.getValue(), "");
		}
	}
	
	/**
	 * this method is used when a model do not match with StringText but we
	 * need to populate map in any case (change model, selecting first model...)
	 * @param sample
	 * @param tableValues
	 * @param st
	 */
	public void forcePopulatation(PunctuationList/*<PunctuationSubfield>*/ sample, Map tableValues, StringText st){
		Iterator it = sample.iterator();
		while (it.hasNext()) {
			PunctuationSubfield subfield = (PunctuationSubfield) it.next();
			StringText stSubfields = st.getSubfieldsWithCodes(subfield.getCode());
			if(stSubfields.isEmpty()) {
				continue;
			}
			if(stSubfields.getNumberOfSubfields()>1){
				// TODO _MIKE repeated subfields! throw exception?
			}
			Subfield stSubfield = stSubfields.getSubfield(0);
			if (subfield.getFilteredFieldList().size()>0) {
				extractFirstValue(subfield, stSubfield, tableValues);
			}
		}
	}
	
	private void extractFirstValue(PunctuationSubfield subfield,
			Subfield stSubfield, Map tableValues) {
		PunctuationField firstField = (PunctuationField) subfield.getFilteredFieldList().get(0);
		String content = stSubfield.getContent();
		tableValues.put(firstField.getValue(), content);
	}
}
