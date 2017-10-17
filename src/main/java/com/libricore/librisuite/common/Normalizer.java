/*
 * (c) LibriCore
 * 
 * Created on Mar 24, 2006
 * 
 * Normalizer.java
 */
package com.libricore.librisuite.common;

import java.text.CollationElementIterator;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A utility class to normalize Unicode stings
 * 
 * Uses the RuleBasedCollator class to build a hashMap of the collationKeys
 * for the provided baseAlphabet.  
 * 
 * The default mask folds accented letters into the corresponding base character.  A mask
 * of 0xFFFF0000 can be used to fold into uppercase characters.
 * 
 * The default constructor provides a Latin alphabet for the default Locale.  The mask,
 * alphabet and locale can be modified through the appropriate setters.  After modifications,
 * the compile() method should be invoked to rebuild the hashMap.
 * 
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/03/24 13:06:42 $
 * @since 1.0
 */
public class Normalizer {
	private Map baseAlphabetMap = new HashMap();
	private String alphabet;
	private int mask = 0xFFFF00FF;
	private Locale locale;
	private Collator collator;
	private CollationElementIterator iter;

	public Normalizer() {
		alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		mask = 0xFFFF00FF;
		locale = Locale.getDefault();
		compile();
	}
	
	public void compile() {
		collator = Collator.getInstance(locale);
		iter = ((RuleBasedCollator)collator).getCollationElementIterator(alphabet);
		int i;
		while ((i = iter.next()) != CollationElementIterator.NULLORDER) {
			baseAlphabetMap.put(
				new Integer(i & mask),
				new Character(alphabet.charAt(iter.getOffset() - 1)));
		}
	}

	public String normalize(String input) {
		iter.setText(input);
		StringBuffer result = new StringBuffer();
		int i;
		while ((i = iter.next()) != CollationElementIterator.NULLORDER) {
			int primary = CollationElementIterator.primaryOrder(i);
			if (primary > 0) {
				Character c = (Character) baseAlphabetMap.get(new Integer(i & mask));
				if (c != null) {
					result.append(c);
				} else {
					result.append(input.charAt(iter.getOffset() - 1));
				}
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAlphabet(String string) {
		alphabet = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setMask(int i) {
		mask = i;
	}

}
