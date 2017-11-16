package librisuite.business.marchelper;

import librisuite.business.marchelper.parser.PunctuationList;
import librisuite.hibernate.TAG_MODEL;

import org.apache.commons.collections.Predicate;

import com.libricore.librisuite.common.StringText;

public class TagModelPredicate implements Predicate {
	String stringTextKey = null;
	
	/**
	 * @param st StringText of the current tag
	 */
	public TagModelPredicate(StringText st) {
		super();
		this.stringTextKey = MarcHelperUtils.createStringTextMatchKey(st);
	}

	/**
	 * return true if StringText is a subset of TAG_MODEL
	 * i.e: StringText $a; TAG_MODEL $a return true
	 * i.e: StringText $a; TAG_MODEL $a$b return true
	 * i.e: StringText $a$n; TAG_MODEL $a$b return false
	 * i.e: StringText $a$n$p; TAG_MODEL $a$p$n return true
	 * i.e: StringText $a$c; TAG_MODEL $a$b$c return true
	 */
	public boolean evaluate(Object modelObject) {
		TAG_MODEL model = (TAG_MODEL) modelObject;
		PunctuationList sample = model.getPunctuationElements();
		String sampleKey = MarcHelperUtils.creatSampleMatchKey(sample);
		if(stringTextKey.equalsIgnoreCase(sampleKey)){
			return true;
		}
		if(sampleKey.contains(stringTextKey)){
			return true;
		}
		return isSubset(sampleKey);
	}

	private boolean isSubset(String sampleKey) {
		int length = stringTextKey.length();
		for(int i=0; i<length; i++){
			if(sampleKey.indexOf(stringTextKey.charAt(i))<0){
				return false; 
			}
		}
		return true;
	}

}
