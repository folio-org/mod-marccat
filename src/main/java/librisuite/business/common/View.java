/*
 * (c) LibriCore
 * 
 * Created on Jul 17, 2004
 * 
 * View.java
 */
package librisuite.business.common;

import java.util.Locale;

import librisuite.business.codetable.DAOCodeTable;
import librisuite.hibernate.DB_LIST;


/**
 * A collection of static methods to manage user views
 * @author paulm
 * MIKE: added view for mades
 * @version %I%, %G%
 * @since 1.0
 */
public class View {

	public static final int AUTHORITY = -1;
	public static final int ANY = 0; // pm 2011
	public static final int DEFAULT_BIBLIOGRAPHIC_VIEW = 1;
	
	/**
	 * Creates a new usr_vw_ind string from the input string by
	 * setting the position specified in arg2 to '0'.  The resultant
	 * view string is useful in saving a persistant object after the
	 * current cataloguing view of the record is deleted (or modified)
	 * 
	 * 
	 * @param viewString -- the original view String
	 * @param cataloguingView -- the position to be set to '0' (1 indexing)
	 */
		static public String maskOutViewString(String viewString, int cataloguingView) {
			if(cataloguingView<0) throw new IllegalArgumentException("view "+cataloguingView+" cannot be converted in string");
			char[] newArray = viewString.toCharArray();
			newArray[cataloguingView - 1] = '0';
			return new String(newArray);
		}

		/**
		 * Creates a new usr_vw_ind string from the input string by
		 * setting the position specified in arg2 to '1'.  The resultant
		 * view string is useful in saving a persistant object after the
		 * current cataloguing view of the record is added (based on a copy from
		 * existing views);
		 * 
		 * 
		 * @param viewString -- the original view String
		 * @param cataloguingView -- the position to be set to '1' (1 indexing)
		 */

		static public String maskOnViewString(String viewString, int cataloguingView) {
			if(cataloguingView<0) throw new IllegalArgumentException("view "+cataloguingView+" cannot be converted in string");
			
			char[] newArray = viewString.toCharArray();
			if(cataloguingView> 0)
				newArray[cataloguingView - 1] = '1';
			else newArray[0] = '1';
			
			return new String(newArray);
	
		}
	/**
	 * Creates a new usr_vw_ind string by
	 * setting all positions to '0' except the position specified in arg1.  
	 * The resultant view string is useful in saving a persistant object after the
	 * current cataloguing view of the record is saved or updated;
	 * 
	 * 
	 * @param cataloguingView -- the position to be set to '1' (1 indexing)
	 */

	static public String makeSingleViewString(int cataloguingView) {
		
		return maskOnViewString("0000000000000000", cataloguingView);
	}

	/**
	 * Determines the equivalent integer value view from the (single) view string
	 * 
	 * @param cataloguingView -- the position to be set to '1' (1 indexing)
	 */

	static public short toIntView(String userViewString) {
		return (short)(1 + userViewString.indexOf("1"));
	}

	/**
	 * pm 2011 Builds a human readable string of view names based on the
	 * settings of the argument (e.g. 1010000000000000 --> "B1, B3"
	 * 
	 * @param userViewString
	 * @param locale
	 * @return
	 */
	public static String getViewText(String userViewString, Locale locale) {
		char[] charArray = userViewString.toCharArray();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '1') {
				s.append(getViewText(i + 1, locale));
				s.append(", ");
			}
		}
		if (s.length() > 2) {
			s.delete(s.length() - 2, s.length() - 1);
		}
		return s.toString();
	}

	/**
	 * Returns the shortText associated with the given view and locale
	 * 
	 * @param view
	 * @param locale
	 * @return
	 */
	public static String getViewText(int view, Locale locale) {
		try {
			return new DAOCodeTable().load(DB_LIST.class, (short) (view),
					locale).getShortText();
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * Returns the shortText associated with the given view and locale
	 * 
	 * @param view
	 * @param locale
	 * @return
	 */
	public static String getCompleteViewText(int view, Locale locale) {
		try {
			return new DAOCodeTable().load(DB_LIST.class, (short) (view),
					locale).getLongText();
		} catch (Exception e) {
			return null;
		}
	}
	

	
}
