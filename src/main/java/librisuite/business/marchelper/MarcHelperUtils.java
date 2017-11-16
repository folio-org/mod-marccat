package librisuite.business.marchelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

import librisuite.business.marchelper.parser.PunctuationList;

public final class MarcHelperUtils {
	
	public static String sortString(String s){
		char[] us = s.toCharArray();
		Arrays.sort(us);
		return new String(us) ;
	}

	public static String creatSampleMatchKey(PunctuationList/*<PunctuationSubfield>*/ sample){
		return sortString(sample.getCodesString());
	}

	public static String createStringTextMatchKey(StringText st){
		return sortString(MarcHelperUtils.getCodesString(st));
	}

	public static String getCodesString(StringText st) {
		List subfields = st.getSubfieldList();
		Iterator it = subfields.iterator();
		String result = ""; 
		while (it.hasNext()) {
			Subfield sf = (Subfield) it.next();
			result += sf.getCode();
		}
		return result;
	}

}
