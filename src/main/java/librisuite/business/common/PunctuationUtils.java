/*
 * (c) LibriCore
 * 
 * Created on Apr 19, 2006
 * 
 * PunctuationUtils.java
 */
package librisuite.business.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to provide static methods useful in parsing/formatting Marc data.
 * 
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/04/27 12:56:53 $
 * @since 1.0
 */
public class PunctuationUtils {
	private static final Log logger = LogFactory.getLog(PunctuationUtils.class);
	private static String[] roman = {"I","II","III","IV","V","VI","VII","VIII","IX","X",
		"XI","XII","XIII","XIV","XV","XVI","XVII","XVIII","XIX","XX"};
	
	public static String strip(String s) {
	//	logger.debug("strip('" + s + "')");
		String result = s.trim();
		if (result.endsWith(".")) {
			return result.substring(0, result.length() - 1);
		}
		else if (result.endsWith(";")) {
			return result.substring(0, result.length() - 1);
		}
		else {
			return result;
		}
	}
	
	
	
	public static String toRoman(int i) {
		return i<=20 ? roman[i-1] : "...";
	}
}
