/*
 * (c) LibriCore
 * 
 * Created on Dec 24, 2004
 * 
 * PublisherAccessPointComparator.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.Comparator;

import librisuite.hibernate.PUBL_TAG;
import librisuite.hibernate.TAG_MODEL;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2009/12/14 22:24:41 $
 * @since 1.0
 */
public class PublisherTagComparator implements Comparator/*<PUBL_TAG>*/ {

	public int compare(Object arg0, Object arg1) {
		PUBL_TAG tm1 = (PUBL_TAG) arg0;
		PUBL_TAG tm2 = (PUBL_TAG) arg1;
		int i = tm1.getSequenceNumber();
		int j = tm2.getSequenceNumber();
		if ( i < j) {
			return -1;
		} else if (i > j) {
			return +1;
		} else {
			return 0;
		}
	}



}
