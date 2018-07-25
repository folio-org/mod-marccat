package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.dao.persistence.PUBL_TAG;

import java.util.Comparator;

/**
 * @author paulm
 * @since 1.0
 */
public class PublisherTagComparator implements Comparator {

	public int compare(final Object arg0, final Object arg1) {
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
