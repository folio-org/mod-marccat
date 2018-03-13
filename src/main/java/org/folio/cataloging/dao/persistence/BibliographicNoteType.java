package org.folio.cataloging.dao.persistence;

/**
 * @author paulm
 * @version %I%, %G%
 */
public class BibliographicNoteType extends T_SINGLE {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int[] publisherTypes = {24,381,382,410,411,412,413,414,415,
		                                           416,417,418,419,420,421,422,423,424};

	public static boolean isPublisherType(int value) {
		for(int s=0;s<publisherTypes.length;s++){
			if (value == publisherTypes[s]) {
				//System.out.println("publisherTypes: "+publisherTypes[s]);
				return true;
			}
		}
		return false;
	}
}
