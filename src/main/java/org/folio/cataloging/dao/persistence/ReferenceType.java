/*
 * (c) LibriCore
 * 
 * Created on Jun 18, 2004
 * 
 * T_REF_TYPE.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class ReferenceType extends T_SINGLE {
	public static final short SEEN_FROM = 2;
	
	static public boolean isSeeAlso(short type) {
		return type == 3;
	}

	static public boolean isSeeAlsoFrom(short type) {
		return type == 4;
	}

	static public boolean isEquivalence(short type) {
		return type == 5;
	}
	
	static public boolean isSee(short type){
		return type == 1;
	}

	static public boolean isSeenFrom(short type){
		return type == 2;
	}

	static public boolean isAuthorityTag(short type){
		return type == 2 || type == 4 || type == 5;
	}

	static public short getReciprocal(short type) {
		switch (type) {
			case 1 :
				return 2;
			case 2 :
				return 1;
			case 3 :
				return 4;
			case 4 :
				return 3;
				 //Aggiunto per le REF: BT e NT
			case 6 :
				return 7;
			case 7 :
			   return 6;
			default :
				return type;
		}
	}
}
