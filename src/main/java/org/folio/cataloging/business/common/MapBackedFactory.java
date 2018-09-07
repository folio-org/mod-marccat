package org.folio.cataloging.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link AbstractMapBackedFactory}.
 *
 * @author paulm
 * @since 1.0
 */
public class MapBackedFactory extends AbstractMapBackedFactory {

	private final static Map<Integer, Class> map = new HashMap<>();
	/*static {
		map.put(1, MaterialDescription.class);
		map.put(2, NameAccessPoint.class);
		map.put(3, TitleAccessPoint.class);
		map.put(4, SubjectAccessPoint.class);
		map.put(5, ControlNumberAccessPoint.class);
		map.put(6, ClassificationAccessPoint.class);
		map.put(7, BibliographicNoteTag.class);
		map.put(8, BibliographicRelationshipTag.class);
		map.put(11, NameTitleAccessPoint.class);
	};*/

	@Override
	public void put(final Integer key, final Class clazz) {
		map.put(key,clazz);
	}

	@Override
	public void put(Map<Integer, Class> entries) {
		map.putAll(entries);
	}

	@Override
	protected Object getInstance(final Integer key) {
		return newInstance(map.get(key));
	}

	@Override
	public void clear() {
		map.clear();
	}
}
