package org.folio.cataloging.business.searching;

import org.folio.cataloging.business.codetable.Avp;

import java.util.Comparator;

/**
 * Comparator for weighted AVPs.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class WeightedAvpComparator implements Comparator<Avp> {
	@Override
	public int compare(final Avp _1, final Avp _2) {
		final String s0 = _1.getLabel();
		final String s1 = _2.getLabel();
		if (s0.length() > s1.length()) return -1;
		if (s1.length() > s0.length()) return 1;
		if (s0.indexOf(s1) >= 0) return -1;
		if (s1.indexOf(s0) >= 0) return 1;
		return 0;
	}
}