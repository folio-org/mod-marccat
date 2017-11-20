package org.folio.cataloging.business.marchelper.parser;

import java.util.Iterator;
import java.util.Map;

public class FieldValueReplacer {

	public FieldValueReplacer() {
		super();
	}

	public String replaceFieldValue(Map values, PunctuationList punctSubfieldList){
		Iterator it = punctSubfieldList.iterator();
		String result = "";
		while (it.hasNext()) {
			PunctuationSubfield elem = (PunctuationSubfield) it.next();
			result += replaceFieldValue(values, elem);
		}
		return result;
	}
	
	public String replaceFieldValue(Map values, PunctuationSubfield subfield){
		Iterator it = subfield.getElements().iterator();
		String result = subfield.getMarcCode();
		String element;
		while (it.hasNext()) {
			PunctuationElement pe = (PunctuationElement) it.next();
			if(pe instanceof PunctuationField) {
				element = (String)values.get(pe.getValue());
			} else {
				element = pe.getValue();
			}
			if(element==null){
				element="";
			}
			result += element;
		}
		return result;
	}
}
