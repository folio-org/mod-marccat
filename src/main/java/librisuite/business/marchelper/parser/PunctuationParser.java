package librisuite.business.marchelper.parser;

import java.util.Iterator;
import java.util.List;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

public class PunctuationParser {
	
	private StringText stringText;

	public PunctuationParser(StringText stringText) {
		super();
		this.stringText = stringText;
	}

	public PunctuationList parse() throws PunctuationParsingException{
		PunctuationList subfieldList = new PunctuationList();
		List subfields = stringText.getSubfieldList();
		Iterator it = subfields.iterator();
		while(it.hasNext()){
			PunctuationSubfield  punctSubfield = parseSubfield((Subfield) it.next());
			subfieldList.add(punctSubfield);
		}
		return subfieldList;
	}

	/*
	 * @param subfield
	 * @return
	 */
	private PunctuationSubfield parseSubfield(Subfield subfield) throws PunctuationParsingException {
																				  // ^
		PunctuationList localSubfieldList = new PunctuationList();
		
		String content = subfield.getContent();
		PunctuationSubfield pSubfield = new PunctuationSubfield(subfield);
		
		int previousPoint = 0;
		int nextPoint = content.indexOf(PunctuationField.START_FIELD_SEPARATOR);                    // $p[:date}] {part}
//		boolean hasPunctuation = false; // punctuation marker
		
		while(nextPoint>-1){
			localSubfieldList.addPunctuation(content, previousPoint, nextPoint); // add [
//			hasPunctuation |= previousPoint<nextPoint;
			
			previousPoint = nextPoint + 1; // skip START_FIELD_SEPARATOR
			nextPoint = content.indexOf(PunctuationField.END_FIELD_SEPARATOR, previousPoint);       // $p[{date:] {part}
			
			localSubfieldList.addField(content, previousPoint, nextPoint); // add date
			previousPoint = nextPoint + 1; // skip END_FIELD_SEPARATOR
			nextPoint = content.indexOf(PunctuationField.START_FIELD_SEPARATOR, previousPoint);                    // $p[{date}] :part}
		}
		if(previousPoint<content.length()){
			localSubfieldList.addPunctuation(content, previousPoint);
//			hasPunctuation |= previousPoint<nextPoint;
		}
		
		pSubfield.setElements(localSubfieldList);
//		pSubfield.setPunctuationPresents(hasPunctuation);

		return pSubfield;
	}
}
