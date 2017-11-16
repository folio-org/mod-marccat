/*
 * (c) LibriCore
 * 
 * Created on Aug 28, 2004
 * 
 * TagComparator.java
 */
package librisuite.business.common.group;

import java.util.Comparator;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.OrderedTag;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SequenceNumberComparator implements Comparator {
	private static final Integer UNSORTED = new Integer(Integer.MAX_VALUE);
	
	private static final Log logger = LogFactory.getLog(SequenceNumberComparator.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(final Object tag0, final Object tag1) {
		if(tag0 == tag1) {
			return 0;
		}
		if(tag0 instanceof OrderedTag && tag1 instanceof OrderedTag){
			Integer seq0 = ((OrderedTag)tag0).getSequenceNumber();
			Integer seq1 = ((OrderedTag)tag1).getSequenceNumber();
			/*test soggetti*/
			/*if(seq0.intValue()==0) seq0 = UNSORTED;
			if(seq1.intValue()==0) seq1 = UNSORTED;*/
			if(seq0==null) seq0 = UNSORTED;
			if(seq1==null) seq1 = UNSORTED;
			int snResult = seq0.compareTo(seq1);
			if(snResult!=0) {
				return snResult; // sequenceNumber assegnati e differenti
			}
		}
		// hanno lo stesso sequenceNumber o non Ã¨ assegnato o uno dei due non ha sequenceNumber: 
		// ordina per numero di tag
		return compareTagNumber(tag0, tag1);
	}


	/**
	 * Esegue il confronto basandosi sul numero di tag ignorando gli indicatori
	 * @param tag0
	 * @param tag1
	 * @return
	 */
	protected int compareTagNumber(final Object tag0, final Object tag1) {
		int compareResult = 0;
		try {
			compareResult = ((Tag) tag0).getMarcEncoding().getMarcTag().compareTo(
			((Tag) tag1).getMarcEncoding().getMarcTag());
		}
		catch (MarcCorrelationException e) {
			logger.warn("MarcCorrelationException sorting in tag order");
		} 
		catch (DataAccessException e) {
			logger.warn("DataAccessException sorting in tag order");
		} 
		return compareResult;
	}
}
