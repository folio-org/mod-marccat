/*
 * (c) LibriCore
 * 
 * Created on Aug 5, 2004
 * 
 * BibliographicNoteType.java
 */
package librisuite.hibernate;

/**
 * @author hansv
 * @version %I%, %G%
 */
public class BibliographicRelationReciprocal extends T_SINGLE {
	public static final short RECIPROCAL = 1;
	public static final short ONE_WAY = 2;
	public static final short BLIND = 3;
	
	public static boolean isBlind(short code) {
		return code == BLIND;
	}

	public static boolean isReciprocal(short code) {
		return code == RECIPROCAL;
	}

	public static boolean isOneWay(short code) {
		return code == ONE_WAY;
	}

}
