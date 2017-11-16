package librisuite.business.marchelper.parser;

import java.util.Iterator;

import com.libricore.librisuite.common.Subfield;

public class PunctuationSubfield extends PunctuationElement {
	
	private PunctuationList subfieldElements;

	/**
	 * @param value
	 */
	public PunctuationSubfield(Subfield subfield) {
		super();
		setValue(Subfield.SUBFIELD_DELIMITER + subfield.getCode());
	}

	public PunctuationList getElements() {
		return subfieldElements;
	}

	public void setElements(PunctuationList elements) {
		this.subfieldElements = elements;
	}

	public boolean isPunctuationPresents() {
		Iterator it = subfieldElements.iterator();
		while (it.hasNext()) {
			PunctuationElement element = (PunctuationElement) it.next();
			if(!(element instanceof PunctuationField)){
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return super.toString() + subfieldElements.toString();
	}
	
	public PunctuationList getFilteredFieldList(){
		PunctuationList fields = new PunctuationList();
		Iterator it = subfieldElements.iterator();
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(elem instanceof PunctuationField) {
				fields.add(elem);
			}
		}
		return fields;
	}
	
	public String getMarcCode(){
		return getValue();
	}
	
	public String getCode(){
		return getValue().substring(1);
	}
	
	/**
	 * Create a Subfield without PunctuationField(s)
	 * @return
	 */
	public Subfield createEmptySubfieldContent(){
		String content = "";
		Iterator it = subfieldElements.iterator();
		while (it.hasNext()) {
			PunctuationElement elem = (PunctuationElement) it.next();
			if(!(elem instanceof PunctuationField)) {
				content += elem.getValue();
			}
		}
		return new Subfield(getCode(), content);
	}

}
