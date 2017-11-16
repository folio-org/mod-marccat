/*
 * (c) LibriCore
 * 
 * Created on Dec 24, 2004
 * 
 * PublisherAccessPointComparator.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.Comparator;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/01/13 12:02:02 $
 * @since 1.0
 */
public class PublisherAccessPointComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof PublisherAccessPoint
			&& arg1 instanceof PublisherAccessPoint) {
			int i = ((PublisherAccessPoint) arg0).getSequenceNumber();
			int j = ((PublisherAccessPoint) arg1).getSequenceNumber();
			if ( i < j) {
				return -1;
			} else if (i > j) {
				return +1;
			} else {
				return 0;
			}
		}
		return 0;
	}

}
