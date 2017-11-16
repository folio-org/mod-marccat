package librisuite.business.searching;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class for implementers of the SearchEngine interface
 * 
 * @author paul
 *
 */
public class SearchEngineHelper {
	private static Log logger = LogFactory.getLog(SearchEngineHelper.class);
	/*
	 * Hashtable operators is keyed on locale and populated just-in-time with
	 * the locale specific boolean operator values (a String[])
	 */
	private static Map operators = new Hashtable();
	private static Map defaultSearchIndex = new Hashtable();
	private static String[] relationsTable = new String[] { "dummy", "<", "<=",
		"=", ">", ">=" };

	public String buildCclQuery(String query, String use, Locale locale) {
		StringBuffer buf = new StringBuffer();

		logger.debug("query is '" + query + "'");
		if (use == null || use.equals("")) {
			use = getDefaultSearchIndex(locale);
			logger.debug("Using default index: '" + use + "'");
		}
		buf.append(use + " = ");
		if (query.trim().matches("\".*\"")) {
			buf.append(query);
		} else {
			String[] words = query.trim().split(" ");
			for (int i = 0; i < words.length - 1; i++) {
				buf.append(words[i] + " " + getLocalisedOperator("1", locale)
						+ " " + use + " = ");
			}
			buf.append(words[words.length - 1]);
			logger.debug("buf is: '" + buf.toString() + "'");
		}
		return buf.toString();
	}
	public String buildCclQuery(List termList, List relationList,
			List useList, List operatorList, Locale locale) {

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < useList.size(); i++) {
			if (i > 0) {
				/*
				 * Don't be confused by the positions and values of the boolean
				 * operators in the list (as I was). Even though the jsp shows
				 * for example "or" in position 0 and "no operator" in position
				 * 1, when it gets to this search, the 0th value is always "and"
				 * and should be ignored. The other "real" operators are shifted
				 * down 1 position and the "no operator" is dropped from the
				 * list. So, whereas visually on the page it looks like the
				 * operator is placed after the term, in this list, the operator
				 * is placed before the term. I assume that this is the way
				 * LibriVision expected these values so I have not changed it
				 * [for now]
				 */
				buf.append(" "
						+ getLocalisedOperator((String) operatorList.get(i),
								locale) + " ");
			}
			buf.append(useList.get(i) + " ");
			buf.append(relationsTable[Integer.parseInt((String) relationList
					.get(i))]);
			
			String queryText = ((String)termList.get(i)).replace("\"", "");
			String queryString = "\"" +queryText + "\"";			
			buf.append(" " + queryString + " ");

		}
		logger.debug("buf: '" + buf.toString() + "'");
		return buf.toString();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDefaultSearchIndex(Locale locale) {
		String result = (String) defaultSearchIndex.get(locale);
		if (result == null) {
			ResourceBundle bundle = ResourceBundle.getBundle(
					"resources/searching/simpleSearch", locale);
			result = bundle.getString("defaultSearchIndex");
			defaultSearchIndex.put(locale, result);
		}
		return result;
	}
	
	private String getLocalisedOperator(String code, Locale locale) {
		String[] results = (String[]) operators.get(locale);
		logger.debug("looking for boolean operator " + code);
		if (results == null) {
			results = new String[6];
			results[0] = "";
			ResourceBundle bundle = ResourceBundle.getBundle(
					"resources/searching/advancedSearch", locale);
			results[1] = bundle.getString("and");
			results[2] = bundle.getString("or");
			results[3] = bundle.getString("not");
			results[4] = bundle.getString("near");
			results[5] = bundle.getString("with");
			operators.put(locale, results);
		}
		logger.debug("returning " + results[Integer.parseInt(code)]);
		return results[Integer.parseInt(code)];
	}

}
